package com.cds.promotion.module.attendance;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cds.promotion.App;
import com.cds.promotion.R;
import com.cds.promotion.base.BaseActivity;
import com.cds.promotion.data.Constant;
import com.cds.promotion.data.entity.ClockOnInfo;
import com.cds.promotion.module.attendance.record.AttendanceRecordActivity;
import com.cds.promotion.util.PermissionHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/16 14:15
 * @Version: 3.0.0
 * 打卡界面
 */
public class AttendanceActivity extends BaseActivity implements View.OnClickListener, AttendanceContract.View {
    @Bind(R.id.check_in_time)
    TextView checkInTime;
    @Bind(R.id.check_in_status)
    TextView checkInStatus;
    @Bind(R.id.check_in_btn)
    LinearLayout checkInBtn;
    @Bind(R.id.check_out_time)
    TextView checkOutTime;
    @Bind(R.id.check_out_status)
    TextView checkOutStatus;
    @Bind(R.id.check_out_btn)
    LinearLayout checkOutBtn;
    @Bind(R.id.record_btn)
    TextView recordBtn;
    @Bind(R.id.go_work_icon)
    AppCompatImageView goWorkIcon;
    @Bind(R.id.off_work_icon)
    AppCompatImageView offWorkIcon;
    @Bind(R.id.go_work_title)
    TextView goWorkTitle;
    @Bind(R.id.go_work_layout)
    LinearLayout goWorkLayout;
    @Bind(R.id.off_work_title)
    TextView offWorkTitle;
    @Bind(R.id.off_work_layout)
    LinearLayout offWorkLayout;

    AttendanceContract.Presenter mPresenter;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private Location mLastLocation;

    private PermissionHelper mHelper;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_attendance;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        findViewById(R.id.back_img).setVisibility(View.VISIBLE);
        findViewById(R.id.back_button).setVisibility(View.VISIBLE);
        findViewById(R.id.back_button).setOnClickListener(this);
        findViewById(R.id.record_btn).setOnClickListener(this);
        ((TextView) findViewById(R.id.title)).setText("Attendance");
    }

    @Override
    protected void initData() {
        new AttendancePresenter(this);
        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mHelper = new PermissionHelper(this);
        getLocation();

        mPresenter.getClockOn();
        mLoadingView.showLoading();
        mLoadingView.setRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getClockOn();
            }
        });
    }

    private void getLocation() {
        mHelper.requestPermissions(getResources().getString(R.string.permission_rationale_location),
                new PermissionHelper.PermissionListener() {
                    @Override
                    public void doAfterGrand(String... permission) {
                        getLastLocation();
                    }

                    @Override
                    public void doAfterDenied(String... permission) {
                        Toast.makeText(App.getInstance(), getResources().getString(R.string.location_permission_denied), Toast.LENGTH_SHORT).show();
                    }
                }, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void getLastLocation() {
        Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
        locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    // Set the map's camera position to the current location of the device.
                    mLastLocation = task.getResult();
                } else {
                    Log.d(TAG, "Current location is null. Using defaults.");
                    Log.e(TAG, "Exception: %s", task.getException());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.record_btn:
                Intent intent = new Intent();
                intent.setClass(AttendanceActivity.this, AttendanceRecordActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    @Override
    public void setPresenter(AttendanceContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @OnClick({R.id.check_in_btn, R.id.check_out_btn})
    public void onViewClicked(View view) {
        String location;
        switch (view.getId()) {
            case R.id.check_in_btn:
                showProgressDilog();
                if (mLastLocation == null) {
                    getLocation();
                } else {
                    location = mLastLocation.getLatitude() + "," + mLastLocation.getLongitude();
                    mPresenter.clockOn(Constant.ON_WORK, location);
                }
                break;
            case R.id.check_out_btn:
                showProgressDilog();
                if (mLastLocation == null) {
                    getLocation();
                } else {
                    location = mLastLocation.getLatitude() + "," + mLastLocation.getLongitude();
                    mPresenter.clockOn(Constant.OFF_WORK, location);
                }
                break;
        }
    }

    @Override
    public void getClockOnSuccess(ClockOnInfo resp) {
        mLoadingView.showContent();
        goWorkTitle.setText("Working hours " + resp.getGo_work_time().substring(10));
        offWorkTitle.setText("Off hours " + resp.getOff_work_time().substring(10));

        DateTime current = new DateTime(System.currentTimeMillis());
//        DateTime goWork = DateTime.parse(resp.getGo_work_time(), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm"));
        DateTime offwork = DateTime.parse(resp.getOff_work_time(), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm"));

        if (TextUtils.isEmpty(resp.getGo_work_active_time())) {
            goWorkIcon.setImageResource(R.mipmap.attendancedot_current);
            offWorkIcon.setImageResource(R.mipmap.attendancedot);
            checkInBtn.setVisibility(View.VISIBLE);
            goWorkLayout.setVisibility(View.GONE);
        } else {
            goWorkIcon.setImageResource(R.mipmap.attendancedot);
            offWorkIcon.setImageResource(R.mipmap.attendancedot_current);

            checkInBtn.setVisibility(View.GONE);
            goWorkLayout.setVisibility(View.VISIBLE);
            checkInTime.setText("Clock in time " + resp.getGo_work_active_time().substring(10));

            DateTime start = DateTime.parse(resp.getGo_work_active_time(), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm"));
            DateTime end = DateTime.parse(resp.getGo_work_time(), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm"));
            if (start.isAfter(end)) {
                checkInStatus.setVisibility(View.VISIBLE);
            } else {
                checkInStatus.setVisibility(View.GONE);
            }
        }

        if (TextUtils.isEmpty(resp.getOff_work_active_time())) {
            if (current.isAfter(offwork)) {
                checkOutBtn.setVisibility(View.VISIBLE);
                offWorkLayout.setVisibility(View.GONE);
            } else {
                checkOutBtn.setVisibility(View.GONE);
                offWorkLayout.setVisibility(View.GONE);
            }
        } else {
            checkOutBtn.setVisibility(View.GONE);
            offWorkLayout.setVisibility(View.VISIBLE);
            checkOutTime.setText("Clock out time " + resp.getOff_work_active_time().substring(10));

            DateTime start = DateTime.parse(resp.getOff_work_active_time(), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm"));
            DateTime end = DateTime.parse(resp.getOff_work_time(), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm"));
            if (start.isAfter(end)) {
                checkOutStatus.setVisibility(View.GONE);
            } else {
                checkOutStatus.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void getClockOnFail() {
        mLoadingView.showError();
    }

    @Override
    public void clockOnSuccess() {
        hideProgressDilog();
        mPresenter.getClockOn();
    }

    @Override
    public void clockOnFailed() {
        hideProgressDilog();
    }
}
