package com.capstone.nft.ui.fragment.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.capstone.nft.R
import com.capstone.nft.base.BaseFragment
import com.capstone.nft.databinding.FragmentMyBinding
import com.capstone.nft.ui.activity.create.CreateActivity
import com.capstone.nft.ui.adapter.main.MyAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MyFragment : BaseFragment() {
    lateinit var mBinding: FragmentMyBinding
    lateinit var mAdapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my, container, false)
        mBinding.listener = this
        mAdapter = MyAdapter(mActivity!!)
        mBinding.viewPager.adapter = mAdapter

        TabLayoutMediator(
            mBinding.fmTvTab, mBinding.viewPager
        )
        { tab, position ->
            when (position) {
                0 -> tab.text = "보유 작품"
                1 -> tab.text = "내 작품"
                2 -> tab.text = "거래 내역"
            }
        }.attach()

        return mBinding.root

    }

    fun onClick(v:View){
        when(v.id){
            R.id.fm_fbtn_create -> {
                val intent = Intent(mActivity, CreateActivity::class.java)
                startActivity(intent)
            }
        }
    }
}