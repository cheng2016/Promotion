package com.cds.promotion.module.visit;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cds.promotion.App;
import com.cds.promotion.R;
import com.cds.promotion.base.BaseActivity;
import com.cds.promotion.module.adapter.ImageListAdapter;
import com.cds.promotion.module.store.StoreActivity;
import com.cds.promotion.module.visit.record.VisitRecordActivity;
import com.cds.promotion.util.PermissionHelper;
import com.cds.promotion.util.ToastUtils;
import com.cds.promotion.view.ActionSheetDialog;
import com.cds.promotion.view.HorizontalListView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import butterknife.Bind;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/16 13:56
 * @Version: 3.0.0
 */
public class VisitActivity extends BaseActivity
        implements
        VisitContract.View,
        View.OnClickListener,
        ImageListAdapter.OnImageClickListener,
        OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener {
    // 拍照回传码
    public final static int PHOTO_REQUEST_CAMERA = 0X01;
    // 相册选择回传吗
    public final static int PHOTO_REQUEST_GALLERY = 0X02;
    //裁剪程序请求吗
    public static final int PHOTO_REQUEST_CROP_PHOTO = 0X03;

    public static final int ADD_SHOP_PHOTO = 0X04;

    private static final int MESSAGE_MAX_LENGTH = 300;

    private static final int IMAGE_MAX_LENGTH = 3;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private static final int DEFAULT_ZOOM = 15;

    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);

    @Bind(R.id.right_img)
    ImageView rightImg;

    @Bind(R.id.note_edit)
    EditText noteEdit;
    @Bind(R.id.edit_status)
    TextView editStatus;

    @Bind(R.id.image_status)
    TextView imageStatus;
    @Bind(R.id.img_list_view)
    HorizontalListView imgListView;

    @Bind(R.id.current_location)
    TextView currentLocation;
    @Bind(R.id.shop_name_edt)
    AppCompatEditText shopNameEdt;

    private ImageListAdapter adapter;

    VisitContract.Presenter mPresenter;

    private GoogleMap mMap;

    private PermissionHelper mHelper;

    private CameraPosition mCameraPosition;

    private String dealer_id;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_visit;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mLastLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        findViewById(R.id.back_img).setVisibility(View.VISIBLE);
        findViewById(R.id.back_button).setVisibility(View.VISIBLE);
        findViewById(R.id.back_button).setOnClickListener(this);
        ((TextView) findViewById(R.id.title)).setText("Visiting");
        rightImg.setVisibility(View.VISIBLE);
        rightImg.setImageResource(R.mipmap.btn_visitlog);
        findViewById(R.id.right_button).setOnClickListener(this);
        findViewById(R.id.add_shop_btn).setOnClickListener(this);
        findViewById(R.id.submit_btn).setOnClickListener(this);

        noteEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String content = noteEdit.getText().toString();
                editStatus.setText("Visiting Notes(" + content.length() + "/"
                        + MESSAGE_MAX_LENGTH + ")");
            }
        });
        initGoogle();
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastLocation);
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void initData() {
        new VisitPresenter(this);
        adapter = new ImageListAdapter(this);
        adapter.setListener(this);
        imgListView.setAdapter(adapter);
        mHelper = new PermissionHelper(this);
    }

    private void initGoogle() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please enable GPS!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(intent);
        }

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);
        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);
        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.right_button:
                intent.setClass(VisitActivity.this, VisitRecordActivity.class);
                startActivity(intent);
                break;
            case R.id.add_shop_btn:
                intent.setClass(VisitActivity.this, StoreActivity.class);
                intent.putExtra("title", "Choose Store");
                startActivityForResult(intent, ADD_SHOP_PHOTO);
                break;
            case R.id.submit_btn:
                String note = noteEdit.getText().toString();
                if (TextUtils.isEmpty(note)) {
                    ToastUtils.showShort(App.getInstance(), "Please fill in the visiting note first");
                } else if (note.length() < 20) {
                    ToastUtils.showShort(App.getInstance(), "Please fill in a description of no less than 20 words");
                } else if (TextUtils.isEmpty(dealer_id)) {
                    ToastUtils.showShort(App.getInstance(), "Please selecton Visiting Store first");
                } else if (mLastLocation == null) {
                    ToastUtils.showShort(App.getInstance(), "Please choose current location first");
                } else if (adapter.getDataList() == null || adapter.getDataList().size() == 0) {
                    ToastUtils.showShort(App.getInstance(), "Please choose upload image first");
                } else {
                    showProgressDilog();
                    String location = mLastLocation.getLatitude() + "," + mLastLocation.getLongitude();
                    mPresenter.saveVisiting(dealer_id, note, location, adapter.getDataList());
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    @Override
    public void setPresenter(VisitContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private String mPhotoPath;
    private File mPhotoFile;
    private File mAvatarFile;
    private Uri imageUri;

    /**
     * 调用手机相机
     */
    private void openCamera() {
        mPhotoPath = App.getInstance().getAppCacheDir() + UUID.randomUUID().toString() + ".jpg";
        mPhotoFile = new File(mPhotoPath);
        Intent intentCamera = new Intent();
        getFileUri(mPhotoFile);
        intentCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intentCamera, PHOTO_REQUEST_CAMERA);
    }

    /**
     * 调用手机相册
     */
    private void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //调用相册，返回结果
        if (requestCode == PHOTO_REQUEST_GALLERY && resultCode == Activity.RESULT_OK) {
            Cursor cursor = getContentResolver().query(data.getData(), new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            //游标移到第一位，即从第一位开始读取
            if (cursor != null) {
                cursor.moveToFirst();
                mPhotoPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                cursor.close();
                //调用系统裁剪
                mPhotoFile = new File(mPhotoPath);
                getFileUri(mPhotoFile);
                startPhoneZoom(imageUri);
            }
        }

        //调用相机,返回结果
        if (requestCode == PHOTO_REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            startPhoneZoom(imageUri);
        }
        //调用系统裁剪，返回结果
        if (requestCode == PHOTO_REQUEST_CROP_PHOTO && resultCode == Activity.RESULT_OK) {
            List<String> stringList = adapter.getDataList();
            stringList.add(mAvatarFile.getAbsolutePath());
            adapter.setDataList(stringList);
            imageStatus.setText("Upload Image(" + stringList.size() + "/"
                    + IMAGE_MAX_LENGTH + ")");
        }
        if (requestCode == ADD_SHOP_PHOTO && resultCode == RESULT_OK) {
            dealer_id = data.getStringExtra("dealer_id");
            String name = data.getStringExtra("name");
            shopNameEdt.setText(name);
        }
    }

    private Uri getFileUri(File mAvatarFile) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(this, "com.cds.promotion.fileprovider", mAvatarFile);
        } else {
            imageUri = Uri.fromFile(mAvatarFile);
        }
        return imageUri;
    }

    /**
     * 调用系统裁剪的方法
     */
    private void startPhoneZoom(Uri uri) {
        mAvatarFile = new File(App.getInstance().getAppCacheDir(), UUID.randomUUID().toString() + ".jpg");//这个是创建一个截取后的图片路径和名称。
        try {
            if (mAvatarFile.exists()) {
                mAvatarFile.delete();
            }
            mAvatarFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageUri = Uri.fromFile(mAvatarFile);

        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        //是否可裁剪
        intent.putExtra("corp", "true");
        //是否压缩
        intent.putExtra("scale", true);
        //裁剪器高宽比
        if (Build.MODEL.contains("HUAWEI")
                || Build.MANUFACTURER.equals("HUAWEI")) {//华为机特殊处理，防止裁剪框为圆形
            intent.putExtra("aspectX", 9998);
            intent.putExtra("aspectY", 9999);
        } else {
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
        }
        //设置裁剪框高宽
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        //返回数据
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, PHOTO_REQUEST_CROP_PHOTO);
    }

    @Override
    public void onAddClick() {
        new ActionSheetDialog(this)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem(getResources().getString(R.string.take_photo), ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                openCamera();
                            }
                        })
                .addSheetItem(getResources().getString(R.string.choose_album), ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                openAlbum();
                            }
                        }).show();
    }

    @Override
    public void onDeleteClick(int index) {
        List<String> stringList = adapter.getDataList();
        stringList.remove(index);
        adapter.setDataList(stringList);
        imageStatus.setText("Upload Image(" + stringList.size() + "/"
                + IMAGE_MAX_LENGTH + ")");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.getUiSettings().setZoomControlsEnabled(false);

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) findViewById(R.id.map), false);
                TextView title = ((TextView) infoWindow.findViewById(R.id.title));
                title.setText(marker.getTitle());

                TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
                snippet.setText(marker.getSnippet());
                return infoWindow;
            }
        });
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        enableMyLocation();
    }


    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        mHelper.requestPermissions(getResources().getString(R.string.permission_rationale_location),
                new PermissionHelper.PermissionListener() {
                    @Override
                    public void doAfterGrand(String... permission) {
                        mMap.setMyLocationEnabled(true);
                        mMap.getUiSettings().setMyLocationButtonEnabled(true);
                        getLocation();
                    }

                    @Override
                    public void doAfterDenied(String... permission) {
                        mMap.setMyLocationEnabled(false);
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        Toast.makeText(App.getInstance(), getResources().getString(R.string.location_permission_denied), Toast.LENGTH_SHORT).show();
                    }
                }, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        getLocation();
//        showCurrentPlace();
//        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
//        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
        if (location != null) {
            LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
        }
//        showCurrentPlace();
    }

    private Location mLastLocation;

    // The entry points to the Places API.
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private void getLocation() {
        Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
        locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    // Set the map's camera position to the current location of the device.
                    mLastLocation = task.getResult();
                    if(mLastLocation != null){
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(mLastLocation.getLatitude(),
                                        mLastLocation.getLongitude()), DEFAULT_ZOOM));
                    }
                } else {
                    Log.d(TAG, "Current location is null. Using defaults.");
                    Log.e(TAG, "Exception: %s", task.getException());
                    mMap.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                    mMap.getUiSettings().setMyLocationButtonEnabled(false);
                }
            }
        });
    }

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    private String[] mLikelyPlaceNames;
    private String[] mLikelyPlaceAddresses;
    private String[] mLikelyPlaceAttributions;
    private LatLng[] mLikelyPlaceLatLngs;

    private void showCurrentPlace() {
        @SuppressWarnings("MissingPermission") final Task<PlaceLikelihoodBufferResponse> placeResult =
                mPlaceDetectionClient.getCurrentPlace(null);
        placeResult.addOnCompleteListener
                (new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();

                            // Set the count, handling cases where less than 5 entries are returned.
                            int count;
                            if (likelyPlaces.getCount() < M_MAX_ENTRIES) {
                                count = likelyPlaces.getCount();
                            } else {
                                count = M_MAX_ENTRIES;
                            }

                            int i = 0;
                            mLikelyPlaceNames = new String[count];
                            mLikelyPlaceAddresses = new String[count];
                            mLikelyPlaceAttributions = new String[count];
                            mLikelyPlaceLatLngs = new LatLng[count];

                            for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                                // Build a list of likely places to show the user.
                                mLikelyPlaceNames[i] = (String) placeLikelihood.getPlace().getName();
                                mLikelyPlaceAddresses[i] = (String) placeLikelihood.getPlace()
                                        .getAddress();
                                mLikelyPlaceAttributions[i] = (String) placeLikelihood.getPlace()
                                        .getAttributions();
                                mLikelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();

                                i++;
                                if (i > (count - 1)) {
                                    break;
                                }
                            }
                            // Release the place likelihood buffer, to avoid memory leaks.
                            likelyPlaces.release();
                            // Show a dialog offering the user the list of likely places, and add a
                            // marker at the selected place.
                            openPlacesDialog();

                        } else {
                            Log.e(TAG, "Exception: %s", task.getException());
                        }
                    }
                });
    }

    /**
     * Displays a form allowing the user to select a place from a list of likely places.
     */
    private void openPlacesDialog() {
        // Ask the user to choose the place where they are now.
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The "which" argument contains the position of the selected item.
                LatLng markerLatLng = mLikelyPlaceLatLngs[which];
                String markerSnippet = mLikelyPlaceAddresses[which];
                if (mLikelyPlaceAttributions[which] != null) {
                    markerSnippet = markerSnippet + "\n" + mLikelyPlaceAttributions[which];
                }

                mMap.clear();

                currentLocation.setText(TextUtils.isEmpty(markerSnippet) ? mLikelyPlaceNames[which] : markerSnippet);

                // Add a marker for the selected place, with an info window
                // showing information about that place.
                mMap.addMarker(new MarkerOptions()
                        .title(mLikelyPlaceNames[which])
                        .position(markerLatLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.btn_maplocation))
                        .snippet(markerSnippet));

                // Position the map's camera at the location of the marker.
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                        DEFAULT_ZOOM));
            }
        };

        // Display the dialog.
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.pick_place)
                .setItems(mLikelyPlaceNames, listener)
                .show();
    }

    @Override
    public void saveVisitingSuccess() {
        hideProgressDilog();
        ToastUtils.showShort(App.getInstance(), "save visiting log success");
        finish();
    }

    @Override
    public void saveVisitingFail() {
        hideProgressDilog();
        ToastUtils.showShort(App.getInstance(), "save visiting log fail");
    }
}