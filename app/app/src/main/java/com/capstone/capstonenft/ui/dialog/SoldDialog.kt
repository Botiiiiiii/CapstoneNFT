package com.capstone.capstonenft.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity.CENTER
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.capstone.capstonenft.R
import com.capstone.capstonenft.databinding.DialofSoldBinding
import com.capstone.capstonenft.databinding.DialogCommonBinding
import com.capstone.capstonenft.dto.DialogItem

class SoldDialog(val okListener: ((String) -> Unit)): DialogFragment() {
    lateinit var mBinding:DialofSoldBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialof_sold, container, false)

        mBinding.dsBtnCancle.setOnClickListener {
            dismiss()
        }

        mBinding.dsBtnOk.setOnClickListener {
            if(mBinding.dsEtPrice.text.toString().isNullOrEmpty())
                return@setOnClickListener

            okListener(mBinding.dsEtPrice.text.toString())
            dismiss()
        }

        return mBinding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return super.onCreateDialog(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog?.window?.setGravity(CENTER)
    }
}