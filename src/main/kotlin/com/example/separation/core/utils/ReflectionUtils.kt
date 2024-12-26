package com.example.separation.core.utils

import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder

object ReflectionUtils {
    private val reflection: Reflections = Reflections(
        ConfigurationBuilder()
            .setUrls(ClasspathHelper.forJavaClassPath())
            .setScanners(Scanners.TypesAnnotated, Scanners.SubTypes)
    )

    fun scanAnnotation(annotationClazz: Class<out Annotation>): Set<Class<*>>? {
        return reflection.getTypesAnnotatedWith(annotationClazz)
    }

    fun <T> scanType(type: Class<T>): MutableSet<Class<out T>>? {
        return reflection.getSubTypesOf(type)
    }
}