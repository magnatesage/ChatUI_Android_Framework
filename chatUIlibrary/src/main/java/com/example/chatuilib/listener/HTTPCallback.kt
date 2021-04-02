package com.example.chatuilib.listener

internal interface HTTPCallback {
    fun onSuccessResponse(output: String)
    fun onErrorResponse(responseCode: Int, output: String)
}