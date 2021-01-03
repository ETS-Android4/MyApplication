package com.example.william.my.sample.activity;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.sample.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

@Route(path = ARouterPath.Sample.Sample_PicCrop)
public class PicCropActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int TO_ALBUM = 0;
    private static final int TO_CAMERA = 1;
    private static final int TO_CAMERA_FULL = 2;

    private static final int TO_CROP = 3;

    private ImageView mImageView;

    private final String[] mItems = {"图库", "拍照", "拍照"};

    private Uri image_crop_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basics_layout_image);

        mImageView = findViewById(R.id.basics_imageView);
        mImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PicCropActivity.this);
        builder.setItems(mItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:// 跳转图库界面
                        Intent intentFromAlbum = new Intent(Intent.ACTION_PICK);
                        if (intentFromAlbum.resolveActivity(getPackageManager()) != null) {
                            intentFromAlbum.setType("image/*");
                            startActivityForResult(intentFromAlbum, TO_ALBUM);
                        }
                        break;
                    case 1:// 跳转拍照界面
                        Intent intentFromCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intentFromCamera.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(intentFromCamera, TO_CAMERA);
                        }
                        break;
                    case 2:// Save the full-size photo
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            File photoFile = null;
                            try {
                                photoFile = createImageFile();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            if (photoFile != null) {
                                image_crop_uri = FileProvider.getUriForFile(PicCropActivity.this,
                                        getPackageName() + ".fileProvider", photoFile);
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_crop_uri);
                                startActivityForResult(takePictureIntent, TO_CAMERA_FULL);
                            }
                        }
                        break;
                }
            }
        }).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TO_ALBUM:
                    if (data != null && data.getData() != null) {
                        cropImage(data.getData(), "TO_ALBUM");
                    }
                    break;
                case TO_CAMERA:
                    if (data != null && data.getExtras() != null) {
                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
                        cropImage(uri, "TO_CAMERA");
                    }
                    break;
                case TO_CAMERA_FULL:
                    if (image_crop_uri != null) {
                        cropImage(image_crop_uri, "TO_CAMERA_FULL");
                    }
                    break;
                case TO_CROP://不返回data数据
                    try {
                        Bitmap bitmap;
                        //if (data != null && data.getExtras() != null) {
                        //	bitmap = (Bitmap) data.getExtras().get("data");
                        //} else {
                        //	bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(image_crop_uri));
                        //}
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(image_crop_uri));
                        mImageView.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmm", Locale.CHINA).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",    /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void cropImage(Uri uri, String type) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        //添加权限
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);

        /*
         * 是否通过intent传递截获的图片，此方法返回的图片只能是小图片
         * 故只保存图片Uri，调用时将Uri转换为Bitmap，此方法还可解决MIUI系统不能return data的问题
         */
        intent.putExtra("return-data", false);

        if ("TO_ALBUM".equals(type)) {
            Log.e(type, "File Path：" + getFilePath(uri));
        } else if ("TO_CAMERA".equals(type)) {
            Log.e(type, "File Path：" + getFilePath(uri));
        } else if ("TO_CAMERA_FULL".equals(type)) {
            Log.e(type, "File Path：" + mCurrentPhotoPath);
        }

        try {
            image_crop_uri = Uri.fromFile(createImageFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.e("TAG", "uri : " + image_crop_uri);

        //necessary!
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_crop_uri);

        startActivityForResult(intent, TO_CROP);
    }

    /**
     * 获取文件路径
     */
    public String getFilePath(Uri uri) {
        String path = "";
        if (null != uri.getScheme()) {
            switch (uri.getScheme()) {
                case ContentResolver.SCHEME_FILE:
                    path = uri.getPath();
                    break;
                case ContentResolver.SCHEME_CONTENT:
                    Cursor cursor = getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
                    if (null != cursor) {
                        if (cursor.moveToFirst()) {
                            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                            if (index > -1) {
                                path = cursor.getString(index);
                            }
                        }
                        cursor.close();
                    }
                    break;
            }
        }
        return path;
    }
}