package com.cds.promotion.module.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cds.promotion.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/17 10:29
 * @Version: 3.0.0
 */
public class StoreAdapter extends BaseAdapter {
    Context context;
    List mDataList = new ArrayList<>();

    public StoreAdapter(Context context) {
        this.context = context;
    }

    public StoreAdapter(Context context, List mDataList) {
        this.context = context;
        this.mDataList = mDataList;
    }

    public List getDataList() {
        return mDataList;
    }

    public void setDataList(List mDataList) {
        this.mDataList = mDataList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return mDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int index, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_visit, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (mDataList != null && !mDataList.isEmpty()) {
//            VisitBean bean = mDataList.get(index);
//            holder.titleTv.setText(bean.getTitle());
//            holder.contentTv.setText(bean.getContent());
//            holder.timeTv.setText(DateUtils.parseMillisToString(Long.parseLong(bean.getTailtime())));
        }
        return view;
    }

    static class ViewHolder {
        @Bind(R.id.time_tv)
        TextView timeTv;
        @Bind(R.id.describe_img)
        AppCompatImageView describeImg;
        @Bind(R.id.title_tv)
        TextView titleTv;
        @Bind(R.id.content_tv)
        TextView contentTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
