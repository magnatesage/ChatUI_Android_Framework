package com.example.cc_ui_framework

import com.example.chatuilib.activity.ChatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.example.chatuilib.customviews.CustomEditText
import com.example.chatuilib.listener.OnBottomMenuItemClickListener
import com.example.chatuilib.listener.OnButtonClickListener
import com.example.chatuilib.listener.OnTopMenuItemClickListener
import com.example.chatuilib.model.MessageModel
import com.example.chatuilib.model.UserModel
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : ChatActivity() {

    private var et: CustomEditText? = null
    private var buttonTitleList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonTitleList.add("Button 1")
        buttonTitleList.add("Button 2")
        buttonTitleList.add("Button 3")
        buttonTitleList.add("Button 4")

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
        val todayDate = sdf.format(Date())
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -1)
        val yesterdayDate = sdf.format(cal.time)

        var json = ""
        try {
            val inputStream = assets.open("default.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, StandardCharsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        val jsonObject = JSONObject(json)

        val chatScreen = ChatScreen(
            jsonObject = jsonObject
        )
        chatScreen.apply {
            setTitle(getString(R.string.hp))
            addMessage(
                MessageModel(
                    data = "Welcome to Chatbot", isSender = false,
                    isCardView = false, cardViewHeader = "",
                    date = "2021-03-31T11:55:53.004Z", senderName = "Test User",MessageModel.BOT
                )
            )
            Handler(Looper.getMainLooper()).postDelayed({
                addMessage(
                    MessageModel(
                        data = "Please Enter your Org Name", isSender = false,
                        isCardView = false, cardViewHeader = "",
                        date = yesterdayDate, senderName = "Test User",MessageModel.WHISPER
                    )
                )
            }, 3000)
            Handler(Looper.getMainLooper()).postDelayed({
                addMessage(
                    MessageModel(
                        data = "Please Enter your Name", isSender = false,
                        isCardView = false, cardViewHeader = "",
                        date = yesterdayDate, senderName = "Test User",MessageModel.NOPE
                    )
                )
            }, 5000)
        }

        chatScreen.getSendButtonImageView().setOnClickListener {
            et = chatScreen.getEditText()
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
                        cardViewHeader = "",
                        date = time, senderName = "Test User",
                        MessageModel.NOPE
                    )
                )
            }
            when (etValue) {
                "ABC" -> {
                    chatScreen.addMessage(
                        MessageModel(
                            data = "Welcome $etValue", isSender = false,
                            isCardView = true, cardViewHeader = "",
                            date = todayDate, senderName = "Test User",MessageModel.BOT
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
                        isCardView = false, cardViewHeader = "",
                        date = todayDate, senderName = "Test User",MessageModel.NOPE
                    )
                )
            }
        })

        chatScreen.setTopMenuItemClickListener(object : OnTopMenuItemClickListener {
            override fun onTopMenuItemClick(position: Int) {
                Toast.makeText(this@MainActivity, "Top Menu Item $position clicked", Toast.LENGTH_SHORT).show()
            }
        })

        chatScreen.setBottomMenuItemClickListener(object : OnBottomMenuItemClickListener {
            override fun onBottomMenuItemClick(position: Int) {
                Toast.makeText(this@MainActivity, "Bottom Menu Item $position clicked", Toast.LENGTH_SHORT).show()
            }
        })

        val userList = ArrayList<UserModel>()
        userList.add(UserModel("https://seeklogo.com/images/S/skype-icon-logo-62E333BBBA-seeklogo.com.png", "Manager 1", "Manager"))
        userList.add(UserModel("https://seeklogo.com/images/S/skype-icon-logo-62E333BBBA-seeklogo.com.png", "Agent 1", "Agent"))
        chatScreen.addUserList(userList)

        chatScreen.apply {
            Log.e("Chat Json Object:: ", getChatJsonObject().toString())
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