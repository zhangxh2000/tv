package com.zz.tv.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxiaohui on 2017/6/2.
 */

public class TVContext {
    private Map<Integer,ChannelInfo> channelInfoMap = new HashMap<Integer, ChannelInfo>();
    private List<ChannelInfo> channelInfoList;
    private ChannelInfo currentChannel;
    private ChannelInfo targetChannel;

    public TVContext() {
        //TODO 模拟频道
        channelInfoList = new ArrayList<>();
        for (int i=0;i<10;i++) {
            ChannelInfo c = new ChannelInfo();
            c.setIndex(i);
            c.setName("频道" + i);
            String[] temp = new String[1];
            temp[0] = "file:///mnt/sdcard/test.mp4";
            if (i == 0) {
                //temp[0] = "http://gs.moli01.lunbocl.ott.cibntv.net/hls/vCIBNHYPD/1800/vCIBNHYPD.m3u8?k=939D28D143F04FD1A0BA820F3CAEDCD8&channel=xlmediawatch&t=1498043311&ttl=86400&clms_sessid=hlsvCIBNHYPD1800vCIBNHYPD724fd62f1749aa95286acb2695c90cbc";
            }
            c.setUrls(temp);
            channelInfoList.add(c);
            channelInfoMap.put(i,c);
        }
    }

    public ChannelInfo getDefaultChannel() {
        return channelInfoList.get(0);
    }

    public ChannelInfo getChannel(int number) {
        return channelInfoMap.get(number);
    }

    public List<ChannelInfo> getChannelList() {
        return channelInfoList;
    }

    public void setCurrentChannel(ChannelInfo c) {
        currentChannel = c;
    }

    public void setChannelList(List<ChannelInfo> list) {
        channelInfoList = list;
    }

    public ChannelInfo getNextChannel() {
        if (currentChannel == null) {
            return null;
        }
        int index = currentChannel.getIndex();
        index++;
        if (index>= channelInfoMap.size()) {
            index = 0;
        }
        return channelInfoMap.get(index);
    }

    public ChannelInfo getPreChannel() {
        if (currentChannel == null) {
            return null;
        }
        int index = currentChannel.getIndex();
        index--;
        if (index<0) {
            index = channelInfoMap.size()-1;
        }
        return channelInfoMap.get(index);
    }

    public ChannelInfo getTargetChannel() {
        return targetChannel;
    }

    public void setTargetChannel(ChannelInfo targetChannel) {
        this.targetChannel = targetChannel;
    }
}
