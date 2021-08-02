package com.example.chatuilib.model

/**
 * This is data model class for cardview configuration
 */
data class CardViewConfigModel(
    val cardviewShapeId : Int,
    val cardviewBgDropShadow: Boolean,
    val cardviewBorderColor: String?,
    val cardviewBorderSize: String?,

    val cardviewHeaderBgColor: String?,
    val cardviewHeaderTextColor: String?,
    val cardviewHeaderTextSize: String?,

    val cardviewFooterButtonBgColor: String?,
    val cardviewFooterButtonTextColor: String?,
    val cardviewFooterButtonTextSize: String?
)