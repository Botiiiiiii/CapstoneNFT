package com.capstone.capstonenft.ui.activity.create

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.capstone.capstonenft.NFT
import com.capstone.capstonenft.R
import com.capstone.capstonenft.base.BaseActivity
import com.capstone.capstonenft.databinding.ActivityCreateBinding
import com.capstone.capstonenft.dto.DialogItem
import com.capstone.capstonenft.ui.dialog.CommonDialog
import com.capstone.capstonenft.viewmodel.CreateViewModel
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class CreateActivity : BaseActivity() {
    lateinit var mBinding: ActivityCreateBinding
    lateinit var mResultLauncher: ActivityResultLauncher<Intent>
    private val mViewModel: CreateViewModel by viewModels()

    lateinit var uri:Uri
    lateinit var file:File

    private val imageActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                result.data?.let {
                    uri = it.data as Uri
                    Glide.with(this)
                        .load(uri)
                        .into(mBinding.fcIvImage)

                    val ins: InputStream? = uri.let {
                        applicationContext.contentResolver.openInputStream(it)
                    }

                    val img: Bitmap = BitmapFactory.decodeStream(ins)
                    file = createImageFile()
                    BitmapConvertFile(img, file.path)

                }
            }
        }
    private val simActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_create)
        mBinding.listener = this

        mViewModel.token.observe(this){
            NFT.instance.loginResponse.token_list.add(it)
            setResult(RESULT_OK)
            finish()
        }

        mViewModel.upload.observe(this){
            Intent(this, ImageCheckActivity::class.java).apply {
                this.putExtra("data", it)
                simActivityLauncher.launch(this)
            }
        }
    }

    fun onClick(v: View) {
        when (v.id) {
            R.id.fc_iv_image -> {
                Intent(Intent.ACTION_PICK).apply {
                    this.type = MediaStore.Images.Media.CONTENT_TYPE
                    imageActivityLauncher.launch(this)
                }
            }

            R.id.fc_btn_regist -> {
                if (uri == null){
                    CommonDialog(
                        DialogItem(
                        title = "이미지를 선택해주세요",
                        content = "이미지 유사도 측정과 민팅을 위해 이미지를 선택해주세요",
                        okBtnName = "확인"
                    )
                    ){

                    }.show(supportFragmentManager, "")

                    return
                }

                if(file == null)
                    return

                if(mBinding.fcEtDescription.text.isNullOrEmpty())
                    return

                if(mBinding.fpEtName.text.isNullOrEmpty())
                    return

                mViewModel.uploadImage(file, mBinding.fpEtName.text.toString(), mBinding.fcEtDescription.text.toString(), "aaa", 1)
            }
        }
    }

    private fun createImageFile(): File {

        // 이미지 파일 이름 ( blackJin_{시간}_ )
        val timeStamp: String = SimpleDateFormat("HHmmss").format(Date())
        val imageFileName = "file"
        // 이미지가 저장될 파일 이름 ( blackJin )
        val storageDir: File =
            File(cacheDir.path)
        if (!storageDir.exists()) storageDir.mkdirs()
        // 빈 파일 생성
        val image = File.createTempFile(imageFileName, ".png", storageDir)
        return image
    }
    private fun BitmapConvertFile(bitmap: Bitmap, strFilePath: String) {
        var file = File(strFilePath);
        // OutputStream 선언 -> bitmap데이터를 OutputStream에 받아 File에 넣어주는 용도
        var out: OutputStream? = null;
        try {
            // 파일 초기화
            file.createNewFile();
            // OutputStream에 출력될 Stream에 파일을 넣어준다
            out = FileOutputStream(file);
            // bitmap 압축
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (e: Exception) {
            e.printStackTrace();
        } finally {
            try {
                out?.close();
            } catch (e: IOException) {
                e.printStackTrace();
            }
        }
    }
}