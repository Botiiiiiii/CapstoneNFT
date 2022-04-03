package com.capstone.nft.ui.activity.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.nft.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity :AppCompatActivity(){

    val TAG:String="Register"
    var isExistBlank = false
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_register)

        al_btn_register.setOnClickListener{
            Log.d(TAG,"회원가입 버튼 클릭")
            val id = ar_et_id.text.toString()
            val pw = ar_et_pw.text.toString()
            if(id.isEmpty()||pw.isEmpty()) {
                isExistBlank =true
            }
            if(!isExistBlank){
                Toast.makeText(this,"회원가입 성공",Toast.LENGTH_LONG).show()

            val sharedPreference=getSharedPreferences("file name", Context.MODE_PRIVATE)
            val editor = sharedPreference.edit()
            editor.putString("id",id)
            editor.putString("pw",pw)
            editor.apply()

            val intent= Intent(this,LoginActivity::class.java)
            startActivity(intent)
            }

    }
    }
}