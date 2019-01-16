package com.cds.promotion.module.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cds.promotion.R;
import com.cds.promotion.data.entity.MenuBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/11 17:56
 * @Version: 3.0.0
 */
public class MenuAdapter extends BaseAdapter {
    private Context context;
    private List<MenuBean> mDataList = new ArrayList<>();

    public MenuAdapter(Context context) {
        this.context = context;
    }

    public List getDataList() {
        return mDataList;
    }

    public void setDataList(List<MenuBean> mDataList) {
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
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null || view.getTag() == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_menu, null, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if(mDataList != null && mDataList.size() > 0){
            MenuBean bean = mDataList.get(position);
            holder.img.setImageResource(bean.getIcon());
            holder.describe.setText(bean.getName());
        }
        return view;
    }

    static class ViewHolder {
        @Bind(R.id.img)
        ImageView img;
        @Bind(R.id.describe)
        TextView describe;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
