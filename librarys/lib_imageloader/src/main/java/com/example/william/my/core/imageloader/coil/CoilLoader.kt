package com.example.william.my.core.imageloader.coil

import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import coil.load
import coil.transform.BlurTransformation
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.example.william.my.core.imageloader.corner.CornerType
import com.example.william.my.core.imageloader.loader.ILoader

/**
 * https://github.com/coil-kt/coil
 */
class CoilLoader : ILoader {

    override fun load(
        activity: FragmentActivity,
        url: String,
        imageView: ImageView
    ) {
        imageView.load(url)
    }

    override fun loadCircle(
        activity: FragmentActivity,
        url: String,
        imageView: ImageView
    ) {
        imageView.load(url) {
            transformations(
                CircleCropTransformation()
            )
        }
    }

    override fun loadRadius(
        activity: FragmentActivity,
        url: String,
        imageView: ImageView,
        radius: Float
    ) {
        imageView.load(url) {
            transformations(
                RoundedCornersTransformation(
                    radius
                )
            )
        }
    }

    override fun loadRadius(
        activity: FragmentActivity,
        url: String,
        imageView: ImageView,
        radius: Float,
        type: CornerType
    ) {
        lateinit var transformations: RoundedCornersTransformation
        when (type) {
            CornerType.TOP_LEFT -> {
                transformations = RoundedCornersTransformation(
                    topLeft = radius,
                )
            }
            CornerType.TOP_RIGHT -> {
                transformations = RoundedCornersTransformation(
                    topRight = radius,
                )
            }
            CornerType.BOTTOM_LEFT -> {
                transformations = RoundedCornersTransformation(
                    bottomLeft = radius
                )
            }
            CornerType.BOTTOM_RIGHT -> {
                transformations = RoundedCornersTransformation(
                    bottomRight = radius
                )
            }

            CornerType.TOP -> {
                transformations = RoundedCornersTransformation(
                    topLeft = radius,
                    topRight = radius
                )
            }
            CornerType.BOTTOM -> {
                transformations = RoundedCornersTransformation(
                    bottomLeft = radius,
                    bottomRight = radius
                )
            }
            CornerType.LEFT -> {
                transformations = RoundedCornersTransformation(
                    topLeft = radius,
                    bottomLeft = radius
                )
            }
            CornerType.RIGHT -> {
                transformations = RoundedCornersTransformation(
                    topRight = radius,
                    bottomRight = radius
                )
            }
            CornerType.ALL -> {
                transformations = RoundedCornersTransformation(
                    radius
                )
            }
        }
        imageView.load(url) {
            transformations(
                transformations
            )
        }
    }

    override fun loadBlur(
        activity: FragmentActivity,
        url: String,
        imageView: ImageView
    ) {
        imageView.load(url) {
            transformations(
                BlurTransformation(activity)
            )
        }
    }

    override fun loadBlur(
        activity: FragmentActivity,
        url: String,
        imageView: ImageView,
        radius: Float
    ) {
        imageView.load(url) {
            transformations(
                BlurTransformation(activity, radius)
            )
        }
    }

    override fun loadBlur(
        activity: FragmentActivity,
        url: String,
        imageView: ImageView,
        radius: Float,
        sampling: Float
    ) {
        imageView.load(url) {
            transformations(
                BlurTransformation(activity, radius, sampling)
            )
        }
    }
}