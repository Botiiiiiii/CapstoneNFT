package com.capstone.nft.ui.activity.splash

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.capstone.nft.R
import com.capstone.nft.base.BaseActivity
import com.capstone.nft.databinding.ActivitySplashBinding
import com.capstone.nft.ui.activity.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    lateinit var mBinding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
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
                        finish()
                    }
            }
        }
    }
    private fun startProcess(){
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finish()
    }
}
