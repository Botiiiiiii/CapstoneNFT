package com.capstone.capstonenft.ui.fragment.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.capstone.capstonenft.NFT
import com.capstone.capstonenft.R
import com.capstone.capstonenft.base.BaseFragment
import com.capstone.capstonenft.databinding.FragmentMyBinding
import com.capstone.capstonenft.system.utils.Trace
import com.capstone.capstonenft.ui.activity.create.CreateActivity
import com.capstone.capstonenft.ui.activity.create.ImageCheckActivity
import com.capstone.capstonenft.ui.activity.login.LoginActivity
import com.capstone.capstonenft.ui.adapter.main.MyAdapter
import com.capstone.capstonenft.viewmodel.MainViewModel
import com.google.android.material.tabs.TabLayoutMediator

class MyFragment : BaseFragment() {
    lateinit var mBinding: FragmentMyBinding
    lateinit var mAdapter: MyAdapter
    val mViewModel:MainViewModel by activityViewModels()

    private val createrActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                mViewModel.getMainItem()
            }
        }

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
            }
        }.attach()

        mBinding.fmTvTab.setTabTextColors(ContextCompat.getColor(mActivity!!, R.color.black), ContextCompat.getColor(mActivity!!, R.color.primary2))
        
        return mBinding.root

    }

    override fun onResume() {
        super.onResume()
        Trace.error("loginResponse = ${NFT.instance.loginResponse}")
        mBinding.fmLlLogin.isVisible = NFT.instance.loginResponse.privatekey.isNullOrEmpty()
        mBinding.fmClMy.isVisible = !NFT.instance.loginResponse.privatekey.isNullOrEmpty()
        if (!NFT.instance.loginResponse.privatekey.isNullOrEmpty()) {
            mBinding.fmTvId.text = NFT.instance.loginResponse.name
            mBinding.fmTvAd.text = NFT.instance.loginResponse.address
            mBinding.fmTvKlay.text = "%f KLAY".format(NFT.instance.klay.klay)
            NFT.instance.loginResponse.profile_img.let {
                if(!it.isNullOrEmpty())
                    Glide.with(this)
                        .load(it)
                        .into(mBinding.fmIvProfile)
            }
        }
    }

    fun onClick(v: View) {
        when (v.id) {
            R.id.fm_fbtn_create -> {
                val intent = Intent(mActivity, CreateActivity::class.java)
                createrActivityLauncher.launch(intent)
            }

            R.id.fm_btn_login -> {
                val intent = Intent(mActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }
}