package com.capstone.capstonenft.ui.adapter.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.capstone.capstonenft.R
import com.capstone.capstonenft.databinding.ViewGalleryBinding
import com.capstone.capstonenft.dto.GalleryList

class GalleryAdapter(val onClicklistener: (View, Int) -> Unit): RecyclerView.Adapter<GalleryAdapter.SearchViewHolder>() {
    var listItem = GalleryList("", arrayListOf())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ViewGalleryBinding = DataBindingUtil.inflate(inflater, R.layout.view_gallery, parent, false)

        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
//        val item = listItem.items[position]

        holder.binding.root.setOnClickListener{v ->
            onClicklistener(v, holder.adapterPosition)
        }

    }

//    override fun getItemCount(): Int = listItem.items.size
    override fun getItemCount(): Int = 10

    fun setItem(item: GalleryList){
        listItem = item
        notifyDataSetChanged()
    }

    inner class SearchViewHolder(val binding: ViewGalleryBinding): RecyclerView.ViewHolder(binding.root) {
    }
}