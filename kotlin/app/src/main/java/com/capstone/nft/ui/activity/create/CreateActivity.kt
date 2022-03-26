package com.capstone.nft.ui.activity.create

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.capstone.nft.R
import com.capstone.nft.base.BaseActivity
import com.capstone.nft.databinding.ActivityCreateBinding

class CreateActivity: BaseActivity() {
    lateinit var mBinding: ActivityCreateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_create)
    }

    fun onClick(v:View){
        when(v.id){
            R.id.fc_iv_image -> {

            }

            R.id.fc_btn_regist -> {

            }
        }
    }
}