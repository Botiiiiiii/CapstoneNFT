package com.capstone.nft.ui.adapter.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.capstone.nft.R
import com.capstone.nft.databinding.ViewGalleryBinding

class GalleryAdapter: RecyclerView.Adapter<GalleryAdapter.SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ViewGalleryBinding = DataBindingUtil.inflate(inflater, R.layout.view_gallery, parent, false)

        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = 15

    inner class SearchViewHolder(val binding: ViewGalleryBinding): RecyclerView.ViewHolder(binding.root) {
    }
}