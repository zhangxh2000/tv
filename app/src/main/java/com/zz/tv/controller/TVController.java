package com.zz.tv.controller;

import android.os.Handler;
import android.view.KeyEvent;
import com.zz.tv.data.ChannelInfo;
import com.zz.tv.data.TVContext;
import com.zz.tv.ui.UICallback;
import com.zz.tv.ui.TVListViewCallback;
import com.zz.tv.ui.VideoViewVLC;

import java.util.List;

/**
 * Created by zhangxiaohui on 2017/6/2.
 */

public class TVController {

    //private VideoViewVLC videoViewVLC;
    private TVContext tvContext;
    private UICallback uiCallback;
    private TVListViewCallback tvListViewCallback;
    private Handler uiHandler;
    private static final int SWITCH_INTERVAL = 300;
    private PlayController playController;
    private DataLoader dataLoader;
    public TVController(VideoViewVLC videoView) {
        tvContext = new TVContext();
        playController = new PlayController(videoView);
        uiHandler = new Handler();
        initData();
    }


    private void initData() {

        DataLoader dataLoader = new DataLoader();
        dataLoader.loadChannelList(new DataLoader.LoadListener() {
            @Override
            public void onSuccess(List<ChannelInfo> list) {
                uiCallback.dismissLoading();
                uiCallback.updateChannelList(list);
            }

            @Override
            public void onFailed() {
                uiCallback.showError("获取频道列表失败！");
            }
        });
    }

    public void test(String url) {
        //playChannel(tvContext.getDefaultChannel());
        String[] temp = new String[1];
        temp[0] = url;
        playController.play(temp);
    }


    public void setUiCallback(UICallback uiCallback) {
        this.uiCallback = uiCallback;
    }

    public void setTvListViewCallback(TVListViewCallback tvListViewCallback) {
        this.tvListViewCallback = tvListViewCallback;
    }

    public void enterTV() {
        //playChannel(tvContext.getDefaultChannel());
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
        uiCallback.updatePf(ch);
        tvContext.setTargetChannel(ch);
        uiHandler.removeCallbacks(switchChannelRunnable);
        uiHandler.postDelayed(switchChannelRunnable, SWITCH_INTERVAL);
    }

    private void playNextChannel() {
        ChannelInfo ch = tvContext.getNextChannel();
        uiCallback.updatePf(ch);
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
