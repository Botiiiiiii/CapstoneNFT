package com.capstone.nft

import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide

class BindingAdapter {
    @androidx.databinding.BindingAdapter("app:imgSrc")
    fun loadImg(imageView: ImageView, id: Int) {
        imageView.setImageResource(id)
    }

    @androidx.databinding.BindingAdapter("app:imgSrc")
    fun loadImg(imageView: ImageView, path: String) {
        if (path == null)
            return;

        Glide.with(imageView.getContext())
            .load(Uri.parse(path))
            .into(imageView);
    }

    @androidx.databinding.BindingAdapter("app:selected")
    fun setSelected(view: View, isSelected: Boolean) {
        view.isSelected = isSelected;
    }
}