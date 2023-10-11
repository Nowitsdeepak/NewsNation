package com.app.newsnation.utils

object CountryCode {
    private var code = "in"
    val country get() = code
    
    fun setCode(country: String): String {
        if (country.length == 2) {
            code = country
        }
        return "invalid country code"
    }

//     TODO: Future Implementation
}