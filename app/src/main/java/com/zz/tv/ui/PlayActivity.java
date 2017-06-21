package com.zz.tv.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.zz.tv.R;
import com.zz.tv.controller.TVController;
import com.zz.tv.data.ChannelInfo;

/**
 * Created by zhangxiaohui on 2017/6/2.
 */

public class PlayActivity extends Activity implements PFCallback {
    private PFFragment pfFragment;
    private VideoViewVLC videoView;
    private TVController tvController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        videoView = (VideoViewVLC) findViewById(R.id.videoview);
        tvController = new TVController(videoView);
        tvController.setPfCallback(this);
        Button button = (Button) findViewById(R.id.test);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvController.test();
            }
        });

        // However, if we're being restored from a previous state,
        // then we don't need to do anything and should return or else
        // we could end up with overlapping fragments.
        if (savedInstanceState != null) {
            return;
        }

        // Create a new Fragment to be placed in the activity layout
        pfFragment = new PFFragment();

        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        pfFragment.setArguments(getIntent().getExtras());

        // Add the fragment to the 'fragment_container' FrameLayout
        getFragmentManager().beginTransaction()
                .add(R.id.pf_container, pfFragment).commit();
    }


    @Override
    protected void onStart() {
        super.onStart();
        tvController.enterTV();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        tvController.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        tvController.exitTV();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean dealed = tvController.onKeyEvent(event);
        if (!dealed) {
            return super.onKeyDown(keyCode,event);
        } else {
            return true;
        }
    }

    @Override
    public void updatePf(ChannelInfo channelInfo) {
        pfFragment.setChannelName(channelInfo.getName());
        pfFragment.setChannelNumber(channelInfo.getNumber());
        pfFragment.show();
    }
}
