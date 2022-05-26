package com.capstone.capstonenft.ui.activity.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
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

class LoginActivity : BaseActivity() {
    val TAG: String = "LoginActivity"
    lateinit var mBinding: ActivityLoginBinding
    val mViewModel: LoginViewModel by viewModels()

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

            }

        }
    }

    fun initObserve() {
        mViewModel.loginResponse.observe(this) {
            if (it.message.equals("true")) {
                NFT.instance.name = it.name
                NFT.instance.privatekey = it.privatekey
                setPref(this, "id", mBinding.alEtId.text.toString())
                setPref(this, "pw", mBinding.alEtPw.text.toString())

                setResult(RESULT_OK)
                finish()
            }
        }
    }

}