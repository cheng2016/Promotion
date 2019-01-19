package com.cds.promotion.module.visit;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cds.promotion.App;
import com.cds.promotion.R;
import com.cds.promotion.base.BaseActivity;
import com.cds.promotion.module.adapter.ImageListAdapter;
import com.cds.promotion.module.store.StoreActivity;
import com.cds.promotion.module.visit.record.VisitRecordActivity;
import com.cds.promotion.view.ActionSheetDialog;
import com.cds.promotion.view.HorizontalListView;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/16 13:56
 * @Version: 3.0.0
 */
public class VisitActivity extends BaseActivity implements VisitContract.View, View.OnClickListener, ImageListAdapter.OnImageClickListener {
    // 拍照回传码
    public final static int PHOTO_REQUEST_CAMERA = 0X01;
    // 相册选择回传吗
    public final static int PHOTO_REQUEST_GALLERY = 0X02;
    //裁剪程序请求吗
    public static final int PHOTO_REQUEST_CROP_PHOTO = 0X03;

    public static final int ADD_SHOP_PHOTO = 0X04;

    private static final int MESSAGE_MAX_LENGTH = 300;

    private static final int IMAGE_MAX_LENGTH = 3;

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

    ImageListAdapter adapter;

    VisitContract.Presenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_visit;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        findViewById(R.id.back_img).setVisibility(View.VISIBLE);
        findViewById(R.id.back_button).setVisibility(View.VISIBLE);
        findViewById(R.id.back_button).setOnClickListener(this);
        ((TextView) findViewById(R.id.title)).setText("Visiting");

        rightImg.setVisibility(View.VISIBLE);
        rightImg.setImageResource(R.mipmap.btn_visitlog);
        findViewById(R.id.right_button).setOnClickListener(this);
        findViewById(R.id.add_shop_btn).setOnClickListener(this);

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
    }

    @Override
    protected void initData() {
        new VisitPresenter(this);
        //第二个参数是需要申请的权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {   //权限还没有授予，需要在这里写申请权限的代码
            // 第二个参数是一个字符串数组，里面是你需要申请的权限 可以设置申请多个权限
            // 最后一个参数是标志你这次申请的权限，该常量在onRequestPermissionsResult中使用到
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PHOTO_REQUEST_CAMERA);

        }

        adapter = new ImageListAdapter(this);
        adapter.setListener(this);
        imgListView.setAdapter(adapter);
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
                startActivityForResult(intent,ADD_SHOP_PHOTO);
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
}
