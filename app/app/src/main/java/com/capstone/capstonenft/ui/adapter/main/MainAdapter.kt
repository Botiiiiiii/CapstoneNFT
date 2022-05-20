package com.capstone.capstonenft.ui.adapter.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.capstone.capstonenft.ui.fragment.main.GalleryFragment
import com.capstone.capstonenft.ui.fragment.main.MyFragment
import com.capstone.capstonenft.ui.fragment.main.SupportFragment

class MainAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> MyFragment()


            1 -> GalleryFragment()

            2 -> SupportFragment()

            else -> GalleryFragment()
        }
    }
}