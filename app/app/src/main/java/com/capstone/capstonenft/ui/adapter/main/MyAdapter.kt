package com.capstone.capstonenft.ui.adapter.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.capstone.capstonenft.ui.fragment.main.Tab1Fragment
import com.capstone.capstonenft.ui.fragment.main.Tab2Fragment
import com.capstone.capstonenft.ui.fragment.main.Tab3Fragment


class MyAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> Tab1Fragment()
            1 -> Tab2Fragment()
            2 -> Tab3Fragment()
            else -> Tab1Fragment()
        }
    }


}