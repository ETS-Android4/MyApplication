package com.example.william.my.module.sample.activity

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.load
import coil.transform.BlurTransformation
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.william.my.bean.base.Urls
import com.example.william.my.module.router.ARouterPath
import com.example.william.my.module.sample.R
import com.example.william.my.module.sample.databinding.SampleActivityCoilBinding

/**
 * https://github.com/coil-kt/coil
 * 模糊变换（BlurTransformation）、圆形变换（CircleCropTransformation）、
 * 灰度变换（GrayscaleTransformation）和圆角变换（RoundedCornersTransformation）
 */
@Route(path = ARouterPath.Sample.Sample_Coil)
class CoilActivity : AppCompatActivity() {

    lateinit var mBinding: SampleActivityCoilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setContentView(R.layout.kt_activity_coil)

        mBinding = SampleActivityCoilBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        //binding.image.load(url)

        mBinding.imageView.load(Urls.Url_Image) {
            crossfade(true)//淡入淡出
            placeholder(R.drawable.ic_launcher)
            transformations(
                BlurTransformation(this@CoilActivity),
                //CircleCropTransformation(),
                //GrayscaleTransformation(),
                //RoundedCornersTransformation(
                //    topLeft = 8f,
                //    topRight = 8f,
                //    bottomLeft = 8f,
                //    bottomRight = 8f
                //)
            )
        }

        /*
         * gif
         */
        ImageLoader.Builder(this)
            .componentRegistry {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder(this@CoilActivity))
                } else {
                    add(GifDecoder())
                }
            }
            .build()

        /*
         * svg
         */
        ImageLoader.Builder(this)
            .componentRegistry {
                add(SvgDecoder(this@CoilActivity))
            }
            .build()
    }
}