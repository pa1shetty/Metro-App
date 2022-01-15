package com.example.nammametromvvm.utility.date

import com.example.nammametromvvm.utility.GeneralException
import com.example.nammametromvvm.utility.date.DateConstants.yyyymmddhhmmss
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateMethods {
    fun currentTimeInString(date: Date = Calendar.getInstance().time): String {
        val dateFormat: DateFormat = SimpleDateFormat(yyyymmddhhmmss, Locale.getDefault())
        return dateFormat.format(date)
    }

    fun stringToDate(dateInString: String, dateInStringFormat: String = yyyymmddhhmmss): Date {
        val sdf = SimpleDateFormat(dateInStringFormat, Locale.getDefault())
        return try {
            sdf.parse(dateInString)!!
        } catch (e: ParseException) {
            throw e.message?.let { GeneralException(it) }!!
        }
    }

}