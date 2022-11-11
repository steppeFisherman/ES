package com.example.es.utils

interface FormatUiPhoneNumber {

    fun modify(initial: String): String

    class Base : FormatUiPhoneNumber {
        override fun modify(initial: String): String {
            if (initial.length == 12) {
                val index2 = 2
                val index6 = 6
                val index10 = 10
                val index13 = 13

                val charList = initial.toCharArray().toMutableList()
                charList.add(index2, '(')
                charList.add(index6, ')')
                charList.add(index10, ' ')
                charList.add(index13, ' ')
                val charArray = charList.toCharArray()
                charArray.joinToString("")

                return String(charArray)

            } else return initial
        }
    }
}