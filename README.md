# Talk to AI

A multiplatform app for chatting with an AI assistant. Have conversations with AI, create multiple chats, and keep message history on your device.

**TalkToAIAssistant** is a cross-platform client for AI chat using an OpenAI-compatible API.

---

## Features

- **AI chats** — Multiple chats, message history, create and delete chats and messages
- **Authorization** — Sign in with Google or email/password, or continue without an account
- **Onboarding** — First-launch screen with a short app overview
- **Settings** — Account, language (including Ukrainian), theme (light/dark), feedback, privacy policy, chat settings

---

## Platforms

| Platform | Support |
|----------|---------|
| Android  | ✅      |
| iOS      | ✅      |
| Desktop  | ✅ (JVM)|
| Web      | ✅ (JS) |

---

## Tech stack

- **Kotlin Multiplatform** + **Compose Multiplatform** — Shared UI across all platforms
- **Ktor** — HTTP client, AI API requests
- **SQLDelight** — Local database (chats and messages)
- **Koin** — Dependency injection
- **Firebase** — Crashlytics, Google Services
- **Google Auth** — Authentication
- **Coil** — Image loading
- **Lottie** — Animations
- **Datastore** — Preferences

Chat API is **OpenAI-compatible** (`/v1/chat/completions`). API key and base URL are configured via the project’s custom **KMP Secrets** plugin (`io.github.tarasovvp.kmp-secrets-plugin`).

---

## Project structure

```
TalkToAI/
├── composeApp/     # Shared UI (Compose) and entry points for Android, iOS, Desktop, Web
├── shared/         # Shared logic: network (AI + auth), repositories, use cases,
│                   # ViewModels, strings, navigation
├── authorization/  # Authorization module
├── core/           # Additional shared logic
├── iosApp/         # Swift/SwiftUI wrapper for iOS
└── build.gradle.kts
```

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

Or open the project in Android Studio and run the **composeApp** configuration for Android.

### Desktop (JVM)

```bash
./gradlew :composeApp:run
```

### Web

```bash
./gradlew :composeApp:jsBrowserDevelopmentRun
```

The output bundle will be in `composeApp/build/dist/js`.

### iOS

1. Build the shared framework:
   ```bash
   ./gradlew :shared:linkReleaseFrameworkIosArm64
   ```
2. Open `iosApp/talktoai.xcodeproj` in Xcode and run the app on a simulator or device.

---

## Configuration

### Secrets (AI API)

The app uses a custom **KMP Secrets** plugin. In `shared`, configure:

- `AI_BASE_URL` — Base URL of the OpenAI-compatible API
- `AI_API_KEY` — API key
- `ORGANIZATION_ID` — Organization ID (if required)
- `PROJECT_ID` — Project ID (if required)

Secret setup depends on your configuration (e.g. `local.properties`, environment variables, or the plugin’s config).

### Android release signing

In the project root `local.properties`, set (optional for release builds):

- `STORE_FILE`
- `STORE_PASSWORD`
- `KEY_ALIAS`
- `KEY_PASSWORD`

---

## License

Specify the project license when publishing.
