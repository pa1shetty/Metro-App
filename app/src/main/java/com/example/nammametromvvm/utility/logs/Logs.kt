package com.example.nammametromvvm.utility.logs

import com.example.nammametromvvm.utility.AppConstants
import com.example.nammametromvvm.utility.AppConstants.FILE_EXTENSION
import com.example.nammametromvvm.utility.AppConstants.MAX_FILE_SIZE
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
class Logs {
    //constants
    private var files2Retain = 10

    //variables
    private val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
    private var fileLogged = false
    private var fileName: String? = AppConstants.INTERNAL_LOG_PATH + File.separator + AppConstants.FILE_NAME
    private var logFile: TextFile? = null

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
        }
    }

    @Synchronized
    private fun log(message: String): String {
        init()
        if (fileLogged) {
            logFile!!.writeLine(message)
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