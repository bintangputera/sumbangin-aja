package com.bintangpoetra.sumbanginaja.utils.ext

import android.text.TextUtils

fun emptyString(): String = ""

fun String.isEmailValid(): Boolean  {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}