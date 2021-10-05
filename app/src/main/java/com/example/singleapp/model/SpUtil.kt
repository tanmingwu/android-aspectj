package com.example.singleapp.model

import com.tencent.mmkv.MMKV

class SpUtil {

    @JvmOverloads
    fun getString(key: String, default: String? = null) {
        MMKV.defaultMMKV().getString(key, default)
    }

    fun putString(key: String, value: String) {
        MMKV.defaultMMKV().encode(key, value)
    }
}