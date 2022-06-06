package com.capstone.capstonenft.ui.activity.create

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.capstone.capstonenft.R
import com.capstone.capstonenft.base.BaseActivity
import com.capstone.capstonenft.databinding.ActivityImageCheckBinding
import com.capstone.capstonenft.dto.DialogItem
import com.capstone.capstonenft.dto.Upload
import com.capstone.capstonenft.system.utils.Trace
import com.capstone.capstonenft.ui.dialog.CommonDialog
import com.capstone.capstonenft.viewmodel.CreateViewModel
import java.io.InputStream

class ImageCheckActivity : BaseActivity() {
    lateinit var mBinding: ActivityImageCheckBinding
    lateinit var item:Upload
    var uri: Uri? = null
    val mViewModel: CreateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_image_check)
        mBinding.listener = this

        item = intent.getSerializableExtra("data") as Upload

        mViewModel.getTokenInfo(item.sim_token_id)

        mViewModel.token.observe(this){
            Glide.with(this)
                .load(it.imageSrc)
                .into(mBinding.aigIvImage)
            mBinding.aigTvSimialrity.text = "유사도 : %d".format(item.score.toInt()) + "%"
        }

        setResult(RESULT_OK)

    }

    fun onClick(v: View) {
        when (v.id) {
            R.id.aig_btn_regist -> {
                finish()
            }
        }
    }
}