package com.capstone.nft.system.utils

import android.app.Activity
import android.util.DisplayMetrics
import com.capstone.nft.base.BaseActivity

class PixelUtil {
    companion object {
        fun dpToPx(activity: BaseActivity, dp: Float): Int {
            val outMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(outMetrics)
            return (dp * outMetrics.density).toInt()
        }
    }
}