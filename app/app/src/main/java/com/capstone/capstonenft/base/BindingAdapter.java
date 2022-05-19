package com.capstone.capstonenft.base;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class BindingAdapter {
    @androidx.databinding.BindingAdapter("app:imgSrc")
    public static void loadImg(ImageView imageView, int id){
        imageView.setImageResource(id);
    }

    @androidx.databinding.BindingAdapter("app:imgSrc")
    public static void loadImg(ImageView imageView, String path){
        if(path == null)
            return;

        Glide.with(imageView.getContext())
                .load(Uri.parse(path))
                .into(imageView);
    }

    @androidx.databinding.BindingAdapter("app:selected")
    public static void selected(View view, Boolean selected){
        view.setSelected(selected);
    }
}