package com.example.chatuilib.model

/**
 * This is data model class for chat list item
 */

data class MessageModel(
    val data: String?,
    val isSender: Boolean,
    val isCardView: Boolean,
    val cardViewHeader: String
)