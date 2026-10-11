# UI and launcher icon fixes

- Weekday selector changed to a horizontally scrolling `LazyRow` so Saturday and Sunday remain accessible on narrow screens.
- Added Android launcher icons for mdpi through xxxhdpi and adaptive icon XML for Android 8.0+.
- Registered `android:icon` and `android:roundIcon` in AndroidManifest.xml.
- Preserved the existing GitHub Actions workflow; if it was absent, a build-and-upload workflow was added.

Upload the contents of this ZIP to the root of your GitHub repository, commit, then run Actions → Build Android APK.
