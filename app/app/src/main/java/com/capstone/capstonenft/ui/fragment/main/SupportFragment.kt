package com.capstone.capstonenft.ui.fragment.main

import android.content.Intent
import android.os.Bundle
import android.os.Trace
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.capstone.capstonenft.R
import com.capstone.capstonenft.base.BaseFragment
import com.capstone.capstonenft.databinding.FragmentSupportBinding
import com.capstone.capstonenft.ui.activity.login.LoginActivity
import com.capstone.capstonenft.ui.activity.more.AboutActivity

class SupportFragment: BaseFragment() {
    lateinit var mBinding: FragmentSupportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    fun onClick(v: View) {
        when (v.id) {
            R.id.fs_about -> {
                Log.d("aaa","aa")
                val intent = Intent(mActivity, AboutActivity::class.java)
                startActivity(intent)
            }
            R.id.fs_logout->{

            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_support, container, false)
        mBinding.listener=this



        return mBinding.root
    }

}