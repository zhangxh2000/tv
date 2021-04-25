package com.zz.tv.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zz.tv.R;
import com.zz.tv.controller.TVController;
import com.zz.tv.data.ChannelInfo;

import java.util.List;

/**
 * Created by zhangxiaohui on 2017/6/2.
 */

public class PlayActivity extends Activity implements UICallback {
    private PFFragment pfFragment;
    private VideoViewVLC videoView;
    private TVController tvController;
    private ChannelListFragment channelListFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        //keep screen on
        getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        videoView = (VideoViewVLC) findViewById(R.id.videoview);
        tvController = new TVController(videoView);
        tvController.setUiCallback(this);
        channelListFragment = (ChannelListFragment) getFragmentManager().findFragmentById(R.id.ch_list_fragment);

        Button button = (Button) findViewById(R.id.test);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) PlayActivity.this.findViewById(R.id.edit_text_url);
                String url = editText.getText().toString();
                if (TextUtils.isEmpty(url)) {
                    Toast.makeText(PlayActivity.this," url为空",Toast.LENGTH_SHORT).show();
                } else {
                    tvController.test(url);
                }
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

    @Override
    public void showLoading() {

    }

    @Override
    public void updateChannelList(List<ChannelInfo> list) {
        channelListFragment.setChannelList(list);
    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void showError(String errorInfo) {

    }
}
