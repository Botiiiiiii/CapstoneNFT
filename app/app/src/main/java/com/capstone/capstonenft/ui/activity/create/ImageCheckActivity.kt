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
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.capstone.capstonenft.R
import com.capstone.capstonenft.base.BaseActivity
import com.capstone.capstonenft.databinding.ActivityImageCheckBinding
import com.capstone.capstonenft.dto.DialogItem
import com.capstone.capstonenft.system.utils.Trace
import com.capstone.capstonenft.ui.dialog.CommonDialog
import java.io.InputStream

class ImageCheckActivity : BaseActivity() {
    lateinit var mBinding: ActivityImageCheckBinding
    var uri: Uri? = null

    private val imageActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                result.data?.let {
                    uri = it.data as Uri
                    Glide.with(this)
                        .load(uri)
                        .into(mBinding.aigIvImage)
                }
            }
        }
    private val createrActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                setResult(RESULT_OK)
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_image_check)
        mBinding.listener = this

    }

    fun onClick(v: View) {
        when (v.id) {
            R.id.aig_iv_image -> {
                Intent(Intent.ACTION_PICK).apply {
                    this.type = MediaStore.Images.Media.CONTENT_TYPE
                    imageActivityLauncher.launch(this)
                }
            }
            R.id.aig_btn_regist -> {
                if (uri == null){
                    CommonDialog(DialogItem(
                        title = "이미지를 선택해주세요",
                        content = "이미지 유사도 측정과 민팅을 위해 이미지를 선택해주세요",
                        okBtnName = "확인"
                    )){

                    }.show(supportFragmentManager, "")

                    return
                }

                Intent(this, CreateActivity::class.java).apply {
                    this.putExtra("uri", uri.toString())
                    createrActivityLauncher.launch(this)
                }
            }
        }
    }
}