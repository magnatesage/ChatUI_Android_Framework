package com.example.chatuilib.model

import com.google.gson.annotations.SerializedName

data class ResponseModel(
    val status: String?,
    val status_code: Int?,
    val data: DataModel?
)

class DataModel {
    @SerializedName("Splash Screen Configuration")
    var splashScreenConfigModel: SplashScreenConfigModel? = null

    @SerializedName("Font Setting")
    var fontSettingModel: FontSettingModel? = null

    @SerializedName("Header & Tab Bar")
    var headerTabBarModel: HeaderTabBarModel? = null

    @SerializedName("Chat Bubble")
    var chatBubbleConfigModel: ChatBubbleConfigModel? = null

    @SerializedName("Button")
    var buttonConfigModel: ButtonConfigModel? = null

    @SerializedName("Card View")
    var cardViewConfigModel: CardViewConfigModel? = null

    @SerializedName("Conversation Bar Styling")
    var conversationBarConfigModel: ConversationBarConfigModel? = null
}
