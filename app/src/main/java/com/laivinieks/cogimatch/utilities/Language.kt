package com.laivinieks.cogimatch.utilities


import com.laivinieks.cogimatch.R

enum class Language(val language: Int, val value: String) {
    TURKISH(R.string.turkish, "tr"),
    ENGLISH(R.string.english, "en"),
    DEFAULT(R.string.system, "sys")
}