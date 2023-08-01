package com.dhkim.tvshows.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

object BindingAdapters {

    @BindingAdapter("android:imageURL")
    @JvmStatic
    fun setImageURL(imageView: ImageView, URL: String) {
        print("dhkim111 $URL")
        try {
            imageView.alpha = 0f
            Picasso.get().load(URL).noFade().into(imageView, object:Callback {
                override fun onSuccess() {
                    imageView.animate().setDuration(300).alpha(1f).start()
                }

                override fun onError(e: Exception?) {
                }

            } )
        } catch (e: Exception) {

        }
    }
}