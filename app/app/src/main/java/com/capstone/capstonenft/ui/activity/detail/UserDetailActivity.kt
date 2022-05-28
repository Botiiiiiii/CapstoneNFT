package com.capstone.capstonenft.ui.activity.detail

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.capstonenft.R
import com.capstone.capstonenft.base.BaseActivity
import com.capstone.capstonenft.databinding.ActivityUserDetailBinding
import com.capstone.capstonenft.ui.adapter.main.GalleryAdapter
import com.capstone.capstonenft.ui.adapter.main.MainAdapter

class UserDetailActivity: BaseActivity() {
    lateinit var mBinding:ActivityUserDetailBinding
    lateinit var mAdapter: GalleryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_detail)
        mBinding.audRvList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        mAdapter = GalleryAdapter(){v, pos ->
            Intent(this, GalleryDetailActivity::class.java).apply {
                startActivity(this)
            }
        }
        mBinding.audRvList.adapter = mAdapter

    }
}