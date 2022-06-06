package com.capstone.capstonenft.ui.activity.splash

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.capstone.capstonenft.NFT
import com.capstone.capstonenft.R
import com.capstone.capstonenft.base.BaseActivity
import com.capstone.capstonenft.databinding.ActivitySplashBinding
import com.capstone.capstonenft.dto.DialogItem
import com.capstone.capstonenft.system.utils.Trace
import com.capstone.capstonenft.system.utils.getPref
import com.capstone.capstonenft.system.utils.setPref
import com.capstone.capstonenft.ui.activity.main.MainActivity
import com.capstone.capstonenft.ui.dialog.CommonDialog
import com.capstone.capstonenft.viewmodel.login.LoginViewModel

class SplashActivity : BaseActivity() {
    lateinit var mBinding: ActivitySplashBinding
    val mViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        initObserve()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        checkPermission()
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
    }

    private fun checkPermission() {
        val internetPermission =
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        if (internetPermission == PackageManager.PERMISSION_GRANTED) {
            startProcess()
        } else {
            requestPermission()
        }

    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 99)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            99 -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startProcess()
                } else {
                    startProcess()
//                    Intent(this, MainActivity::class.java).apply {
//                        startActivity(this)
//                        finish()
//                    }
                }
            }
        }
    }

    private fun startProcess() {
        val id = getPref(this, "id")
        val pw = getPref(this, "pw")
        Trace.error("id = $id, pw = $pw, if() = ${!id.isNullOrEmpty() && !pw.isNullOrEmpty()}")
        if (!id.isNullOrEmpty() && !pw.isNullOrEmpty())
            mViewModel.login(id, pw)
        else
            Intent(this, MainActivity::class.java).apply {
                startActivity(this)
                finish()
            }
    }

    fun initObserve() {
        mViewModel.loginResponse.observe(this) {
            if (it.message.equals("true")) {
                NFT.instance.loginResponse = it
            }

            Intent(this, MainActivity::class.java).apply {
                startActivity(this)
                finish()
            }
        }

        mViewModel.loginFailure.observe(this) {
            Intent(this, MainActivity::class.java).apply {
                startActivity(this)
                finish()
            }
        }

        mViewModel.isblock.observe(this) {
            CommonDialog(
                DialogItem(
                    title = "사용자 정지",
                    content = "이상 거래가 탐지되어 해당 계정은 정지 처리되었습니다",
                    isCancel = false
                )
            ) {
                setPref(this, "id", "")
                setPref(this, "pw", "")
                Intent(this, MainActivity::class.java).apply {
                    startActivity(this)
                    finish()
                }
            }.show(supportFragmentManager, "")

        }
    }
}
