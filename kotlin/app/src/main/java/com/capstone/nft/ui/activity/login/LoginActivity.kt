package com.capstone.nft.ui.activity.login

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.capstone.nft.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity:AppCompatActivity() {
    val TAG : String="LoginActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        al_btn_login.setOnClickListener{
            var id = al_et_id.text.toString()
            var pw = al_et_pw.text.toString()

            val sharedPreference = getSharedPreferences("file name", Context.MODE_PRIVATE)
            val savedId= sharedPreference.getString("id","")
            val savedPw = sharedPreference.getString("pw","")

            if(id==savedId&&pw==savedPw){
                dialog("Success")
            }
        }



    }

    private fun dialog(type: String) {
        var dialog = AlertDialog.Builder(this)
        if(type.equals("Success")){
            dialog.setTitle("로그인 성공")
            dialog.setMessage("로그인 성공 !")
        }
        else if(type.equals("fail")){
            dialog.setTitle("로그인 실패")
            dialog.setMessage("로그인 실패 !")
        }

        var dialog_listener = object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    DialogInterface.BUTTON_POSITIVE-> Log.d(TAG,"")
                }
            }
        }
        dialog.setPositiveButton("확인",dialog_listener)
        dialog.show()
    }

}