package com.capstone.nft.ui.adapter.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.capstone.nft.R
import com.capstone.nft.databinding.ActivityCreateBinding.inflate
import com.capstone.nft.databinding.ViewGalleryBinding
import com.capstone.nft.databinding.ViewProfileBinding

class ProfileAdapter: RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val binding = ViewProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
    }

    override fun getItemCount(): Int = 100

    inner class ProfileViewHolder(binding: ViewProfileBinding): RecyclerView.ViewHolder(binding.root) {
        val binding = binding
    }
}