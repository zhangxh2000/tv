package com.zz.tv.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zz.tv.R;
import com.zz.tv.data.ChannelInfo;

/**
 * Created by zhangxiaohui on 2017/6/23.
 */

public class ChannelListItem extends RelativeLayout {
    private TextView name;

    public ChannelListItem(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.channel_list_item,this);
        name = (TextView) findViewById(R.id.channel);
    }

    public void setChannel(ChannelInfo channel) {
        name.setText(channel.getNumber()+ " " + channel.getName());
    }
}
