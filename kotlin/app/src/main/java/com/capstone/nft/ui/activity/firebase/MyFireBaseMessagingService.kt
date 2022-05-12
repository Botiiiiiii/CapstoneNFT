package com.capstone.nft.ui.activity.firebase

import android.content.Context
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFireBaseMessagingService : FirebaseMessagingService(){
    private val TAG = "FirebaseService"
    override fun onNewToken(token: String) {
        Log.d(TAG,"new Token: $token")
        val pref = this.getSharedPreferences("token", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("token",token).apply()
        editor.commit()
        Log.i("로그: ","성공적으로 토큰을 저장함")
    }
    override  fun onMessageRecived(remoteMessage: RemoteMessage?){
        Log.d(TAG, "From: "+ remoteMessage!!.from)
        if(remoteMessage.data.isNotEmpty()){
            Log.i("바디",remoteMessage.data["body"].toString())
            Log.i("타이틀",remoteMessage.data["title"].toString())
            sendNotification(remoteMessage)

        }
    }
    private fun sendNotification(remoteMessage: RemoteMessage){

    }
}