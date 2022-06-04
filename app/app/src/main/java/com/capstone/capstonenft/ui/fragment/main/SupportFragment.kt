package com.capstone.capstonenft.ui.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.capstone.capstonenft.R
import com.capstone.capstonenft.base.BaseFragment
import com.capstone.capstonenft.databinding.FragmentSupportBinding

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