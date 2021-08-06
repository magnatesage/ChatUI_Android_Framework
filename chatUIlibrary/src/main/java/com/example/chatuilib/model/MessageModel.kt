package com.example.chatuilib.model

/**
 * This is data model class for chat list item
 */

data class MessageModel(
    val data: String?,
    val isSender: Boolean,
    val isCardView: Boolean,
    val cardViewHeader: String,
    val date: String,
    val senderName : String,
    val type : Int
){
    companion object{
        const val NOPE = 0
        const val BOT = 1
        const val WHISPER = 2
    }
}