# Talk to AI

A multiplatform app for chatting with AI assistants. Create multiple chats, switch between OpenAI and Anthropic models, attach images, and keep message history on your device — optionally synced to the cloud when signed in.

---

## Features

- **AI chats** — Multiple chats, reorderable chat list (drag & drop), message history, create/rename/delete chats and messages
- **Multiple AI providers** — OpenAI and Anthropic (Claude), selectable globally and per chat
- **Model tiers** — Choose a tier (Fast / Balanced / Powerful) instead of a specific model; the tier is resolved to a concrete model of the selected provider
- **Streaming responses** — Answers stream in token by token with message statuses (requesting, streaming, success, error) and truncation detection
- **Image attachments** — Pick an image, compress it, store it locally and send it to vision-capable models; a small thumbnail is synced so images can still be shown when the original file is unavailable on another device
- **Per-chat settings** — Provider, model tier, temperature and context for each chat
- **Context & token usage** — Context usage indicator and input/output/cache token counts per message
- **Remote model config** — The list of available models (id, tier, vision support, context window, output limit) is loaded from Firestore, validated and cached, with a built-in fallback list
- **Authorization** — Sign in with Google or email/password, sign up, password reset, or continue without an account
- **Cloud sync** — For signed-in users chats, messages, user data and settings are stored in Firestore; without an account everything stays in local SQLite (local storage is always written)
- **Onboarding** — First-launch screen with a short app overview
- **Settings** — Account, language (including Ukrainian), theme (light/dark), chat defaults, privacy policy

---

## Platforms

| Platform | Support  |
|----------|----------|
| Android  | ✅       |
| iOS      | ✅       |
| Desktop  | ✅ (JVM) |
| Web      | ✅ (JS)  |

---

## Tech stack

- **Kotlin Multiplatform** + **Compose Multiplatform** — Shared UI across all platforms
- **Ktor** — HTTP client for AI APIs (SSE streaming), Firebase Auth REST and Firestore REST
- **kotlinx.serialization** / **kotlinx.datetime** / **Coroutines & Flow**
- **SQLDelight** — Local database (chats and messages)
- **Koin** — Dependency injection
- **Firebase (REST)** — Authentication (Identity Toolkit / Secure Token API) and Firestore via REST; Crashlytics / Google Services Gradle plugins on Android
- **Google Sign-In** — Google authentication
- **Coil 3** — Image loading
- **Lottie** — Animations
- **DataStore** — Preferences
- **KMP Secrets** (`io.github.tarasovvp.kmp-secrets-plugin`) — Generates the `Secrets` object from `local.properties`

---

## AI providers

| Provider  | Endpoint / auth                                         | Models (built-in fallback)                          |
|-----------|---------------------------------------------------------|-----------------------------------------------------|
| OpenAI    | OpenAI-compatible chat completions, `Bearer` API key    | GPT-5.6 Luna / Terra / Sol                          |
| Anthropic | Messages API with SSE streaming, `x-api-key` header     | Claude Haiku 4.5 / Sonnet 5 / Opus 4.8              |

Each model has a tier (`FAST`, `BALANCED`, `POWERFUL`), vision support, context window and max output tokens. The effective provider for a chat is the chat's own provider, falling back to the global one. The model list can be overridden at runtime from Firestore (`SyncAiModelsConfigUseCase`).

---

## Project structure

```
TalkToAI/
├── composeApp/      # Entry points per platform (Android, iOS, Desktop, Web), platform Koin modules
├── shared/          # App logic and UI: screens, ViewModels, navigation, theme, strings,
│                    # SQLDelight database, Firestore client, repositories, use cases,
│                    # image picker / compressor / storage
├── ai/              # AI providers: OpenAI and Anthropic clients, request mappers,
│                    # SSE parsing, model catalog, tiers
├── authorization/   # Firebase Auth REST client, auth repository and use cases
├── network/         # Common network constants/extensions, Result type, generated Secrets
├── settings/        # Preferences (DataStore) and settings use cases
├── core/            # Shared domain models (e.g. MessageContent)
├── iosApp/          # Swift/SwiftUI wrapper for iOS
├── docs/            # Design specs and implementation plans
└── build.gradle.kts
```

Screens (in `shared`): onboarding, login, sign up, chat list, chat (with chat settings bottom sheet), settings (list, account, login, sign up, language, theme, chat, privacy policy).

---

## Requirements

- **JDK 21**
- **Android**: minSdk 24, compileSdk 36
- **iOS**: Xcode and target setup (iosX64, iosArm64, iosSimulatorArm64)
- **Gradle**: version from the project wrapper

---

## Running the app

### Android

```bash
./gradlew :composeApp:installDebug
```

Or open the project in Android Studio and run the **composeApp** configuration.

### Desktop (JVM)

```bash
./gradlew :composeApp:run
```

### Web

```bash
./gradlew :composeApp:jsBrowserDevelopmentRun
```

### iOS

1. Build the shared framework:
   ```bash
   ./gradlew :shared:linkReleaseFrameworkIosArm64
   ```
2. Open `iosApp/talktoai.xcodeproj` in Xcode and run the app on a simulator or device.

### Tests

```bash
./gradlew allTests
```

Unit tests cover the AI module (request mapping, SSE parsing, provider selection, model catalog, streaming) and the network module.

---

## Configuration

### Secrets

Secrets are read by the KMP Secrets plugin (applied in the `network` module) from `network/local.properties` and exposed as `secrets.Secrets`. This file is not committed. Required keys:

| Key                    | Purpose                                              |
|------------------------|------------------------------------------------------|
| `OPENAI_BASE_URL`      | Base URL of the OpenAI-compatible API                |
| `OPENAI_API_KEY`       | OpenAI API key                                       |
| `ANTHROPIC_API_KEY`    | Anthropic API key                                    |
| `ORGANIZATION_ID`      | OpenAI organization ID (if required)                 |
| `PROJECT_ID`           | OpenAI project ID (if required)                      |
| `AUTH_BASE_URL`        | Firebase Auth (Identity Toolkit) base URL            |
| `AUTH_API_KEY`         | Firebase Web API key                                 |
| `ANDROID_CLIENT_ID`    | Google Sign-In client ID                             |
| `FIRESTORE_BASE_URL`   | Firestore REST base URL                              |
| `FIRESTORE_PROJECT_ID` | Firestore / Firebase project ID                      |

### Android release signing

In the root `local.properties` (optional for release builds):

- `STORE_FILE`
- `STORE_PASSWORD`
- `KEY_ALIAS`
- `KEY_PASSWORD`

---

## License

Specify the project license when publishing.
