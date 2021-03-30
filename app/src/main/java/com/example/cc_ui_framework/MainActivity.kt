package com.example.cc_ui_framework

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.chatuilib.activity.ChatActivity
import com.example.chatuilib.customviews.CustomEditText
import com.example.chatuilib.listener.OnButtonClickListener
import com.example.chatuilib.model.MessageModel
import com.example.chatuilib.utils.AppConstants
import com.example.chatuilib.utils.AppLog

class MainActivity : ChatActivity() {

    private var et: CustomEditText? = null
    private var buttonTitleList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        buttonTitleList.add("2001")
        buttonTitleList.add("2002")
        buttonTitleList.add("2003")
        buttonTitleList.add("2004")


        val chatScreen = ChatScreen(userCompanyId = "2", baseUrl = AppConstants.BASE_URL)
        chatScreen.apply {
            setTitle(getString(R.string.hp))
            addMessage(
                MessageModel(
                    data = "Welcome to Chatbot", isSender = false,
                    isCardView = false, cardViewHeader = ""
                )
            )
            Handler(Looper.getMainLooper()).postDelayed({
                addMessage(
                    MessageModel(
                        data = "Please Enter your Org Name", isSender = false,
                        isCardView = false, cardViewHeader = ""
                    )
                )
            }, 3000)
        }

        et = chatScreen.getEditText()

        chatScreen.getSendButtonImageView().setOnClickListener {
            val etValue = et?.text.toString()
            et?.text?.clear()
            if (!etValue.isNullOrBlank()) {
                chatScreen.addMessage(
                    MessageModel(
                        etValue, isSender = true,
                        isCardView = false,
                        cardViewHeader = ""
                    )
                )
            }
            when (etValue) {
                "ABC" -> {
                    chatScreen.addMessage(
                        MessageModel(
                            data = "Welcome $etValue", isSender = false,
                            isCardView = false, cardViewHeader = ""
                        )
                    )
                }

                "button" -> {
                    chatScreen.addButtonList(buttonTitleList)
                }
            }
        }

        chatScreen.setButtonListClickListener(object : OnButtonClickListener {
            override fun onButtonClick(buttonTitle: String) {
                chatScreen.addMessage(
                    MessageModel(
                        data = buttonTitle, isSender = true,
                        isCardView = false, cardViewHeader = ""
                    )
                )
            }
        })


        chatScreen.apply {
            AppLog.e("getCompanyId:: ", getCompanyId())
            AppLog.e("getTitle:: ", getTitle())
            AppLog.e("getChatListRecyclerview:: ", getChatListRecyclerview().toString())
            AppLog.e("getButtonListRecyclerview:: ", getButtonListRecyclerview().toString())
            AppLog.e("getChatButtonListAdapter:: ", getChatButtonListAdapter().toString())
            AppLog.e("getChatListAdapter:: ", getChatListAdapter().toString())
            AppLog.e("getSendButtonImageView:: ", getSendButtonImageView().toString())
            AppLog.e("getEditText:: ", getEditText().toString())
            AppLog.e("getFlashButtonImageView:: ", getFlashButtonImageView().toString())
        }
    }
}