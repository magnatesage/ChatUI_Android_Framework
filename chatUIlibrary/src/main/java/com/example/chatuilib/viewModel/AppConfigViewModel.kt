package com.example.chatuilib.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatuilib.listener.ResponseListener
import com.example.chatuilib.model.*
import com.example.chatuilib.utils.CallApi
import com.example.chatuilib.utils.Endpoints
import com.example.chatuilib.utils.Utils

class AppConfigViewModel() : ViewModel() {
    private val splashScreenConfigModelLiveData: MutableLiveData<SplashScreenConfigModel> =
        MutableLiveData()
    private val fontSettingModelLiveData: MutableLiveData<FontSettingModel> = MutableLiveData()
    private val headerTabBarModelLiveData: MutableLiveData<HeaderTabBarModel> = MutableLiveData()
    private val chatBubbleConfigModelLiveData: MutableLiveData<ChatBubbleConfigModel> =
        MutableLiveData()
    private val buttonConfigModelLiveData: MutableLiveData<ButtonConfigModel> = MutableLiveData()
    private val cardViewConfigModelLiveData: MutableLiveData<CardViewConfigModel> =
        MutableLiveData()
    private val conversationBarConfigModelLiveData: MutableLiveData<ConversationBarConfigModel> =
        MutableLiveData()

    fun getSplashScreenConfigModel(): LiveData<SplashScreenConfigModel> {
        return splashScreenConfigModelLiveData
    }

    fun getFontSettingModel(): LiveData<FontSettingModel> {
        return fontSettingModelLiveData
    }

    fun getHeaderTabBarModel(): LiveData<HeaderTabBarModel> {
        return headerTabBarModelLiveData
    }

    fun getChatBubbleConfigModel(): LiveData<ChatBubbleConfigModel> {
        return chatBubbleConfigModelLiveData
    }

    fun getButtonConfigModel(): LiveData<ButtonConfigModel> {
        return buttonConfigModelLiveData
    }

    fun getCardViewConfigModel(): LiveData<CardViewConfigModel> {
        return cardViewConfigModelLiveData
    }

    fun getConversationBarConfigModel(): LiveData<ConversationBarConfigModel> {
        return conversationBarConfigModelLiveData
    }

    fun getAppConfig(
        context: Context,
        hashMap: HashMap<String, String>,
        showProgressBar: Boolean
    ) {
        if (Utils.isNetworkAvailable(context, true)) {
            CallApi.callPostApi(context, Endpoints.getAppConfig, hashMap, showProgressBar,
                responseListener = object : ResponseListener {
                    override fun onSuccessResponse(dataModel: DataModel?) {
                        splashScreenConfigModelLiveData.value = dataModel?.splashScreenConfigModel

                        fontSettingModelLiveData.value = dataModel?.fontSettingModel

                        headerTabBarModelLiveData.value = dataModel?.headerTabBarModel

                        chatBubbleConfigModelLiveData.value = dataModel?.chatBubbleConfigModel

                        buttonConfigModelLiveData.value = dataModel?.buttonConfigModel

                        cardViewConfigModelLiveData.value = dataModel?.cardViewConfigModel

                        conversationBarConfigModelLiveData.value =
                            dataModel?.conversationBarConfigModel
                    }

                    override fun onErrorResponse(message: Any?) {
                    }
                }
            )
        }
    }
}