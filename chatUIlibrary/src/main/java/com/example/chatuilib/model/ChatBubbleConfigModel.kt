package com.example.chatuilib.model

/**
 * This is data model class for chat bubble configuration
 */
internal data class ChatBubbleConfigModel (
    val chatBubbleStyle : Int,
    val chatBotBgType: String?,
    val chatBotBgColor: String?,
    val chatBotBgImageUrl: String?,
    val senderChatBubbleColor: String?,
    val senderTextColor: String?,
    val receiverChatBubbleColor: String?,
    val receiverTextColor: String?,
    val cardBgDropShadow: Boolean
)