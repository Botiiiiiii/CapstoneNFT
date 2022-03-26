package com.capstone.nft.ui.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.capstone.nft.R
import com.capstone.nft.base.BaseFragment
import com.capstone.nft.databinding.FragmentSupportBinding

class SupportFragment: BaseFragment() {
    lateinit var mBinding: FragmentSupportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_support, container, false)
        return mBinding.root
    }
}