package com.bintangpoetra.sumbanginaja.utils.ext

import android.text.TextUtils
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody


fun emptyString(): String = ""

fun String.isEmailValid(): Boolean  {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.toBearer(): String {
    return "Bearer $this"
}

fun String.toRequestBody(): RequestBody {
    return this.toRequestBody("multipart/form-data".toMediaTypeOrNull())
}

fun String.isTheFoodOwner(id: String): Boolean {
    return this == id
}