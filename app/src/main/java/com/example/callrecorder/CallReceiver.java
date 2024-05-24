package com.example.callrecorder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.telephony.TelephonyManager;
import android.media.MediaRecorder;
import android.media.MediaPlayer;
import java.io.IOException;

public class CallReceiver extends BroadcastReceiver {
    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static boolean isIncommingCall;
    private static final int SAMPLE_RATE = 44100;
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_DEFAULT;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private static MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
            System.out.println("Outgoing call ");
        } else {
            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

            int state = 0;
            if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                state = TelephonyManager.CALL_STATE_IDLE;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                state = TelephonyManager.CALL_STATE_OFFHOOK;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                state = TelephonyManager.CALL_STATE_RINGING;
            }

            onCallEvents(context, state, number);
        }
    }

    public void onCallEvents(Context ctx, int state, String number) {
        if (state == lastState) {
            return;
        } else {
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    System.out.println("Incoming call: Ringing....");
                    isIncommingCall = true;
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                        isIncommingCall = false;
                        System.out.println("Outgoing call started");
                    } else {
                        isIncommingCall = true;
                        System.out.println("Incoming call started");
                    }
                    startRecording(ctx);
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                        System.out.println("Missed call");
                    } else if (isIncommingCall) {
                        System.out.println("Incoming call Finished");
                        stopRecording(ctx);
                    } else {
                        System.out.println("Outgoing call ended");
                        stopRecording(ctx);
                    }
                    break;
            }
            lastState = state;
        }
    }

    private synchronized void startRecording(Context ctx) {
        System.out.println("Starting recording");
        if (mediaRecorder == null) {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(ctx.getExternalCacheDir().getAbsolutePath() + "/recorded_audio.3gp");
            System.out.println(ctx.getExternalCacheDir().getAbsolutePath() + "/recorded_audio.3gp");
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
                System.out.println("Recording started successfully");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error preparing or starting MediaRecorder");
            }
        } else {
            System.out.println("MediaRecorder is already initialized and recording");
        }
    }

    private synchronized void stopRecording(Context ctx) {
        System.out.println("Stopping Recording");
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
                System.out.println("Successfully stopped recording");
            } catch (RuntimeException e) {
                e.printStackTrace();
                System.out.println("RuntimeException when stopping MediaRecorder");
            } finally {
                mediaRecorder.release();
                mediaRecorder = null;
                System.out.println("MediaRecorder has been released");
            }
        } else {
            System.out.println("MediaRecorder is null when stopping recorder");
        }
    }

    private void playAudio(Context ctx) {
        System.out.println("Playing audio");
        mediaPlayer = new MediaPlayer();
        try {
            System.out.println(ctx.getExternalCacheDir().getAbsolutePath() + "/recorded_audio.3gp");
            mediaPlayer.setDataSource(ctx.getExternalCacheDir().getAbsolutePath() + "/recorded_audio.3gp");
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
