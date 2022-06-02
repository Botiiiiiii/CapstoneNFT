package com.capstone.capstonenft.ui.activity.create

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.capstone.capstonenft.R
import com.capstone.capstonenft.base.BaseActivity
import com.capstone.capstonenft.databinding.ActivityImageCheckBinding
import com.capstone.capstonenft.system.utils.Trace

class ImageCheckActivity : BaseActivity() {
    lateinit var mBinding : ActivityImageCheckBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_image_check)
        mBinding.listener = this

    }

    fun onClick (v : View){
        Trace.error("ddddde")
        when (v.id){
            R.id.aig_btn_regist ->{
                Intent(this,CreateActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
    }
}