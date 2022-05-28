package com.capstone.capstonenft.ui.activity.detail

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.capstone.capstonenft.R
import com.capstone.capstonenft.base.BaseActivity
import com.capstone.capstonenft.databinding.ActivityGalleryDetailBinding

class GalleryDetailActivity: BaseActivity() {
    lateinit var mBinding: ActivityGalleryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_gallery_detail)

        Glide.with(this)
            .load(R.drawable.test)
            .into(mBinding.agdIvPicture)

        mBinding.agdTvOwner.setOnClickListener {
            Intent(this, UserDetailActivity::class.java).apply {
                startActivity(this)
            }
        }
    }
}