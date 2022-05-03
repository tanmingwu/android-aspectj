package com.tmw.gradle.aspectj.utils

import com.android.build.api.transform.JarInput
import java.io.IOException
import java.util.jar.JarEntry
import java.util.jar.JarFile

/**
 * Aspect默认过滤
 *
 * @author tanmingwu
 * @since 2021/09/15
 */
object FilterUtil {

    private val CLASS_FILTERS: MutableList<String> = arrayListOf(
        "R.jar"
    )

    private fun isFilter(name: String): Boolean {
        return CLASS_FILTERS.contains(name)
    }

    fun isExclude(jarInput: JarInput, excludes: List<String>): Boolean {
        try {
            val firstClass = getFirstClass(jarInput)
            firstClass ?: return false
            if (isFilter(firstClass.name)) {
                return true
            }
            return ListUtil.contain(firstClass.name, excludes)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }

    fun isInclude(jarInput: JarInput, includes: List<String>): Boolean {
        try {
            if (isFilter(jarInput.file.name)) {
                return false
            }

            val firstClass = getFirstClass(jarInput)
            firstClass ?: return false
            return ListUtil.contain(firstClass.name, includes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    private fun getFirstClass(jarInput: JarInput): JarEntry? {
        if (!jarInput.file.absolutePath.endsWith(".jar")) {
            return null
        }
        val jarFile = JarFile(jarInput.file)
        val enumeration = jarFile.entries()
        if (!enumeration.hasMoreElements()) {
            return null
        }
        var first = enumeration.nextElement()
        while (!first.name.endsWith(".class") && enumeration.hasMoreElements()) {
            first = enumeration.nextElement()
        }
        return first
    }

    fun checkAspectPath(jarInput: JarInput): Boolean {
        val firstClass = getFirstClass(jarInput)
        firstClass ?: return false
        if (firstClass.name.contains("com/example/baselibrary")) {
            return true
        }
        return false
    }
}