package com.example.es.utils

interface FormatUiPhoneNumber {

    fun modify(initial: String): String

    class Base : FormatUiPhoneNumber {
        override fun modify(initial: String): String {
            if (initial.length == 12) {
                val index2 = 2
                val index6 = 6
                val index7 = 7
                val index11 = 11
                val index14 = 14

                val charList = initial.toCharArray().toMutableList()
                charList.add(index2, '(')
                charList.add(index6, ')')
                charList.add(index7, ' ')
                charList.add(index11, ' ')
                charList.add(index14, ' ')
                val charArray = charList.toCharArray()
                charArray.joinToString("")

                return String(charArray)

            } else return initial
        }
    }
}