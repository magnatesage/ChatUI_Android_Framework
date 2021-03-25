package com.example.chatuilib.model

/**
 * This is data model class for splash screen configuration
 */
data class SplashScreenConfigModel(
    val orgName: String?,
    val orgNameFontType: String?,
    val orgNameFontSize: String?,
    val orgNameFontColor: String?,
    val cardBgDropShadow: Boolean,
    val bgShapeOfLogo: String?,
    val splashScreenBgType: String?,
    val splashScreenBgColor: String?,
    val splashScreenLogoImageUrl: String?,
    val splashScreenBgImageUrl: String?
)
