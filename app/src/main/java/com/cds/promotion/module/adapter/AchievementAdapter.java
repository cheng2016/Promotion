package com.cds.promotion.module.adapter;

import android.content.Context;
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
 * @CreateDate: 2019/1/17 18:32
 * @Version: 3.0.0
 */
public class AchievementAdapter extends BaseAdapter {
    Context context;
    List mDataList = new ArrayList<>();

    public AchievementAdapter(Context context) {
        this.context = context;
    }

    public AchievementAdapter(Context context, List mDataList) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_achievement, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (mDataList != null && !mDataList.isEmpty()) {
//            ClockOnInfo bean = mDataList.get(index);
//            holder.timeTv.setText(bean.getGo_work_date());
//            holder.checkInTv.setText(bean.getGo_work_active_time());
//            holder.checkInTv.setText(bean.getOff_work_active_time());
        }
        return view;
    }

    static class ViewHolder {
        @Bind(R.id.number_tv)
        TextView numberTv;
        @Bind(R.id.describe_tv)
        TextView describeTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
