package com.capstone.capstonenft.system.utils

import android.util.DisplayMetrics
import com.capstone.capstonenft.base.BaseActivity

class PixelUtil {
    companion object {
        fun dpToPx(activity: BaseActivity, dp: Float): Int {
            val outMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(outMetrics)
            return (dp * outMetrics.density).toInt()
        }
    }
}