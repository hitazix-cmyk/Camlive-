# Build CamLink Live APK

This is a native Android source project. **It is not itself an APK.** This workspace does not have the Android SDK/Build Tools or Gradle installed, and network access is unavailable here, so I cannot compile or device-test the APK in this environment.

## Easiest build: GitHub Actions (no Android Studio required)

1. Sign in to GitHub and create a new repository (a private repository is fine).
2. Extract the ZIP on your device/computer.
3. Upload both top-level items from the ZIP into the repository: `CamLinkLive-Android/` and `.github/`. Keep their folder structure unchanged.
4. Commit the files to the `main` branch.
5. Open the repository's **Actions** tab and select **Build CamLink Live APK**. If it did not start automatically, tap **Run workflow**.
6. Wait for the run to finish successfully, open that run, and download the `CamLinkLive-debug-apk` artifact.
7. Extract the downloaded artifact ZIP to get `app-debug.apk`, then install it on your Android phone. You may need to allow installation from that file manager/browser.

The workflow installs Android SDK Platform 35 and Build Tools, compiles the debug APK, verifies that the file exists, and uploads it as an artifact. This workflow has been prepared but **has not been run from this environment**, so a successful cloud build is not yet verified.

## Build locally in Android Studio

1. Open the `CamLinkLive-Android` folder in Android Studio.
2. Use JDK 17 and install Android SDK Platform 35 and Build Tools 35.0.0 when prompted.
3. Ensure Gradle 8.9 is selected; run `gradle assembleDebug` from this project folder, or use Android Studio's Build APK action.
4. The expected output is `app/build/outputs/apk/debug/app-debug.apk`.

## Use the app

1. Install and open CamLink Live on the main phone.
2. Generate or enter a room code.
3. Tap **Start floating live camera**. Allow **Display over other apps** if Android asks, return to the app, and tap Start again.
4. On the second phone, open https://camlink-live-2lbgoe.v2.appdeploy.ai/ in Chrome, select **Second phone**, enter the same room code, and grant camera/microphone permission.
5. When the feed connects, switch to the game. Drag the floating header to move the window; use −/+ to resize; use Sound on/off to control audio.

## Important limitations

- The native Android source has not been compiled or installed on a physical device in this environment. Do not treat the APK as tested until it builds and is checked on your phone.
- The native overlay uses a WebView to connect to the deployed CamLink Live WebRTC service. Both phones need internet access; latency and video quality depend on network conditions.
- Android battery optimization and manufacturer-specific background limits may stop long sessions. The foreground notification helps but cannot guarantee persistence on every phone.
- This workflow produces a **debug APK**, not a signed Play Store release.
