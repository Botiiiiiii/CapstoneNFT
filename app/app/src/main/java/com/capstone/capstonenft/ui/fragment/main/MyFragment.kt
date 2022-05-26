package com.capstone.capstonenft.ui.fragment.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.capstone.capstonenft.NFT
import com.capstone.capstonenft.R
import com.capstone.capstonenft.base.BaseFragment
import com.capstone.capstonenft.databinding.FragmentMyBinding
import com.capstone.capstonenft.ui.activity.create.CreateActivity
import com.capstone.capstonenft.ui.activity.login.LoginActivity
import com.capstone.capstonenft.ui.adapter.main.MyAdapter
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

        mBinding.fmBtnLogin.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val intent = Intent(mActivity, CreateActivity::class.java)
                startActivity(intent)
            }

        })

        return mBinding.root

    }

    override fun onResume() {
        super.onResume()
        mBinding.fmLlLogin.isVisible = NFT.instance.privatekey.isNullOrEmpty()

        mBinding.fmClMy.isVisible = !NFT.instance.privatekey.isNullOrEmpty()
        if(!NFT.instance.privatekey.isNullOrEmpty()){
            mBinding.fmTvId.text = NFT.instance.name
        }
    }

    fun onClick(v: View) {
        when (v.id) {
            R.id.fm_fbtn_create -> {
                val intent = Intent(mActivity, CreateActivity::class.java)
                startActivity(intent)
            }

            R.id.fm_btn_login -> {
                val intent = Intent(mActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }
}