package com.example.chatuilib.model

/**
 * This is data model class for button configuration
 */
internal data class ButtonConfigModel (
    val buttonShapeSelectedBg: Int,

    val normalButtonColor: String?,
    val normalTextColor: String?,
    val normalBorderColor: String?,
    val normalBorderSize: String?,

    val clickedButtonColor: String?,
    val clickedTextColor: String?,
    val clickedBorderColor: String?,
    val clickedBorderSize: String?,

    val normalIconColor: String?,
    val clickedIconColor: String?,
    val iconSize: String?,

    val buttonPlacementStyle: String?,
    val buttonBgDropShadow: Boolean,
    val buttonShapeSelectedId:Int
)