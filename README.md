# Call Recorder App

A simple Android application to record phone calls.

## Output Location
The recorded audio file can be found at: AndroidStorage/android/data/com.example.callrecorder.com/cache/recorded_audio.3gp


## Installation and Setup

1. **Load the Project**
   - Open Android Studio.
   - Load the project and run it.

2. **Permissions**
   - Ensure all required permissions are granted.

3. **Enable Accessibility Service**
   - Add the application to the accessibility service.

4. **Background Running**
   - Make sure the application is running in the background while making calls.

5. **Debugging**
   - Check Logcat for any errors.
   - If an error occurs, especially regarding setting the audio source, uninstall the application and rerun it.

## Note
- The default audio player on your device might not support the recorded audio format.
- It is recommended to use [VLC for Android](https://play.google.com/store/apps/details?id=org.videolan.vlc) to play the recorded audio.

## Permissions Required

- **READ_PHONE_STATE**: To detect incoming and outgoing calls.
- **RECORD_AUDIO**: To record the audio during calls.
- **WRITE_EXTERNAL_STORAGE**: To save the recorded audio file.

## Troubleshooting

- **Setting Audio Source Error**:
  - If you encounter an error when setting the audio source, try uninstalling the application and rerun it.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contributing
1. Fork the repository.
2. Create a new branch:
   ```sh
   git checkout -b feature/your-feature-name
