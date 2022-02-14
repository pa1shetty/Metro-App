package com.example.nammametromvvm.utility.logs

import java.io.*


/**
 * Created by USER on 02-07-2020.
 */
class TextFile(fileName: String) {
    private var opened = false
    private var fileName = ""
    private var file: File? = null
    private var `in`: BufferedReader? = null
    private var out: BufferedWriter? = null
    private var fileOperation = 0
    private var curPos = 0
    private val aline: MutableList<String> = ArrayList()
    @Suppress("unused")
    val totalLines: Int
        get() = aline.size

    @Throws(Throwable::class)
    protected fun finalize() {
        close()

    }

    fun close() {
        try {
            `in`!!.close()
        } catch (ex: Exception) {
        }
        try {
            out!!.close()
        } catch (ex: Exception) {
        }
    }

    //0=read,1=overwrite,2=append
    @Throws(Exception::class)
    fun open(fileOperation: Int): Boolean {
        curPos = 0
        this.fileOperation = fileOperation
        opened = false
        file = File(fileName)
        val folder = File(file!!.absolutePath.replace(file!!.name.toRegex(), ""))
        folder.mkdirs()
        when (fileOperation) {
            READ -> {
                `in` = BufferedReader(FileReader(file))
                toList()
            }
            OVERWRITE -> out = BufferedWriter(FileWriter(file))
            APPEND -> out = BufferedWriter(FileWriter(file, true))
            else -> {}
        }
        opened = true
        return opened
    }

    @Throws(Exception::class)
    private fun toList() {
        aline.clear()
        var line: String
        while (`in`!!.readLine().also { line = it } != null) {
            aline.add(line)
        }
    }
    @Suppress("unused")
    fun prevLine(): String? {
        return if (curPos > 1) aline[--curPos - 1] else null
    }
    @Suppress("unused")
    fun nextLine(): String? {
        return if (curPos < aline.size) aline[++curPos - 1] else null
    }
    @Suppress("unused")
    fun curLine(): String? {
        return if (curPos > 0 && curPos <= aline.size) aline[curPos - 1] else null
    }
    @Suppress("unused")
    fun getLine(lineNumber: Int): String? {
        return if (lineNumber > 0 && lineNumber <= aline.size) aline[lineNumber - 1] else null
    }

    @Throws(Exception::class)
    fun writeLine(text: String?) {
        out!!.write(text)
        out!!.newLine()
        out!!.flush()
    }

    fun size(): Long {
        return file!!.length()
    }

    companion object {
        const val READ = 0
        const val OVERWRITE = 1
        const val APPEND = 2
    }

    init {
        this.fileName = fileName
    }
}