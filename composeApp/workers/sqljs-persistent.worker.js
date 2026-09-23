import initSqlJs from "sql.js";

const IDB_NAME = "TalkToAI_SQLite";
const STORE_NAME = "database";
const IDB_KEY = "db";

let db = null;
let inTransaction = false;
let saveQueue = Promise.resolve();

function queueSaveToIDB() {
  saveQueue = saveQueue.then(() => saveToIDB());
  return saveQueue;
}

function openIDB() {
  return new Promise((resolve, reject) => {
    const req = indexedDB.open(IDB_NAME, 1);
    req.onupgradeneeded = (e) => e.target.result.createObjectStore(STORE_NAME);
    req.onsuccess = (e) => resolve(e.target.result);
    req.onerror = () => reject(req.error);
  });
}

async function loadFromIDB() {
  try {
    const idb = await openIDB();
    return await new Promise((resolve) => {
      const tx = idb.transaction(STORE_NAME, "readonly");
      const req = tx.objectStore(STORE_NAME).get(IDB_KEY);
      req.onsuccess = () => resolve(req.result ?? null);
      req.onerror = () => resolve(null);
    });
  } catch {
    return null;
  }
}

async function saveToIDB() {
  try {
    const data = db.export();
    const idb = await openIDB();
    await new Promise((resolve) => {
      const tx = idb.transaction(STORE_NAME, "readwrite");
      tx.objectStore(STORE_NAME).put(data, IDB_KEY);
      tx.oncomplete = resolve;
      tx.onerror = resolve;
    });
  } catch {}
}

async function createDatabase() {
  const SQL = await initSqlJs({ locateFile: () => '/sql-wasm.wasm' });
  try {
    const saved = await loadFromIDB();
    db = saved ? new SQL.Database(saved) : new SQL.Database();
  } catch {
    db = new SQL.Database();
  }
}

function onModuleReady() {
  const data = this.data;
  switch (data && data.action) {
    case "exec": {
      if (!data["sql"]) throw new Error("exec: Missing query string");
      const results = db.exec(data.sql, data.params);
      const sql = (data.sql || "").trimStart().toUpperCase();
      if (!inTransaction && /^(INSERT|UPDATE|DELETE|CREATE|DROP|ALTER|REPLACE)/.test(sql)) queueSaveToIDB();
      return postMessage({ id: data.id, results: results[0] ?? { values: [] } });
    }
    case "begin_transaction":
      inTransaction = true;
      return postMessage({ id: data.id, results: db.exec("BEGIN TRANSACTION;") });
    case "end_transaction": {
      const results = db.exec("END TRANSACTION;");
      inTransaction = false;
      queueSaveToIDB();
      return postMessage({ id: data.id, results });
    }
    case "rollback_transaction":
      inTransaction = false;
      return postMessage({ id: data.id, results: db.exec("ROLLBACK TRANSACTION;") });
    default:
      throw new Error(`Unsupported action: ${data && data.action}`);
  }
}

function onError(err) {
  return postMessage({ id: this.data.id, error: err });
}

if (typeof importScripts === "function") {
  db = null;
  const sqlModuleReady = createDatabase();
  self.onmessage = (event) => {
    return sqlModuleReady
      .then(onModuleReady.bind(event))
      .catch(onError.bind(event));
  };
}
