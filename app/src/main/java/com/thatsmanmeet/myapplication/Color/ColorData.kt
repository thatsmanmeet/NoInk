package com.thatsmanmeet.myapplication.Color

import com.thatsmanmeet.myapplication.R

open class ColorData {
    val colors = mutableListOf<ColorObject>(
        ColorObject("Blue", R.color.blue_default),
        ColorObject("Red",R.color.red),
        ColorObject("Yellow",R.color.yellow),
        ColorObject("Green",R.color.green_bg),
        ColorObject("Purple",R.color.purple_bg),
        ColorObject("Pink",R.color.pink_bg)
        )
}