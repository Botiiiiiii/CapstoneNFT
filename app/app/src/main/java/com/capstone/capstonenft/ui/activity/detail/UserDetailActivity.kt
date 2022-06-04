package com.capstone.capstonenft.ui.activity.detail

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.capstone.capstonenft.R
import com.capstone.capstonenft.base.BaseActivity
import com.capstone.capstonenft.databinding.ActivityUserDetailBinding
import com.capstone.capstonenft.ui.adapter.main.GalleryAdapter
import com.capstone.capstonenft.ui.adapter.main.MainAdapter
import com.capstone.capstonenft.viewmodel.DetailViewModel

class UserDetailActivity : BaseActivity() {
    lateinit var mBinding: ActivityUserDetailBinding
    lateinit var mAdapter: GalleryAdapter
    var owner = ""
    val mViewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_detail)
        owner = intent.getStringExtra("owner").toString()
        mViewModel.getOwnerItem(owner)

        mBinding.audRvList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mAdapter = GalleryAdapter() { v, pos ->
            Intent(this, GalleryDetailActivity::class.java).apply {
                this.putExtra("data", mViewModel.owner.value!!.token_sale[pos])
                startActivity(this)
            }
        }
        mBinding.audRvList.adapter = mAdapter

        mViewModel.owner.observe(this) {
            mBinding.data = it
            Glide.with(this)
                .load(it.userinfo.profile_img)
                .into(mBinding.fmIvProfile)

            mAdapter.setItem(it.token_sale)
        }

    }
}