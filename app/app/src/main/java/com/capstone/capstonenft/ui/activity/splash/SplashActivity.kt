package com.capstone.capstonenft.ui.activity.splash

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import com.capstone.capstonenft.NFT
import com.capstone.capstonenft.R
import com.capstone.capstonenft.databinding.ActivitySplashBinding
import com.capstone.capstonenft.system.utils.getPref
import com.capstone.capstonenft.system.utils.setPref
import com.capstone.capstonenft.ui.activity.main.MainActivity
import com.capstone.capstonenft.viewmodel.login.LoginViewModel

class SplashActivity : AppCompatActivity() {
    lateinit var mBinding: ActivitySplashBinding

    val mViewModel: LoginViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        initObserve()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        checkPermission()
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

    }

    private fun checkPermission()
    {
        val internetPermission = ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)
        if(internetPermission==PackageManager.PERMISSION_GRANTED){
            startProcess()
        }
        else {
            requestPermission()
        }

    }

        private fun requestPermission(){
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.CAMERA),99)

        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            99->{
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    startProcess()
                }
                else{
//                        finish()
                    }
            }
        }
    }
    private fun startProcess(){
        val id = getPref(this,"id")
        val pw = getPref(this,"pw")
        if(!id.isNullOrEmpty()&&!pw.isNullOrEmpty())
            mViewModel.login(id,pw)
    }
    fun initObserve(){
       mViewModel.loginResponse.observe(this){
           if(it.message.equals("true")){
               NFT.instance.privatekey = it.privatekey
               NFT.instance.name = it.name
               }

           Intent(this,MainActivity::class.java).apply {
               startActivity(this)
           }
       }
    }
}
