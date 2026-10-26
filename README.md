# Weekly Life Planner

A starter project based on the supplied app concept. Includes:
- Native Android app (Kotlin + Jetpack Compose)
- Responsive Web/PWA version (installable on supported browsers)
- GitHub Actions workflow that builds a debug APK and uploads it as an artifact

## Default preferences
- Work starts at 08:00; 8 working hours on weekdays (split 08:00–12:00 and 13:00–17:00)
- Sleep 23:00–07:00 (8 hours every day)
- Weekend work off
- Balanced lifestyle
- English (`en`)

The schedule generator uses fixed, non-overlapping blocks and validates the result. Saturday and Sunday are work-free. Activities include meals, coffee, sports, study, shopping, movies, outdoor time, and personal time.

## Open in Android Studio
1. Open this folder in Android Studio (Giraffe or newer recommended).
2. Let Gradle sync.
3. Run the `app` configuration on an emulator or Android device.

This starter uses Android Gradle Plugin 8.7.3, Gradle 8.9, Kotlin 2.0.21 and Compose. The GitHub workflow installs Gradle and Android SDK components on the runner; no wrapper JAR is required for CI.

## Build a debug APK locally
With Gradle 8.9 installed and Android SDK configured:
```bash
gradle assembleDebug
```
APK output: `app/build/outputs/apk/debug/app-debug.apk`

## GitHub Actions
Push the project to GitHub. The workflow at `.github/workflows/android-debug.yml` runs on pushes and pull requests and uploads `app-debug.apk` as an artifact. You can also run it manually from the Actions tab.

## Web/PWA
Open `web/index.html` in a local static server, or publish the `web/` directory with GitHub Pages. For service-worker support, use HTTPS or localhost. Example:
```bash
python -m http.server 8080 --directory web
```
Then visit `http://localhost:8080`.

## Notes
This is a functional starter, not a production release. App signing, automated tests, localization files beyond English, and persistent cloud sync are not configured. Preferences are editable in both clients; the generated week is deterministic for the selected settings.

## Version 2 improvements
- **Appearance:** five bright accent themes (Fresh mint, Sunny peach, Sky blue, Lavender, Rose pink).
- **Schedule display:** switch between a day-by-day agenda and a week calendar with hourly time markers.
- **Activity tracking:** optional completion checkboxes on each activity, plus a completed count on the home screen.
- **Refreshed UI:** softer cards, rounded corners, friendly emoji markers and more spacious layouts in Android and PWA.
- Existing Android launcher icons and GitHub Actions debug APK workflow are retained.

Note: This source package has been ZIP-integrity checked. Run the included GitHub Actions workflow to validate the Android build and produce the APK.


## v1.0.1 update
- Language selector now applies Vietnamese labels in the Web/PWA and key Android navigation labels.
- Week calendar displays all 24 hours (00:00–23:00), so late-evening and overnight schedule slots are not cut off at 15:00.
- Android version bumped to 1.0.1; GitHub Actions uploads `Weekly-Life-Planner-v1.0.1-debug.apk` (filename is generated from `versionName`).

## v1.0.2 — Shared language system

- Added shared English/Vietnamese translation lookup for Android Compose screens and translated activity names, day labels, meal categories, settings, and navigation labels.
- Saved language/theme/calendar preferences and completion state with Android SharedPreferences.
- Web/PWA translates text nodes across the full page, including regenerated calendar and meal content, and persists language in local storage.
- Removed Month from the Web/PWA calendar view selector; Day and Week remain available.
- Updated APK version to 1.0.2 and made GitHub Actions artifact names use the versionName from app/build.gradle.kts.

## Validation performed

- JavaScript syntax check: `node --check web/app.js` passed.
- ZIP integrity verified after packaging.
- Android Gradle build was not run in this environment; run the GitHub Actions workflow to confirm Kotlin/Gradle compilation.

## Public recipe discovery (v1.0.3)
The Meals tab provides rotating Vietnamese meal ideas and links to public Cookidoo search pages and the official Vietnam Cookidoo portal. Recipe pages are opened on the original site; this app does not scrape, copy, or bundle third-party recipe instructions. Some pages may require an account or subscription. Always check allergies and dietary needs.

## APK filename
The GitHub Actions workflow uploads an artifact named `Weekly-Life-Planner-v1.0.3-debug-APK` containing `Weekly-Life-Planner-v1.0.3-debug.apk`. Android's intermediate build output remains `app-debug.apk`; the workflow copies and renames it for download.
