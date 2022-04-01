package com.capstone.nft.ui.adapter.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.capstone.nft.R
import com.capstone.nft.ui.fragment.main.Tab1Fragment
import com.capstone.nft.ui.fragment.main.Tab2Fragment
import com.capstone.nft.ui.fragment.main.Tab3Fragment


class MyAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int=3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> Tab1Fragment()
            1 -> Tab2Fragment()
            2 -> Tab3Fragment()
            else -> Tab1Fragment()
        }
    }


}