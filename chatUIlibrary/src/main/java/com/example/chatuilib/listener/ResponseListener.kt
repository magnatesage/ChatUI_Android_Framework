package com.example.chatuilib.listener

import com.example.chatuilib.model.DataModel

/**
 * This interface is used to notify API response
 */
interface ResponseListener {
    fun onSuccessResponse(
        dataModel: DataModel?
    )

    fun onErrorResponse(
        message: Any?
    )
}