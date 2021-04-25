package com.zz.tv.ui;

import com.zz.tv.data.ChannelInfo;

import java.util.List;

/**
 * Created by zhangxiaohui on 2017/6/2.
 */

public interface UICallback {
    void updatePf(ChannelInfo channelInfo);
    void showLoading();
    void updateChannelList(List<ChannelInfo> list);
    void dismissLoading();
    void showError(String errorInfo);
}
