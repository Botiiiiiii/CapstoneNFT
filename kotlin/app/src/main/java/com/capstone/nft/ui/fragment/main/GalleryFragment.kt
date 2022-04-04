package com.capstone.nft.ui.fragment.main

import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.nft.R
import com.capstone.nft.base.BaseFragment
import com.capstone.nft.databinding.FragmentGalleryBinding
import com.capstone.nft.system.utils.PixelUtil
import com.capstone.nft.ui.adapter.main.GalleryAdapter
import com.capstone.nft.ui.adapter.main.ProfileAdapter

class GalleryFragment() : BaseFragment() {
    lateinit var mBinding: FragmentGalleryBinding
    lateinit var mProfileAdapter: ProfileAdapter
    lateinit var mGalleryAdapter: GalleryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_gallery, container, false)

        init()

        return mBinding.root
    }

    fun init() {
        mProfileAdapter = ProfileAdapter()
        mBinding.fgRvArtist.adapter = mProfileAdapter
        mBinding.fgRvArtist.layoutManager =
            LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false)

        mGalleryAdapter = GalleryAdapter()
        mBinding.fgRvGallery.adapter = mGalleryAdapter
        mBinding.fgRvGallery.layoutManager = GridLayoutManager(mActivity, 2, GridLayoutManager.VERTICAL, false)
        mBinding.fgRvGallery.addItemDecoration(object : RecyclerView.ItemDecoration() {

            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                super.onDraw(c, parent, state)
            }

            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)

                val position = parent.getChildAdapterPosition(view)
                val lp = view.layoutParams as GridLayoutManager.LayoutParams
                val spanIndex = lp.spanIndex

                //첫째줄
                if (position / 2 != 0) outRect.top =
                    PixelUtil.dpToPx(mActivity!!, 5f)

                //제일 오른쪽 아이템
                if (spanIndex == 2) outRect.right = PixelUtil.dpToPx(mActivity!!, 5f)

            }
        })
    }
}