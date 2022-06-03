package com.capstone.capstonenft.ui.fragment.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.capstonenft.NFT
import com.capstone.capstonenft.R
import com.capstone.capstonenft.databinding.FragmentTab1Binding
import com.capstone.capstonenft.databinding.FragmentTab3Binding
import com.capstone.capstonenft.ui.activity.detail.GalleryDetailActivity
import com.capstone.capstonenft.ui.adapter.main.GalleryAdapter


class Tab1Fragment : Fragment() {
    lateinit var mBinding: FragmentTab1Binding
    lateinit var mAdapter: GalleryAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_tab1,container,false)
        mBinding.ftRvList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mAdapter = GalleryAdapter(){ v, pos ->
            Intent(activity, GalleryDetailActivity::class.java).apply {
                this.putExtra("data", NFT.instance.loginResponse.token_list[pos])
                activity?.startActivity(this)
            }
        }
        mBinding.ftRvList.adapter = mAdapter
        mAdapter.setItem(NFT.instance.loginResponse.token_list)
        // Inflate the layout for this fragment
        return mBinding.root
    }

}