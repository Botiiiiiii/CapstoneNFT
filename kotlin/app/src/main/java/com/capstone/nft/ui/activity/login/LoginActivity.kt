package com.capstone.nft.ui.activity.login

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.capstone.nft.R
import com.capstone.nft.databinding.ActivityLoginBinding
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    val TAG: String = "LoginActivity"
    lateinit var mBinding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        mBinding.alLlLogin.setOnClickListener {
            val uri = "intent://klipwallet/open?url=https://klipwallet.com/?target=/a2a?request_key=0b0ee0ad-62b3-4146-980b-531b3201265d#Intent;scheme=kakaotalk;package=com.kakao.talk;end"
            val intent = Intent.parseUri(uri, Intent.URI_INTENT_SCHEME);
            val existPackage = packageManager.getLaunchIntentForPackage(intent.getPackage()!!)
            if (existPackage != null) {
                startActivity(intent)
            } else {
                var marketIntent = Intent(Intent.ACTION_VIEW)
                marketIntent.setData(Uri.parse("market://details?id=com.kakao.talk"))
                startActivity(marketIntent)
            }
        }
    }

}