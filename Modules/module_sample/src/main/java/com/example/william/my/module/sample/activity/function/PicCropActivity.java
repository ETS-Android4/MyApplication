package com.example.william.my.module.sample.activity.function;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.william.my.library.base.BaseActivity;
import com.example.william.my.module.router.ARouterPath;
import com.example.william.my.module.router.provider.ImageUtilsService;
import com.example.william.my.module.sample.R;

import java.io.File;

@Route(path = ARouterPath.Sample.Sample_PicCrop)
public class PicCropActivity extends BaseActivity implements View.OnClickListener {

    private static final int TO_ALBUM = 0;
    private static final int TO_CAMERA = 1;
    private static final int TO_CAMERA_FULL = 2;

    private static final int TO_CROP = 3;

    private ImageView mImageView;

    private final String[] mItems = {"图库", "拍照", "拍照"};

    private Uri sourceUri, destinationUri;

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
                        intentFromAlbum.setType("image/*");
                        startActivityForResult(intentFromAlbum, TO_ALBUM);
                        break;
                    case 1:// 跳转拍照界面
                        Intent intentFromCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intentFromCamera, TO_CAMERA);
                        break;
                    case 2:// Save the full-size photo
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File file = new File(getExternalCacheDir() + File.separator + System.currentTimeMillis() + ".jpg");
                        sourceUri = FileProvider.getUriForFile(PicCropActivity.this, getPackageName() + ".fileProvider", file);
                        Log.e("TAG", "Save the full-size photo : " + sourceUri);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, sourceUri);
                        startActivityForResult(takePictureIntent, TO_CAMERA_FULL);
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

                        Uri source = data.getData();

                        File file = new File(getExternalCacheDir() + File.separator + System.currentTimeMillis() + ".jpg");
                        Uri destination = Uri.fromFile(file);
                        toCrop(source, destination);
                    }
                    break;
                case TO_CAMERA:
                    if (data != null && data.getExtras() != null) {
                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                        Uri source = saveBitmap2Uri(bitmap);

                        File file = new File(getExternalCacheDir() + File.separator + System.currentTimeMillis() + ".jpg");
                        Uri destination = Uri.fromFile(file);
                        toCrop(source, destination);
                    }
                    break;
                case TO_CAMERA_FULL:
                    if (sourceUri != null) {
                        File file = new File(getExternalCacheDir() + File.separator + System.currentTimeMillis() + ".jpg");
                        Uri destination = Uri.fromFile(file);
                        toCrop(sourceUri, destination);
                    }
                    break;
                case TO_CROP:
                    /*
                     * 设置 MediaStore.EXTRA_OUTPUT false，不返回data数据，通过Uri获取数据
                     */
                    try {
                        Bitmap bitmap;
                        //if (data != null && data.getExtras() != null) {
                        //	bitmap = (Bitmap) data.getExtras().get("data");
                        //} else {
                        //	bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(image_crop_uri));
                        //}
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(destinationUri));
                        mImageView.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private void toCrop(Uri source, Uri destination) {

        Log.e("TAG", "source : " + source);
        Log.e("TAG", "destination : " + destination);

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(source, "image/*");

        //添加权限
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 1024);
        intent.putExtra("outputY", 1024);

        /*
         * 是否通过intent传递截获的图片，此方法返回的图片只能是小图片
         * 故只保存图片Uri，调用时将Uri转换为Bitmap，此方法还可解决MIUI系统不能return data的问题
         */
        intent.putExtra("return-data", false);

        destinationUri = destination;//保存指定Uri

        //necessary!
        //将URI指向相应的file:///... , 需要使用Uri.fromFile(file)生成
        intent.putExtra(MediaStore.EXTRA_OUTPUT, destination);

        startActivityForResult(intent, TO_CROP);
    }

    private Uri saveBitmap2Uri(Bitmap bitmap) {
        File file = new File(getExternalCacheDir() + File.separator + System.currentTimeMillis() + ".jpg");
        ImageUtilsService service = (ImageUtilsService) ARouter.getInstance().build(ARouterPath.Service.ImageUtilsService).navigation();
        boolean successful = service.save(bitmap, file, Bitmap.CompressFormat.JPEG);
        return FileProvider.getUriForFile(PicCropActivity.this, getPackageName() + ".fileProvider", file);
    }
}