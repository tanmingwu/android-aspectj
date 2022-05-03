package com.tmw.gradle.aspectj.utils

/**
 *  @author tanmingwu
 *  @since 2022/01/24
 */
object ListUtil {

    fun contain(tag: String, values: List<String>): Boolean {
        println("class = $tag")
        val result = values.map {
            it.replace(".", "/")
        }.firstOrNull {
            tag.contains(it)
        }
        return result != null
    }
}