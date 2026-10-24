# CamLink Live — Native Android floating camera overlay

This project wraps the deployed CamLink Live WebRTC viewer in a native Android `WindowManager` overlay so the remote camera preview can remain above games and other apps when Android's overlay permission is granted.

## Features in source

- Room-code generation, manual entry, and copy-to-clipboard.
- System **Display over other apps** permission flow.
- Foreground service with a persistent notification.
- Movable overlay window with resize controls, sound toggle, and close control.
- WebView auto-joins the deployed CamLink Live room using `?overlay=1&room=ROOMCODE`.
- Service cleanup removes the window and destroys the WebView.

## Pairing

1. On the main phone, open the native app and note/copy its room code.
2. Start the floating camera and grant **Display over other apps** permission.
3. On the second phone, open https://camlink-live-2lbgoe.v2.appdeploy.ai/ in Chrome.
4. Select **Second phone**, enter the same room code, and allow camera/microphone access.
5. Wait for the live feed, then switch to your game. Drag the header to move the overlay and use −/+ to resize it.

## Build

See [BUILD-APK.md](BUILD-APK.md) for the GitHub Actions cloud build and Android Studio instructions. This source package is not a compiled APK; no Android SDK or Gradle executable is available in the current build environment.
