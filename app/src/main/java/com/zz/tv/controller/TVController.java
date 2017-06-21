package com.zz.tv.controller;

import android.os.Handler;
import android.view.KeyEvent;

import com.zz.tv.data.ChannelInfo;
import com.zz.tv.data.TVContext;
import com.zz.tv.ui.PFCallback;
import com.zz.tv.ui.TVListViewCallback;
import com.zz.tv.ui.VideoViewVLC;

import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.media.VideoView;

/**
 * Created by zhangxiaohui on 2017/6/2.
 */

public class TVController {

    //private VideoViewVLC videoViewVLC;
    private TVContext tvContext;
    private PFCallback pfCallback;
    private TVListViewCallback tvListViewCallback;
    private Handler uiHandler;
    private static final int SWITCH_INTERVAL = 300;
    private PlayController playController;

    public TVController(VideoViewVLC videoView) {
        tvContext = new TVContext();
        playController = new PlayController(videoView);
        uiHandler = new Handler();
    }

    public void test() {
//        playController.stop();
//        String[] urls = new String[1];
//        urls[0] = "file:///mnt/sdcard/test1.mp4";
//        playController.play(urls);
        playPreChannel();
    }


    public void setPfCallback(PFCallback pfCallback) {
        this.pfCallback = pfCallback;
    }

    public void setTvListViewCallback(TVListViewCallback tvListViewCallback) {
        this.tvListViewCallback = tvListViewCallback;
    }

    public void enterTV() {
//        //TODO play default channel
//        String[] urls = new String[1];
//        urls[0] = "file:///mnt/sdcard/test.mp4";
//        playController.play(urls);
        playChannel(tvContext.getDefaultChannel());
    }

    public void exitTV() {
        playController.stop();
    }

    public void onResume() {
        playController.resume();
    }

    public void onPause() {
        playController.pause();
    }

    private Runnable switchChannelRunnable = new Runnable() {
        @Override
        public void run() {
            playChannel(tvContext.getTargetChannel());
        }
    };

    private void playChannel(ChannelInfo c) {
        if (playController.isPlaying()) {
            playController.stop();
        }
        tvContext.setCurrentChannel(c);
        playController.play(c.getUrls());
    }

    private void playPreChannel() {
        ChannelInfo ch = tvContext.getPreChannel();
        pfCallback.updatePf(ch);
        tvContext.setTargetChannel(ch);
        uiHandler.removeCallbacks(switchChannelRunnable);
        uiHandler.postDelayed(switchChannelRunnable, SWITCH_INTERVAL);
    }

    private void playNextChannel() {
        ChannelInfo ch = tvContext.getNextChannel();
        pfCallback.updatePf(ch);
        tvContext.setTargetChannel(ch);
        uiHandler.removeCallbacks(switchChannelRunnable);
        uiHandler.postDelayed(switchChannelRunnable, SWITCH_INTERVAL);
    }

    public boolean onKeyEvent(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.KEYCODE_DPAD_UP:
                playPreChannel();
                return true;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                playNextChannel();
                return true;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                tvListViewCallback.show(tvContext.getTargetChannel());
                return true;
            }
        return false;
    }
}
