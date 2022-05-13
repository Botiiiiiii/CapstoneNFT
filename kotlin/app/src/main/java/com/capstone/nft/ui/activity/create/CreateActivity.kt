package com.capstone.nft.ui.activity.create

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.capstone.nft.R
import com.capstone.nft.base.BaseActivity
import com.capstone.nft.databinding.ActivityCreateBinding

class CreateActivity : BaseActivity() {
    lateinit var mBinding: ActivityCreateBinding
    lateinit var mResultLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_create)
        mBinding.listener = this
        mResultLauncher = registerForActivityResult(StartActivityForResult()) {

            if (it.resultCode == RESULT_OK) {
                var currentImageUri = it.data?.data
                try {
                    Glide.with(mBinding.fcIvImage.context)
                        .load(currentImageUri)
                        .into(mBinding.fcIvImage)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else if (it.resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }

    fun onClick(v: View) {
        when (v.id) {
            R.id.fc_iv_image -> {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.setType("image/*")
                mResultLauncher.launch(intent)
            }

            R.id.fc_btn_regist -> {

            }
        }
    }
}