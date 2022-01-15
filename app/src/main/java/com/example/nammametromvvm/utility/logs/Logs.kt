package com.example.nammametromvvm.utility.logs

import android.annotation.TargetApi
import android.os.Build
import com.example.nammametromvvm.utility.AppConstants.FILE_EXTENSION
import com.example.nammametromvvm.utility.AppConstants.MAX_FILE_SIZE
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.*

/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/


/**
 * Created by USER on 02-07-2020.
 */
class Logs(override val kodein: Kodein) :   KodeinAware {
    //constants
    private var files2Retain = 10
    private val logs: Logs by instance()
    private val loggerClass: LoggerClass by instance()

    //variables
    private val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS",Locale.getDefault())
    private var fileLogged = false
    private var fileName: String? = null
    private var logFile: TextFile? = null
    fun toFile(fileName: String?, files2Retain: Int) {
        this.fileName = fileName
        this.files2Retain = files2Retain
        init()
    }

    @Throws(Throwable::class)
    protected fun finalize() {
        shutdown()
    }

    @Synchronized
    fun shutdown() {
        if (fileLogged) {
            fileLogged = false
            logFile!!.close()
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    @Synchronized
    fun init() {
        try {
            if (fileLogged && logFile!!.size() > MAX_FILE_SIZE) {
                //size exceeded
                shutdown()
                //retain logs by deleting the oldest and renaming the rest
                var fileFrom: File
                var fileTo: File
                for (i in files2Retain downTo 0) {
                    fileFrom = File(
                        fileName + "_" + String.format("%2s", i).replace(" ", "0") + FILE_EXTENSION
                    )
                    //pathFrom = FileSystems.getDefault().getPath(fileFrom);
                    if (fileFrom.exists()) {
                        if (i == files2Retain) {
                            //Files.delete(pathFrom);
                            fileFrom.delete()
                        } else {
                            fileTo = File(
                                fileName + "_" + String.format("%2s", i + 1)
                                    .replace(" ", "0") + FILE_EXTENSION
                            )
                            //Files.move(pathFrom, pathFrom.resolveSibling(fileTo));
                            fileFrom.renameTo(fileTo)
                        }
                    }
                }
            }
            if (!fileLogged && fileName != null) {
                logFile = TextFile(fileName + "_00" + FILE_EXTENSION)
                logFile!!.open(TextFile.APPEND)
                fileLogged = true
            }
        } catch (ex: Exception) {
            warn(ex.toString())
            loggerClass.error(ex);
        }
    }

    @Synchronized
    private fun log(message: String): String {
        try {
            init()
            if (fileLogged) {
                logFile!!.writeLine(message)
            }
            //System.out.println(message);
        } catch (ex: Exception) {
            loggerClass.error(ex)
        }
        return message
    }

    @Synchronized
    fun info(message: String): String {
        return log(sdf.format(Date()) + ", INFO, " + message)
    }

    @Synchronized
    fun warn(message: String): String {
        return log(sdf.format(Date()) + ", WARN, " + message)
    }

    @Synchronized
    fun error(message: String): String {
        return log(sdf.format(Date()) + ", ERROR, " + message)
    }

    @Synchronized
    fun error(message: String?, e: Exception): String {
        val errors = StringWriter()
        e.printStackTrace(PrintWriter(errors))
        return log(sdf.format(Date()) + ", Exception, " + errors.toString())
    }
}