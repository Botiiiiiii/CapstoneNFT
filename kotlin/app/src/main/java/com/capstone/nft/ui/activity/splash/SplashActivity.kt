package com.capstone.nft.ui.activity.splash

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.capstone.nft.R
import com.capstone.nft.databinding.ActivitySplashBinding
import com.capstone.nft.base.BaseActivity
import com.capstone.nft.ui.activity.main.MainActivity
import kotlinx.coroutines.*

class SplashActivity : BaseActivity() {
    lateinit var mBinding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        CoroutineScope(Dispatchers.Main).launch {
            var time = 0
            while (time < 3) {
                delay(1000L)
                time++

                if (time >= 3) {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }
            }
        }
    }
}