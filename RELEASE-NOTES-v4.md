# Weekly Life Planner v4 — usability improvements

## Included
- Brighter, softer visual design with responsive mobile styling and clearer focus/hover states.
- Month grid now uses the actual current month, correct weekday offset, and the real number of days in the month.
- Android settings and activity-completion state are persisted with SharedPreferences.
- Fixed the Compose month-cell click target to avoid the incompatible clickable `Card` overload that caused the reported Kotlin argument errors.
- Existing day agenda, week time-grid, month view, color themes, tracking toggle, schedule settings, PWA local storage, and GitHub Actions workflow retained.

## Verification
- `node --check web/app.js` passes.
- ZIP integrity checked after packaging.
- An Android APK build could not be run in this environment because Android SDK / Gradle build tooling is not installed. Use GitHub Actions to verify Kotlin compilation and produce the APK.
