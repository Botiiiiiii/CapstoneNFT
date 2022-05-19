package com.capstone.capstonenft.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.capstonenft.system.utils.Trace

open class BaseActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Trace.debug(">> onCreate()")
    }

    override fun onStart() {
        super.onStart()
        Trace.debug(">> onStart")
    }

    override fun onResume() {
        super.onResume()
        Trace.debug(">> onResume()")
    }

    override fun onPause() {
        super.onPause()
        Trace.debug(">> onPause()")
    }

    override fun onStop() {
        super.onStop()
        Trace.debug(">> onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Trace.debug(">> onDestroy()")
    }
}