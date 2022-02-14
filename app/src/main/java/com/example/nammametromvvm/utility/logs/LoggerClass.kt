package com.example.nammametromvvm.utility.logs

import android.util.Log
import java.io.PrintWriter
import java.io.StringWriter
import javax.inject.Inject


/**
 * Created by USER on 02-07-2020.
 */
class LoggerClass  @Inject constructor(private val logs: Logs) {
    @Suppress("unused")
    fun info(text: String) {
        val fullClassName = Thread.currentThread().stackTrace[3].className
        val className = fullClassName.substring(fullClassName.lastIndexOf('.') + 1)
        val methodName = Thread.currentThread().stackTrace[3].methodName
        val lineNumber = Thread.currentThread().stackTrace[3].lineNumber
        val msgMetaData =
            "##" + Thread.currentThread() + "## " + className + "." + methodName + "() ## ln " + lineNumber + " ## msg => " + text
        Log.e("INFO", msgMetaData)
        logs.info(msgMetaData)
    }

    @Synchronized
    fun error(exception: Exception) {
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        exception.printStackTrace(pw)
        val msg = sw.toString()
        val fullClassName = Thread.currentThread().stackTrace[3].className
        val className = fullClassName.substring(fullClassName.lastIndexOf('.') + 1)
        val methodName = Thread.currentThread().stackTrace[3].methodName
        val lineNumber = Thread.currentThread().stackTrace[3].lineNumber
        val msgMetaData =
            "##" + Thread.currentThread() + "## " + className + "." + methodName + "() ## ln " + lineNumber + " ## msg => " + msg
        Log.e("Error", msgMetaData)
        logs.error(msgMetaData)
    }
}