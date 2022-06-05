package com.capstone.capstonenft.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.capstone.capstonenft.R
import com.capstone.capstonenft.databinding.DialogCommonBinding
import com.capstone.capstonenft.dto.DialogItem

class CommonDialog(val item: DialogItem, val okListener: ((Boolean) -> Unit)): DialogFragment() {
    lateinit var mBinding:DialogCommonBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_common, container, false)
        mBinding.dialogItem = item

        mBinding.dcBtnOk.setOnClickListener {
            okListener(true)
            dismiss()
        }

        mBinding.dcBtnCancel.setOnClickListener {
            dismiss()
        }

        return mBinding.root
    }
}