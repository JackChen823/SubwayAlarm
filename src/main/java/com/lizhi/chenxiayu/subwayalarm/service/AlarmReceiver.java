package com.lizhi.chenxiayu.subwayalarm.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;

/**
 * Created by chenxiayu on 2017/6/17.
 */

public class AlarmReceiver extends BroadcastReceiver {
    private MediaPlayer mMediaPlayer;
    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int minute = calendar.get(Calendar.MINUTE);
        CharSequence text = String.valueOf(minute);
        //Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        this.context = context;
        Bundle bundle = intent.getExtras();
        Intent serviceIntent = new Intent();
        serviceIntent.setAction("chief_musicService");
        serviceIntent.setPackage(context.getPackageName());
        serviceIntent.putExtras(bundle);
        Log.i("chenxiayu", "bundle---"+bundle);
        if(bundle != null) {
            Log.i("chenxiayu", String.valueOf(bundle.getBoolean("music")));
            if(bundle.getBoolean("music"))
                context.startService(serviceIntent);
            else
                context.stopService(serviceIntent);
        }
        //在这里是播放不了的！！
        //playAlarmRing();
    }
    private void playAlarmRing() {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        try {
            mMediaPlayer = new MediaPlayer();
            try {
                mMediaPlayer.setDataSource(context, uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            final AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mMediaPlayer.setLooping(true);
                mMediaPlayer.prepare();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.start();
    }
    private void StopAlarmRing() {
        mMediaPlayer.stop();
    }
}
