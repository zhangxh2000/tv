package com.zz.tv.controller;

import android.util.Log;

import com.zz.tv.ui.VideoViewVLC;

import org.videolan.libvlc.MediaPlayer;

/**
 * Created by zhangxiaohui on 2017/6/6.
 */

public class PlayController {
    private VideoViewVLC videoViewVLC;
    private String[] urls;

    public PlayController(VideoViewVLC videoViewVLC) {
        this.videoViewVLC = videoViewVLC;
        videoViewVLC.setEventListener(playerEventListener);
    }

    private VideoViewVLC.EventListener playerEventListener = new VideoViewVLC.EventListener() {
        @Override
        public void onError(MediaPlayer mediaPlayer, int what, int extra) {

        }

        @Override
        public void onCompleted(MediaPlayer mediaPlayer) {

        }

        @Override
        public void onInfo(MediaPlayer mediaPlayer, int what, int extra) {

        }

        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {

        }
    };

    public void play(String[] urls) {
        this.urls = urls;
        videoViewVLC.setVideoPath(urls[0]);
        videoViewVLC.start();
    }

    public boolean isPlaying() {
        return videoViewVLC.isPlaying();
    }

    public void resume() {
        videoViewVLC.resume();
    }

    public void stop() {
        videoViewVLC.stopPlayback();
    }

    public void pause() {
        videoViewVLC.pause();
    }
}
