package com.tmw.gradle.aspectj.base

import org.gradle.api.Plugin
import org.gradle.api.Project

inline fun <reified T> Project.getExtension(): T {
    return extensions.getByType(T::class.java)
}

inline fun <reified T> Project.findExtension(): T? {
    return extensions.findByType(T::class.java)
}

inline fun <reified T : Plugin<*>> Project.hasPlugin(): Boolean {
    return getPlugin<T>() != null
}

inline fun <reified T : Plugin<*>> Project.getPlugin(): T? {
    return plugins.findPlugin(T::class.java)
}