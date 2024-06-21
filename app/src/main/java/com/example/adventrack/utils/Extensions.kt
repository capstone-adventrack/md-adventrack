package com.example.adventrack.utils

import com.example.adventrack.utils.DateConstant.DATE_MONTH_DAY_YEAR_FORMAT
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun Long.convertMillisToDateString(): String {
    val formatter = SimpleDateFormat(
        DATE_MONTH_DAY_YEAR_FORMAT,
        Locale.getDefault()
    )
    formatter.timeZone = TimeZone.getDefault()
    val date = Date(this)
    return formatter.format(date)
}