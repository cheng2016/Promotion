package com.cds.promotion.module.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cds.promotion.R;
import com.cds.promotion.data.entity.ClockOnInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/17 11:31
 * @Version: 3.0.0
 */
public class AttendanceAdapter extends BaseAdapter {
    Context context;
    List<ClockOnInfo> mDataList = new ArrayList<>();

    public AttendanceAdapter(Context context) {
        this.context = context;
    }

    public AttendanceAdapter(Context context, List<ClockOnInfo> mDataList) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_attendance, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (mDataList != null && !mDataList.isEmpty()) {
            ClockOnInfo bean = mDataList.get(index);
            holder.timeTv.setText(bean.getGo_work_date());

            holder.checkInTv.setText(Html.fromHtml("<font color='#999999'>Attendance: &nbsp&nbsp</font>"
                    + bean.getGo_work_active_time() +  "<font color='#FF4A56'>" + bean.getLate() + "&nbsp&nbsp</font>"));
            holder.checkOutTv.setText(Html.fromHtml("&nbsp&nbsp<font color='#999999'>Off Attendance: &nbsp&nbsp</font>"
                    + bean.getOff_work_active_time() +  "&nbsp&nbsp<font color='#FF4A56'>" + bean.getLeave_early() + "&nbsp&nbsp</font>"));
        }
        return view;
    }

    static class ViewHolder {
        @Bind(R.id.time_tv)
        TextView timeTv;
        @Bind(R.id.check_in_tv)
        TextView checkInTv;
        @Bind(R.id.check_out_tv)
        TextView checkOutTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}