package com.rainbow.bird.utils

import android.content.res.Resources

object DensityUtil {
    fun dxToDp(resources: Resources, px: Int): Int =
        (px / resources.displayMetrics.density + 0.5f).toInt()
}