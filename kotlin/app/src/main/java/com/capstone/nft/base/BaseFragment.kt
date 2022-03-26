package com.capstone.nft.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.capstone.nft.system.utils.Trace

open class BaseFragment: Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Trace.debug(">> onCreate()")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Trace.debug(">> onCreateView()")
        return super.onCreateView(inflater, container, savedInstanceState)
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

    override fun onDestroyView() {
        super.onDestroyView()
        Trace.debug(">> onDestroy()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Trace.debug(">> onDestroy()")
    }
}