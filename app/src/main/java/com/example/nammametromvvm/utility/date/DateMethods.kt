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

    fun findDifferenceBetweenDates(
        d1: Date,
        d2: Date,
        differenceType: Int = DateDifferenceTypeEnum.HOURS.DifferenceType
    ): Long {
        val differenceInTime: Long = d2.time - d1.time
        val differenceInSeconds = ((differenceInTime
                / 1000)
                % 60)
        val differenceInMinutes = ((differenceInTime
                / (1000 * 60))
                % 60)
        val differenceInHours = ((differenceInTime
                / (1000 * 60 * 60))
                % 24)
        val differenceInYears = (differenceInTime
                / (1000L * 60 * 60 * 24 * 365))
        val differenceInDays = ((differenceInTime
                / (1000 * 60 * 60 * 24))
                % 365)
        return when (differenceType) {
            DateDifferenceTypeEnum.YEARS.DifferenceType -> differenceInYears
            DateDifferenceTypeEnum.DAYS.DifferenceType -> differenceInDays
            DateDifferenceTypeEnum.HOURS.DifferenceType -> differenceInHours
            DateDifferenceTypeEnum.MINUTES.DifferenceType -> differenceInMinutes
            DateDifferenceTypeEnum.SECONDS.DifferenceType -> differenceInSeconds
            else -> {
                differenceInHours
            }
        }
    }
}