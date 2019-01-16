package com.cds.promotion.module.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cds.promotion.R;
import com.cds.promotion.data.entity.VisitBean;
import com.cds.promotion.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/16 14:04
 * @Version: 3.0.0
 */
public class VisitAdapter extends BaseAdapter {
    Context context;
    List<VisitBean> mDataList = new ArrayList<>();

    public VisitAdapter(Context context) {
        this.context = context;
    }

    public VisitAdapter(Context context, List<VisitBean> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
    }

    public List<VisitBean> getDataList() {
        return mDataList;
    }

    public void setDataList(List<VisitBean> mDataList) {
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
            VisitBean bean = mDataList.get(index);
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