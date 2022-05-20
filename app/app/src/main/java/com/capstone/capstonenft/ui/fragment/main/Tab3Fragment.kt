package com.capstone.capstonenft.ui.fragment.main

import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.capstonenft.R
import com.capstone.capstonenft.base.BaseFragment
import com.capstone.capstonenft.databinding.FragmentTab3Binding
import com.capstone.capstonenft.system.utils.PixelUtil
import com.capstone.capstonenft.ui.adapter.main.TDAdapter

class Tab3Fragment : BaseFragment() {
    lateinit var mBinding : FragmentTab3Binding
    lateinit var mTDAdapter: TDAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_tab3,container,false)
        init()
        // Inflate the layout for this fragment
        return mBinding.root
    }

    private fun init() {
        mTDAdapter = TDAdapter()
        mBinding.fgRvTab3.adapter = mTDAdapter
        mBinding.fgRvTab3.layoutManager=GridLayoutManager(mActivity,2,GridLayoutManager.VERTICAL,false)
        mBinding.fgRvTab3.addItemDecoration(object :RecyclerView.ItemDecoration(){
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

                if (position/2!=0)outRect.top=
                    PixelUtil.dpToPx(mActivity!!,5f)
                if(spanIndex==2)outRect.right= PixelUtil.dpToPx(mActivity!!,5f)
            }
        })
    }


}