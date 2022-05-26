package com.capstone.capstonenft.ui.activity.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.capstone.capstonenft.R
import com.capstone.capstonenft.base.BaseActivity
import com.capstone.capstonenft.databinding.ActivityLoginBinding
import com.capstone.capstonenft.databinding.ActivityRegisterBinding
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {
    val TAG: String = "RegisterActivity"
    lateinit var mBinding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        mBinding.listener = this

        mBinding.arEtId.setOnFocusChangeListener { _, b ->
            mBinding.arLlId.isSelected = b
        }
        mBinding.arEtPw.setOnFocusChangeListener { _, b ->
            mBinding.arLlPw.isSelected = b
        }
        mBinding.arEtPwre.setOnFocusChangeListener{_, b->
            mBinding.arLlPwre.isSelected=b
        }

    }

    fun onClick(v:View){
        when(v.id){
            R.id.ar_et_pw -> {

            }

            R.id.ar_et_pwre -> {

            }
            R.id.ar_et_rg-> {

                if (mBinding.arEtPw.text != mBinding.arEtPwre.text)
                    Toast.makeText(this@RegisterActivity,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show()
            }
        }
    }

}