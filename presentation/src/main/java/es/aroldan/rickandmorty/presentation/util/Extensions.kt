package es.aroldan.rickandmorty.presentation.util

import android.animation.Animator
import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import androidx.core.widget.ImageViewCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import es.aroldan.rickandmorty.domain.model.DefinedError
import es.aroldan.rickandmorty.presentation.R
import java.lang.reflect.ParameterizedType
import java.util.Objects

fun View.fadeIn(milliseconds: Int = -1, delayStart: Int = 0, onAnimationEnd: (() -> Unit)? = null) {
    alpha = 0f
    visibility = View.VISIBLE

    val animationTime =
        if (milliseconds == -1) {
            resources.getInteger(android.R.integer.config_shortAnimTime)
        }
        else {
            milliseconds
        }

    animate().alpha(1f)
        .setStartDelay(delayStart.toLong())
        .setDuration(animationTime.toLong())
        .setListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator) {
                onAnimationEnd?.invoke()
            }
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
            override fun onAnimationStart(animation: Animator) {}
        })
}

fun Context.getError(definedError: DefinedError): String =
    definedError.toString(this)

fun DefinedError.toString(context: Context): String =
    when (this) {
        is DefinedError.Unknown -> {
            this.exception.message ?: context.getString(R.string.error_unknown)
        }
        is DefinedError.NoInternetConnection -> {
            context.getString(R.string.error_no_internet_connection)
        }
    }

fun ImageView.loadImage(url: String) {
    Glide
        .with(this)
        .load(url)
        .into(this)
}

@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, url: String?) {
    if (url != null && url.isNotEmpty()) {
        imageView.loadImage(url)
    }
}

fun <T: ViewModel> Fragment.getViewModel(): T =
    ViewModelProvider(this)[(Objects.requireNonNull(javaClass.genericSuperclass) as ParameterizedType).actualTypeArguments[0] as Class<T>]

@BindingAdapter("tintColor")
fun setImageTintColor(imageView: ImageView, color: Int) {
    ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(imageView.context.getColor(color)))
}