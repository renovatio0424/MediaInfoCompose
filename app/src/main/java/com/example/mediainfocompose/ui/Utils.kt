package com.example.mediainfocompose.ui

import java.text.DecimalFormat
import kotlin.math.log10
import kotlin.math.pow

fun Int.toReadableByteString(): String {
    val size = this.toLong()
    if (size <= 0) return "0"
    val units = arrayOf("B", "kB", "MB", "GB", "TB")
    val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
    return DecimalFormat("#,##0.#").format(size / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
}