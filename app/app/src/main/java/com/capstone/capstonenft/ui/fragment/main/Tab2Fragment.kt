package com.capstone.capstonenft.ui.fragment.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.capstonenft.R
import com.capstone.capstonenft.databinding.FragmentTab2Binding
import com.capstone.capstonenft.ui.activity.detail.GalleryDetailActivity
import com.capstone.capstonenft.ui.adapter.main.GalleryAdapter


class Tab2Fragment : Fragment() {

    lateinit var mBinding: FragmentTab2Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_tab2, container, false)
        mBinding.ft2RvList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mBinding.ft2RvList.adapter = GalleryAdapter(){ v, pos ->
            Intent(activity, GalleryDetailActivity::class.java).apply {
                startActivity(this)
            }
        }
        return mBinding.root
    }


}