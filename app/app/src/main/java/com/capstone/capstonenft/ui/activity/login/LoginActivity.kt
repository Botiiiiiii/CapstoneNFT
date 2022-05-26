package com.capstone.capstonenft.ui.activity.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.capstone.capstonenft.R
import com.capstone.capstonenft.base.BaseActivity
import com.capstone.capstonenft.databinding.ActivityLoginBinding
import com.capstone.capstonenft.viewmodel.login.LoginViewModel

class LoginActivity : BaseActivity() {
    val TAG: String = "LoginActivity"
    lateinit var mBinding: ActivityLoginBinding
    val mViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        mBinding.listener = this

        mBinding.alEtId.setOnFocusChangeListener { _, b ->
            mBinding.alLlId.isSelected = b
        }
        mBinding.alEtPw.setOnFocusChangeListener { _, b ->
            mBinding.alLlPw.isSelected = b
        }

    }

    fun onClick(v:View){
        when(v.id){
            R.id.al_tv_login -> {

            }

            R.id.al_tv_register -> {

            }

        }
    }

}