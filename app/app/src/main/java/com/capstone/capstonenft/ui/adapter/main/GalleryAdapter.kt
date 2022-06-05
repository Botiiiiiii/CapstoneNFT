package com.capstone.capstonenft.ui.adapter.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.capstonenft.R
import com.capstone.capstonenft.databinding.ViewGalleryBinding
import com.capstone.capstonenft.dto.GalleryList
import com.capstone.capstonenft.dto.Token

class GalleryAdapter(val onClicklistener: (View, Int) -> Unit): RecyclerView.Adapter<GalleryAdapter.SearchViewHolder>() {
    var listItem = ArrayList<Token>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ViewGalleryBinding = DataBindingUtil.inflate(inflater, R.layout.view_gallery, parent, false)

        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        if (listItem.isNullOrEmpty())
            return

        val item = listItem[position]

        holder.binding.vgTvTitle.text = item.title
        holder.binding.vgTvPrice.text = "%f KLAY".format(item.price)
        Glide.with(holder.binding.vgIvThumnail)
            .load(item.imageSrc)
            .into(holder.binding.vgIvThumnail)

        holder.binding.root.setOnClickListener{v ->
            onClicklistener(v, holder.adapterPosition)
        }

    }

    override fun getItemCount(): Int = listItem.size
//    override fun getItemCount(): Int = 10

    fun setItem(item: ArrayList<Token>){
        listItem = item
        notifyDataSetChanged()
    }

    inner class SearchViewHolder(val binding: ViewGalleryBinding): RecyclerView.ViewHolder(binding.root) {
    }
}