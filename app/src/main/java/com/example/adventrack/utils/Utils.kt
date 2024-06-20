package com.example.adventrack.utils

import java.text.NumberFormat
import java.util.Locale

fun stringIDRToInteger(priceString: String): Int {
    val cleanedString = priceString.replace("[^\\d.]".toRegex(), "").replace(".", "")

    return cleanedString.toInt()
}

fun String.withCurrencyFormat(): String {
    val mCurrencyFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return mCurrencyFormat.format(this.toDouble())
}
