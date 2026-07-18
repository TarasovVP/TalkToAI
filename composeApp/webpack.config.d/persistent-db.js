const path = require('path');
const { NormalModuleReplacementPlugin } = require('webpack');

// build/js/node_modules is where yarn installs JS packages;
// add it to module resolution so our worker file can import sql.js
config.resolve.modules = [
    path.resolve(__dirname, '../../node_modules'),
    'node_modules'
];

config.plugins.push(
    new NormalModuleReplacementPlugin(
        /sqljs\.worker\.js$/,
        path.resolve(__dirname, '../../../../composeApp/workers/sqljs-persistent.worker.js')
    )
);
