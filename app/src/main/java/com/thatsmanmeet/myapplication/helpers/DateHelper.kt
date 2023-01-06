package com.thatsmanmeet.myapplication.helpers

import java.time.LocalDate

class DateHelper {
    fun getCurrentDate(): String{
        val currentDate = LocalDate.now().toString()
        val splitStr = currentDate.split("-")
        val day = splitStr[2]
        val year = splitStr[0]
        val month = when(splitStr[1]){
            "01" -> "Jan"
            "02" -> "Feb"
            "03" -> "Mar"
            "04" -> "Apr"
            "05" -> "May"
            "06" -> "June"
            "07" -> "July"
            "08" -> "Aug"
            "09" -> "Sep"
            "10" -> "Oct"
            "11" -> "Nov"
            "12" -> "Dec"
            else -> {"undefined"}
        }
        return "$day $month, $year"
    }
}