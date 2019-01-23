package com.cds.promotion.module.visit.detail;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cds.promotion.R;
import com.cds.promotion.base.BaseActivity;
import com.cds.promotion.data.entity.VisitingInfo;
import com.cds.promotion.util.DateUtils;
import com.cds.promotion.view.HorizontalListView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/19 13:58
 * @Version: 3.0.0
 */
public class VisitDetailActivity extends BaseActivity implements View.OnClickListener, VisitDetailContract.View, OnMapReadyCallback {
    @Bind(R.id.current_location)
    TextView currentLocationTv;
    @Bind(R.id.shop_name)
    TextView shopNameTv;
    @Bind(R.id.time)
    TextView timeTv;
    @Bind(R.id.img_list_view)
    HorizontalListView imgListView;
    @Bind(R.id.note)
    TextView noteTv;

    VisitDetailContract.Presenter mPresenter;

    ImageAdapter adapter;

    private GoogleMap mMap;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_visit_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        findViewById(R.id.back_img).setVisibility(View.VISIBLE);
        findViewById(R.id.back_button).setVisibility(View.VISIBLE);
        findViewById(R.id.back_button).setOnClickListener(this);
        ((TextView) findViewById(R.id.title)).setText("Visiting Details");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void initData() {
        new VisitDetailPresenter(this);
        if (getIntent() != null && getIntent().getExtras() != null) {
            final String dealer_id = getIntent().getStringExtra("visiting_id");
            mPresenter.getVisitingInfo(dealer_id);
            mLoadingView.showLoading();
            mLoadingView.setRetryListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.getVisitingInfo(dealer_id);
                }
            });
        }
        adapter = new ImageAdapter(this);
        imgListView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    @Override
    public void getVisitingInfoSuccess(VisitingInfo resp) {
        mLoadingView.showContent();
        shopNameTv.setText(resp.getDealer_name());
        noteTv.setText(resp.getVisiting_notes());
        if(resp.getVisiting_img() != null){
            adapter.setDataList(resp.getVisiting_img());
        }
        timeTv.setText(DateUtils.parseMillisToString(resp.getVisiting_date()));

        if(!TextUtils.isEmpty(resp.getDealer_location()) && resp.getDealer_location().split(",").length >0){
            LatLng sydney = new LatLng(Double.valueOf(resp.getDealer_location().split(",")[0]),Double.valueOf(resp.getDealer_location().split(",")[1]));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
            mMap.addMarker(new MarkerOptions()
                    .title(resp.getDealer_name())
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.btn_maplocation))
                    .position(sydney));
        }

    }

    @Override
    public void getVisitingInfoFail() {
        mLoadingView.showError();
    }

    @Override
    public void setPresenter(VisitDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        LatLng sydney = new LatLng(38.0350781,114.43902579999997);
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
//        googleMap.addMarker(new MarkerOptions()
//                .title("Sydney")
//                .snippet("The most populous city in Australia.")
//                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.btn_maplocation))
//                .position(sydney));
    }

    class ImageAdapter extends BaseAdapter {
        List<String> mDataList = new ArrayList<>();
        Context context;
        public ImageAdapter(Context context) {
            this.context = context;
        }

        public ImageAdapter(Context context, List<String> list) {
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
            return mDataList.size();
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_visit_img, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if(mDataList.size() > 0 &&!TextUtils.isEmpty(mDataList.get(position))){
                Picasso.with(context)
                        .load(mDataList.get(position))
                        .into(holder.img);
            }

            return convertView;
        }
    }

    static class ViewHolder {
        @Bind(R.id.img)
        AppCompatImageView img;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
