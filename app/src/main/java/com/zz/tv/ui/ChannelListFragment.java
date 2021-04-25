package com.zz.tv.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zz.tv.R;
import com.zz.tv.data.ChannelInfo;
import java.util.List;

/**
 * Created by zhangxiaohui on 2017/6/2.
 */

public class ChannelListFragment extends Fragment {
    private TextView myChannelTextView;
    private RecyclerView channelListView;
    private List<ChannelInfo> dataList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.channel_list_fragment,container,false);
        channelListView = (RecyclerView) view.findViewById(R.id.recycler_list);
        channelListView.setAdapter(adapter);
        return view;
        //return super.onCreateView(inflater,container,savedInstanceState);
    }

    private class ViewHoler extends RecyclerView.ViewHolder {
        public ChannelListItem itemView;
        public ViewHoler(ChannelListItem itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }

    private RecyclerView.Adapter adapter = new RecyclerView.Adapter() {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHoler(new ChannelListItem(getActivity()));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHoler viewHoler = (ViewHoler) holder;
            viewHoler.itemView.setChannel(dataList.get(position));
        }

        @Override
        public int getItemCount() {
            return dataList==null?0:dataList.size();
        }
    };

    public void setChannelList(List<ChannelInfo> list) {
        dataList = list;
    }
}
