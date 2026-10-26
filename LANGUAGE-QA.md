# Language QA checklist (v1.0.2)

Use this checklist on Android APK and Web/PWA after build/deploy.

- [ ] Set language to Tiếng Việt; verify Home headings, statistics, navigation, buttons and day names are Vietnamese.
- [ ] Open My week in Day mode; verify day chips, activity names and completion text are Vietnamese.
- [ ] Switch to Week calendar; verify title, time rail, weekday headers and event names are Vietnamese.
- [ ] Open Meals; verify page title, meal-slot labels, refresh button and explanatory note are Vietnamese; Vietnamese dish names remain unchanged.
- [ ] Open Settings; verify theme names, display modes, tracking description, routine labels and dropdown options are Vietnamese.
- [ ] Close and reopen the app; verify language and settings remain selected.
- [ ] Switch back to English; verify all screens return to English without mixed-language labels.
- [ ] Mark an activity done, restart the app, and verify completion persists.

Automated checks completed in this package: Web JavaScript syntax and ZIP integrity. The Android build and on-device visual QA still require GitHub Actions and device testing.
