package com.example.singleapp.model

import com.tencent.mmkv.MMKV
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SharedPref<T>(
    private val key: String,
    private val defValue: T
) : ReadWriteProperty<Any?, T> {

    private val prefs by lazy {
        MMKV.defaultMMKV()
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T =
        findPreference(findProperName(property))

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) =
        putPreference(findProperName(property), value)

    private fun findProperName(property: KProperty<*>) = if (key.isEmpty()) property.name else key

    @Suppress("UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY")
    private fun findPreference(key: String): T = when (defValue) {
        is Int -> prefs.getInt(key, defValue)
        is Long -> prefs.getLong(key, defValue)
        is Float -> prefs.getFloat(key, defValue)
        is Boolean -> prefs.getBoolean(key, defValue)
        is String -> prefs.getString(key, defValue)
        else -> throw IllegalArgumentException("Unsupported type.")
    } as T

    private fun putPreference(key: String, value: T) {
        val edit = prefs.apply {
            when (value) {
                is Int -> encode(key, value)
                is Long -> encode(key, value)
                is Float -> encode(key, value)
                is Boolean -> encode(key, value)
                is String -> encode(key, value)
                else -> throw IllegalArgumentException("Unsupported type.")
            }
        }
        edit.apply()
    }
}