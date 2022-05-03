package com.tmw.gradle.aspectj.base

import org.gradle.api.plugins.JavaPlugin
import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.implementation(dependencyNotation: Any) {
    add(JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME, dependencyNotation)
}

fun DependencyHandlerScope.api(dependencyNotation: Any) {
    add(JavaPlugin.API_CONFIGURATION_NAME, dependencyNotation)
}

fun DependencyHandlerScope.kapt(dependencyNotation: Any) {
    add("kapt", dependencyNotation)
}