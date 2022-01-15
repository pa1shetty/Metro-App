package com.example.nammametromvvm.utility.logs

import android.util.Log
import com.example.nammametromvvm.utility.AppConstants.logs
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.Exception


/**
 * Created by USER on 02-07-2020.
 */
class LoggerClass(override val kodein: Kodein) : KodeinAware {
    private val logs: Logs by instance()

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