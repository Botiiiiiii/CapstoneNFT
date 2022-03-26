package com.capstone.nft.ui.adapter.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.capstone.nft.ui.activity.create.CreateActivity
import com.capstone.nft.ui.fragment.main.GalleryFragment
import com.capstone.nft.ui.fragment.main.MyFragment
import com.capstone.nft.ui.fragment.main.SupportFragment

class MainAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> MyFragment()


            1 -> GalleryFragment()

            2 -> CreateActivity()

            3 -> SupportFragment()
            else -> GalleryFragment()
        }
    }
}