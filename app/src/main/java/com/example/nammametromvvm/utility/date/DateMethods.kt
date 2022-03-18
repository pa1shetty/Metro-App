package com.example.nammametromvvm.utility.date

import com.example.nammametromvvm.utility.GeneralException
import com.example.nammametromvvm.utility.date.DateMethods.DateConstants.date_format_only_time
import com.example.nammametromvvm.utility.date.DateMethods.DateConstants.date_format_previous_year
import com.example.nammametromvvm.utility.date.DateMethods.DateConstants.date_format_this_week
import com.example.nammametromvvm.utility.date.DateMethods.DateConstants.date_format_this_year
import com.example.nammametromvvm.utility.date.DateMethods.DateConstants.yyyymmddhhmmss
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateMethods {

    object DateConstants {
        const val yyyymmddhhmmss = "yyyymmddhhmmss"
        const val date_format_from_server = "dd-MM-yyyy HH:mm"
        const val date_format_only_time = "hh:mm a"
        const val date_format_this_week = "d EEE"
        const val date_format_this_year = "MMM d"
        const val date_format_previous_year = "MMM yyyy"
        const val date_format_dd_MMM_yyyy = "dd MMM yyyy"

    }

    enum class DateDifferenceTypeEnum(val DifferenceType: Int) {
        YEARS(0),
        DAYS(1),
        HOURS(2),
        MINUTES(3),
        SECONDS(4),
    }


    fun dateTimeInString(
        date: Date = Calendar.getInstance().time,
        dateFormat: String = yyyymmddhhmmss
    ): String {
        return SimpleDateFormat(dateFormat, Locale.getDefault()).format(date)
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
        d2: Date = Date(),
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

    private fun convertDateFormat(date: Date = Date(), requiredDateFormat: String): String {
        return SimpleDateFormat(requiredDateFormat, Locale.getDefault()).format(date)
    }

    fun convertDateFormat(
        originalDate: String,
        originalDateFormat: String,
        requiredDateFormat: String
    ): String {
        return convertDateFormat(stringToDate(originalDate, originalDateFormat), requiredDateFormat)
    }


    fun convertDateFormatDynamic(originalDateString: String, originalDateFormat: String): String {
        val originalDate = stringToDate(originalDateString, originalDateFormat)
        val requiredDateFormat: String
        when {
            findDifferenceBetweenDates(
                originalDate,
                differenceType = DateDifferenceTypeEnum.YEARS.DifferenceType
            ) > 1 -> {
                requiredDateFormat = date_format_previous_year
            }
            findDifferenceBetweenDates(
                originalDate,
                differenceType = DateDifferenceTypeEnum.DAYS.DifferenceType
            ) > 7 -> {
                requiredDateFormat = date_format_this_year
            }
            findDifferenceBetweenDates(
                originalDate,
                differenceType = DateDifferenceTypeEnum.DAYS.DifferenceType
            ) > 1 -> {
                requiredDateFormat = date_format_this_week
            }
            else -> {
                requiredDateFormat = date_format_only_time
            }
        }
        return convertDateFormat(
            stringToDate(originalDateString, originalDateFormat),
            requiredDateFormat
        )
    }

    fun dateTimeInMilliSecond(dateInString: String, requiredDateFormat: String): Long {
        return SimpleDateFormat(requiredDateFormat, Locale.getDefault()).parse(dateInString)!!.time
    }

    fun dateTimeInMilliSecond(dateInString: Date = Date()): Long {
        return dateInString.time
    }

    private fun longToDate(dateLong: Long): Date {
        return Date(dateLong)
    }

    fun longToFormattedDate(dateLong: Long, dateFormat: String): String {
        return convertDateFormat(longToDate(dateLong), dateFormat)
    }

}