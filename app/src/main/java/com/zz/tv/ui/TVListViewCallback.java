package com.zz.tv.ui;

import com.zz.tv.data.ChannelInfo;

/**
 * Created by zhangxiaohui on 2017/6/20.
 */

public interface TVListViewCallback {
    /**
     * 显示频道列表
     * @param currentChannel 当前正在播放频道
     */
    void show(ChannelInfo currentChannel);
    void dismiss();
}
