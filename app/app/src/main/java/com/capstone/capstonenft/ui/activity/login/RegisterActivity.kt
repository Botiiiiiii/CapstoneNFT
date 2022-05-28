package com.capstone.capstonenft.ui.activity.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.capstone.capstonenft.NFT
import com.capstone.capstonenft.R
import com.capstone.capstonenft.base.BaseActivity
import com.capstone.capstonenft.databinding.ActivityLoginBinding
import com.capstone.capstonenft.databinding.ActivityRegisterBinding
import com.capstone.capstonenft.system.utils.Trace
import com.capstone.capstonenft.system.utils.setPref
import com.capstone.capstonenft.viewmodel.login.LoginViewModel
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {
    lateinit var mBinding: ActivityRegisterBinding
    val mViewModel: LoginViewModel by viewModels()

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

        mViewModel.register.observe(this){
            if(it.message.equals("true")){
                setPref(this, "id", mBinding.arEtId.text.toString())
                setPref(this, "pw", mBinding.arEtPw.text.toString())

                NFT.instance.privatekey = it.privatekey
                NFT.instance.name = it.name
                setResult(RESULT_OK)
                finish()
            }
        }
    }

    fun onClick(v:View){
        when(v.id){
            R.id.ar_et_pw -> {

            }

            R.id.ar_et_pwre -> {

            }
            R.id.ar_et_rg-> {
                Trace.error("pw = ${mBinding.arEtPw.text} rePw = ${mBinding.arEtPwre.text}")
                if (mBinding.arEtPw.text.toString() != mBinding.arEtPwre.text.toString())
                    Toast.makeText(this@RegisterActivity,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show()
                else
                    mViewModel.register(mBinding.arEtId.text.toString(), mBinding.arEtPw.text.toString())
            }
        }
    }

}