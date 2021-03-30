package com.example.chatuilib.listener

interface HTTPCallback {
    fun onSuccessResponse(output: String)
    fun onErrorResponse(responseCode: Int, output: String)
}