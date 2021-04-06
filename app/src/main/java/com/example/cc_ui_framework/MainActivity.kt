package com.example.cc_ui_framework

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import com.example.chatuilib.activity.ChatActivity
import com.example.chatuilib.customviews.CustomEditText
import com.example.chatuilib.listener.OnButtonClickListener
import com.example.chatuilib.model.MessageModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : ChatActivity() {

    private var et: CustomEditText? = null
    private var buttonTitleList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonTitleList.add("2001")
        buttonTitleList.add("2002")
        buttonTitleList.add("2003")
        buttonTitleList.add("2004")

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
        val todayDate = sdf.format(Date())
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -1)
        val yesterdayDate = sdf.format(cal.time)

        val chatScreen = ChatScreen(
            userCompanyId = "2",
            userApiUrl = "http://app-demo.core-chat.magnatesage.net/api/get-app-config"
        )
        chatScreen.apply {
            setTitle(getString(R.string.hp))
            addMessage(
                MessageModel(
                    data = "Welcome to Chatbot", isSender = false,
                    isCardView = false, cardViewHeader = "", date = "2021-03-31T11:55:53.004Z"
                )
            )
            Handler(Looper.getMainLooper()).postDelayed({
                addMessage(
                    MessageModel(
                        data = "Please Enter your Org Name", isSender = false,
                        isCardView = false, cardViewHeader = "", date = yesterdayDate
                    )
                )
            }, 3000)
        }

        et = chatScreen.getEditText()

        chatScreen.getSendButtonImageView().setOnClickListener {
            val etValue = et?.text.toString()
            et?.text?.clear()
            var time = ""
            if (!etValue.isNullOrBlank()) {
                time = if (etValue == "button") {
                    todayDate
                } else {
                    yesterdayDate
                }
                chatScreen.addMessage(
                    MessageModel(
                        etValue, isSender = true,
                        isCardView = false,
                        cardViewHeader = "", date = time
                    )
                )
            }
            when (etValue) {
                "ABC" -> {
                    chatScreen.addMessage(
                        MessageModel(
                            data = "Welcome $etValue", isSender = false,
                            isCardView = true, cardViewHeader = "", date = todayDate
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
                        isCardView = false, cardViewHeader = "", date = todayDate
                    )
                )
            }
        })

        chatScreen.getBackButton().setOnClickListener {
            this.finish()
        }

        chatScreen.apply {
            Log.e("CompanyId:: ", getCompanyId())
            Log.e("Title:: ", getTitle())
            Log.e("ChatListRecyclerview:: ", getChatListRecyclerview().toString())
            Log.e("ButtonListRecyview:: ", getButtonListRecyclerview().toString())
            Log.e("ChatButtonListAdptr:: ", getChatButtonListAdapter().toString())
            Log.e("getChatListAdapter:: ", getChatListAdapter().toString())
            Log.e("SendButtonImageView:: ", getSendButtonImageView().toString())
            Log.e("EditText:: ", getEditText().toString())
            Log.e("FlashButtonImageView:: ", getFlashButtonImageView().toString())
        }
    }
}