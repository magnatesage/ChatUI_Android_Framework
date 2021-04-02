package com.example.chatuilib.model

/**
 * This is data model class for font configuration
 */
internal data class FontSettingModel(
    val headingFontType: String,
    val headingFontSize: String,
    val headingFontColor: String,

    val subHeadingFontType: String,
    val subHeadingFontSize: String,
    val subHeadingFontColor: String,

    val contentFontType: String,
    val contentFontSize: String,
    val contentFontColor: String
)