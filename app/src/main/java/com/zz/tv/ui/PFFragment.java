package com.zz.tv.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zz.tv.R;

/**
 * Created by zhangxiaohui on 2017/6/2.
 */

public class PFFragment extends Fragment {
    private TextView channelNumber;
    private TextView channelName;
    private Handler handler;
    View pfView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        pfView = inflater.inflate(R.layout.pf_layout,null);
        channelNumber = (TextView) pfView.findViewById(R.id.channel_num);
        channelName = (TextView) pfView.findViewById(R.id.channel_name);
        handler = new Handler();
        dismiss();
        return pfView;
    }

    public void setChannelNumber(int num) {
        String number = "";
        try {
            number = Integer.toString(num);
        } catch (Exception e) {
            e.printStackTrace();
        }
        channelNumber.setText(number);
    }

    public void setChannelName(String name) {
        channelName.setText(name);
    }

    public void show() {
        pfView.setVisibility(View.VISIBLE);
        handler.removeCallbacks(hideRunnable);
        handler.postDelayed(hideRunnable,3000);
    }

    private Runnable hideRunnable = new Runnable() {
        @Override
        public void run() {
            dismiss();
        }
    };

    public void dismiss() {
        handler.removeCallbacks(hideRunnable);
        pfView.setVisibility(View.GONE);
    }
}
