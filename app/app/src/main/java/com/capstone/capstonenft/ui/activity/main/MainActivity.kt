package com.capstone.capstonenft.ui.activity.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import com.capstone.capstonenft.NFT
import com.capstone.capstonenft.R
import com.capstone.capstonenft.base.BaseActivity
import com.capstone.capstonenft.databinding.ActivityMainBinding
import com.capstone.capstonenft.system.utils.Trace
import com.capstone.capstonenft.ui.adapter.main.MainAdapter
import com.capstone.capstonenft.viewmodel.MainViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_image_check.view.*

class MainActivity : BaseActivity() {
    lateinit var mBinding: ActivityMainBinding
    lateinit var mAdapter: MainAdapter
    val mViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setLayout()
        setObserve()
    }

    fun setLayout() {
        mAdapter = MainAdapter(this)
        mBinding.amVpPager.adapter = mAdapter
        TabLayoutMediator(
            mBinding.amTlTab,
            mBinding.amVpPager
        ) { tab: TabLayout.Tab, pos: Int ->
            when (pos) {
                0 -> {
                    tab.text = "My"
                }
                1 -> {
                    tab.text = "Gallery"
                }
                2 -> {
                    tab.text = "More"
                }
            }
        }.attach()
        mBinding.amTlTab.setTabTextColors(ContextCompat.getColor(this, R.color.black), ContextCompat.getColor(this, R.color.white))

        mBinding.amVpPager.setCurrentItem(1, false)
    }

    fun setObserve(){
        mViewModel.mldGallery.observe(this){
            mBinding.amVpPager.get(0)
        }
    }
}