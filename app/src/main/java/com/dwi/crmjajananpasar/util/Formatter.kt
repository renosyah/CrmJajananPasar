package com.dwi.crmjajananpasar.util

import java.text.DecimalFormat

class Formatter {
    companion object {
        val f = DecimalFormat("##,###")
        fun decimalFormat(i : Int) : String{
            return "Rp. ${f.format(i).toString()},-"
        }
    }
}