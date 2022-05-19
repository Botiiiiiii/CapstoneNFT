package com.capstone.capstonenft.ui.adapter.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.capstone.capstonenft.R
import com.capstone.capstonenft.databinding.ViewTdBinding

class TDAdapter :RecyclerView.Adapter<TDAdapter.SearchViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding : ViewTdBinding = DataBindingUtil.inflate(inflater, R.layout.view_td, parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {

    }

    override fun getItemCount(): Int=15

    inner class SearchViewHolder(val binding: ViewTdBinding):RecyclerView.ViewHolder(binding.root){

    }


}