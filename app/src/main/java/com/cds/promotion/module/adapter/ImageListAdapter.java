package com.cds.promotion.module.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cds.promotion.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/17 14:57
 * @Version: 3.0.0
 */
public class ImageListAdapter extends BaseAdapter {
    List<String> mDataList = new ArrayList<>();
    Context context;

    public ImageListAdapter(Context context) {
        this.context = context;
    }

    public ImageListAdapter(Context context, List<String> list) {
        this.mDataList = list;
        this.context = context;
    }

    public void setDataList(List<String> listItems) {
        this.mDataList = listItems;
        this.notifyDataSetChanged();
    }

    public List getDataList() {
        return mDataList;
    }

    @Override
    public int getCount() {
//        return 2;
        if (mDataList.size() == 3) {
            return mDataList.size();
        }
        return mDataList.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_img, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == mDataList.size() && position < 3) { //在最后位置增加一个加号图片
            holder.addImg.setVisibility(View.VISIBLE);
            holder.img.setVisibility(View.GONE);
            holder.delImg.setVisibility(View.GONE);
            holder.addImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Light light = new Light();
//                    mDataList.add(light);
//                    setDataList(mDataList);
                    if (listener != null) {
                        listener.onAddClick();
                    }
                }
            });
            return convertView;
        }
        holder.addImg.setVisibility(View.GONE);
        holder.img.setVisibility(View.VISIBLE);
        holder.delImg.setVisibility(View.VISIBLE);

        if(mDataList.size() > 0 &&!TextUtils.isEmpty(mDataList.get(position))){
            Picasso.with(context)
                    .load("file://" + mDataList.get(position))
                    .into(holder.img);
        }

        holder.delImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mDataList.remove(position);
//                setDataList(mDataList);

                if (listener != null) {
                    listener.onDeleteClick(position);
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.add_img)
        RelativeLayout addImg;
        @Bind(R.id.img)
        AppCompatImageView img;
        @Bind(R.id.del_img)
        AppCompatImageView delImg;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    OnImageClickListener listener;

    public void setListener(OnImageClickListener listener) {
        this.listener = listener;
    }

    public interface OnImageClickListener {
        void onAddClick();

        void onDeleteClick(int index);
    }
}