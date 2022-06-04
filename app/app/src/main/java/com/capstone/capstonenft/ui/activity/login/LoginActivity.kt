package com.capstone.capstonenft.ui.activity.login

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.capstone.capstonenft.NFT
import com.capstone.capstonenft.R
import com.capstone.capstonenft.base.BaseActivity
import com.capstone.capstonenft.databinding.ActivityLoginBinding
import com.capstone.capstonenft.system.utils.setPref
import com.capstone.capstonenft.viewmodel.login.LoginViewModel
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class LoginActivity : BaseActivity() {
    val TAG: String = "LoginActivity"
    lateinit var mBinding: ActivityLoginBinding
    val mViewModel: LoginViewModel by viewModels()
    private val imageActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                result.data?.let {
                    val uri = it.data as Uri

                    val ins: InputStream? = uri.let {
                        applicationContext.contentResolver.openInputStream(
                            it
                        )
                    }
                    val img: Bitmap = BitmapFactory.decodeStream(ins)
                    var file = createImageFile()
                    BitmapConvertFile(img, file.path)
                    mViewModel.uploadFile(file)
                }
            }
        }
    private val registActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        mBinding.listener = this
        initObserve()

        mBinding.alEtId.setOnFocusChangeListener { _, b ->
            mBinding.alLlId.isSelected = b
        }
        mBinding.alEtPw.setOnFocusChangeListener { _, b ->
            mBinding.alLlPw.isSelected = b
        }

        mBinding.root.setOnClickListener {
            softkeyboardHide()
        }

    }

    fun softkeyboardHide() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    }

    fun onClick(v: View) {
        when (v.id) {
            R.id.al_tv_login -> {
                mViewModel.login(mBinding.alEtId.text.toString(), mBinding.alEtPw.text.toString())
            }
            R.id.al_tv_register -> {
                Intent(this, RegisterActivity::class.java).apply {
                    registActivityLauncher.launch(this)
                }
//                Intent(Intent.ACTION_PICK).apply {
//                    this.type = MediaStore.Images.Media.CONTENT_TYPE
//                    imageActivityLauncher.launch(this)
//                }
            }
        }
    }
    fun initObserve() {
        mViewModel.loginResponse.observe(this) {
            if (it.message.equals("true")) {
                NFT.instance.loginResponse = it
                setPref(this, "id", mBinding.alEtId.text.toString())
                setPref(this, "pw", mBinding.alEtPw.text.toString())
                setResult(RESULT_OK)
                finish()
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