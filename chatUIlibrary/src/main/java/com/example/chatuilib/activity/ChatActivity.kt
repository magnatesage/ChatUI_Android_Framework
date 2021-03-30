package com.example.chatuilib.activity

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.chatuilib.R
import com.example.chatuilib.adapter.ChatButtonListAdapter
import com.example.chatuilib.adapter.ChatListAdapter
import com.example.chatuilib.customviews.CustomEditText
import com.example.chatuilib.customviews.CustomShapeableImageView
import com.example.chatuilib.databinding.ActivityChatBinding
import com.example.chatuilib.listener.HTTPCallback
import com.example.chatuilib.listener.OnButtonClickListener
import com.example.chatuilib.model.*
import com.example.chatuilib.utils.*
import com.example.chatuilib.utils.AppConstants.CIRCLE_RECT
import com.example.chatuilib.utils.AppConstants.HORIZONTAL
import com.example.chatuilib.utils.AppConstants.OVAL_RECT
import com.example.chatuilib.utils.AppConstants.ROUNDED_CORNERS_RECT
import com.example.chatuilib.utils.AppConstants.SEMI_ROUNDED_CORNERS_RECT
import com.example.chatuilib.utils.AppConstants.SQUARE_RECT
import com.example.chatuilib.utils.AppConstants.VERTICAL
import com.example.chatuilib.utils.Utils.changeBg
import com.example.chatuilib.utils.Utils.getDesiredColorFromXML
import com.example.chatuilib.utils.Utils.getParsedColorValue
import com.example.chatuilib.utils.Utils.getSizeInSDP
import com.example.chatuilib.utils.Utils.loadImageWithExecutor
import com.example.chatuilib.utils.Utils.setBackgroundColorOfDrawable
import com.example.chatuilib.utils.Utils.setStrokeColorAndWidth
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


open class ChatActivity : AppCompatActivity() {
    private val TAG = ChatActivity::class.java.simpleName
    private var companyId = ""
    private var chatButtonListAdapter: ChatButtonListAdapter? = null
    private var chatListAdapter: ChatListAdapter? = null
    private var chatList: ArrayList<MessageModel> = ArrayList()
    private var loaderList: ArrayList<Int> = ArrayList()
    private var buttonTitleList: ArrayList<String> = ArrayList()
    private var organizationName = "ABC"
    private var editTextView: CustomEditText? = null
    private lateinit var sendButtonImageView: CustomShapeableImageView
    private lateinit var flashButtonImageView: CustomShapeableImageView
    lateinit var binding: ActivityChatBinding
    private lateinit var httpRequest: HTTPRequest

    override fun setContentView(layoutResID: Int) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        val view = binding.root
        super.setContentView(view)
    }

    private fun init() {
        chatListAdapter = ChatListAdapter(this, chatList, null, null, loaderList)
        chatButtonListAdapter = ChatButtonListAdapter(this, null, buttonTitleList)
        editTextView = binding.etRoundedRect
        sendButtonImageView = binding.ivSquareRectSend
        flashButtonImageView = binding.ivSemiRoundedRectFlash

        if (companyId.isNotBlank()) {
            val hashMap: HashMap<String, String> = HashMap()
            hashMap["application_id"] = companyId
            httpRequest = HTTPRequest(
                AppConstants.BASE_URL + AppConstants.GET_APP_CONFIG,
                hashMap,
                object : HTTPCallback {
                    override fun onSuccessResponse(output: String) {
                        val jsonObject = JSONObject(output)
                        try {
                            val status = jsonObject.optString("status")
                            val statusCode = jsonObject.optInt("status_code")
                            val message = jsonObject.optString("message")
                            val data = jsonObject.optJSONObject("data")
                            if (status == "success" && statusCode == 200) {

                                val chatBubbleObject = data?.optJSONObject("Chat Bubble")
                                val chatBubbleConfigModel = ChatBubbleConfigModel(
                                    chatBubbleObject?.optInt("chatBubbleStyle")!!,
                                    chatBubbleObject.optString("chatBotBgType"),
                                    chatBubbleObject.optString("chatBotBgColor"),
                                    chatBubbleObject.optString("chatBotBgImageUrl"),
                                    chatBubbleObject.optString("senderChatBubbleColor"),
                                    chatBubbleObject.optString("senderTextColor"),
                                    chatBubbleObject.optString("receiverChatBubbleColor"),
                                    chatBubbleObject.optString("receiverTextColor"),
                                    chatBubbleObject.optBoolean("cardBgDropShadow")
                                )
                                setBackgroundOfRecyclerView(chatBubbleConfigModel)

                                val buttonObject = data.optJSONObject("Button")
                                val buttonConfigModel = ButtonConfigModel(
                                    buttonObject?.optInt("buttonShapeSelectedBg")!!,
                                    buttonObject.optString("normalButtonColor"),
                                    buttonObject.optString("normalTextColor"),
                                    buttonObject.optString("normalBorderColor"),
                                    buttonObject.optString("normalBorderSize"),
                                    buttonObject.optString("clickedButtonColor"),
                                    buttonObject.optString("clickedTextColor"),
                                    buttonObject.optString("clickedBorderColor"),
                                    buttonObject.optString("clickedBorderSize"),
                                    buttonObject.optString("normalIconColor"),
                                    buttonObject.optString("clickedIconColor"),
                                    buttonObject.optString("iconSize"),
                                    buttonObject.optString("buttonPlacementStyle"),
                                    buttonObject.optBoolean("buttonBgDropShadow"),
                                    buttonObject.optInt("buttonShapeSelectedId")
                                )
                                setUpButtonShapesRecyclerViewDisplay(buttonConfigModel)

                                val conversationBarStylingObject =
                                    data.optJSONObject("Conversation Bar Styling")
                                val conversationBarConfigModel = ConversationBarConfigModel(
                                    conversationBarStylingObject?.optString("conversationBarShapeSelected"),
                                    conversationBarStylingObject?.optString("floatingIconUrl")
                                )
                                setUpConversationBarStylingDisplay(conversationBarConfigModel)

                                val cardViewObject = data.optJSONObject("Card View")
                                val cardViewConfigModel = CardViewConfigModel(
                                    cardViewObject?.optInt("cardviewShapeSelectedId")!!,
                                    cardViewObject.optBoolean("cardviewBgDropShadow"),
                                    cardViewObject.optString("cardviewBorderColor"),
                                    cardViewObject.optString("cardviewBorderSize"),
                                    cardViewObject.optString("cardviewHeaderBgColor"),
                                    cardViewObject.optString("cardviewHeaderTextColor"),
                                    cardViewObject.optString("cardviewHeaderTextSize"),
                                    cardViewObject.optString("cardviewFooterButtonBgColor"),
                                    cardViewObject.optString("cardviewFooterButtonTextColor"),
                                    cardViewObject.optString("cardviewFooterButtonTextSize")
                                )
                                setupChatRecyclerViewDisplay(
                                    chatBubbleConfigModel,
                                    cardViewConfigModel
                                )
                            } else {
                                AppLog.e("Response :: ", jsonObject.toString())
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onErrorResponse(responseCode: Int, output: String) {
                        AppLog.e(TAG, getString(R.string.error_api_msg))
                    }
                })
        }

        binding.rlConversationBar.visibility = View.VISIBLE
        binding.rlRoundedRect.visibility = View.VISIBLE
    }

    /**
     * This method is used to set background Color or Image to Recyclerview of Chat list.
     */
    @SuppressLint("CheckResult")
    private fun setBackgroundOfRecyclerView(chatBubbleConfigModel: ChatBubbleConfigModel) {
        if (chatBubbleConfigModel.chatBotBgType?.toLowerCase(Locale.ROOT) == AppConstants.COLOR) {
            binding.rvChatList.setBackgroundColor(getParsedColorValue(chatBubbleConfigModel.chatBotBgColor!!))
        } else {
            Glide.with(this).load(chatBubbleConfigModel.chatBotBgImageUrl)
                .into(object : SimpleTarget<Drawable>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        binding.rvChatList.background = resource
                    }
                })
        }
    }

    /**
     * This method is used to set adapter for RecyclerView & bind data to Recyclerview of chat
     */
    private fun setupChatRecyclerViewDisplay(
        chatBubbleConfigModel: ChatBubbleConfigModel,
        cardViewConfigModel: CardViewConfigModel
    ) {
        binding.rvChatList.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
        }

        if (cardViewConfigModel != null) {
            chatListAdapter = ChatListAdapter(
                this,
                chatList,
                chatBubbleConfigModel,
                cardViewConfigModel,
                loaderList
            )
            binding.rvChatList.adapter = chatListAdapter

            binding.rvChatList.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
                if (bottom < oldBottom) {
                    if (binding.rvChatList.adapter?.itemCount!! > 0) {
                        binding.rvChatList.adapter?.itemCount?.minus(
                            1
                        )?.let { it1 -> binding.rvChatList.smoothScrollToPosition(it1) }
                    }
                }
            }
        }
    }

    /**
     * This method is used to set adapter for RecyclerView & bind data to Recyclerview of Button
     */
    private fun setUpButtonShapesRecyclerViewDisplay(buttonConfigModel: ButtonConfigModel) {
        binding.rlConversationBar.visibility = View.GONE
        binding.rlButtonList.visibility = View.VISIBLE
        changeBg(
            binding.rlButtonList,
            getDesiredColorFromXML(this, R.color.colorWhite)
        )

        if (buttonConfigModel.buttonPlacementStyle?.toLowerCase(Locale.ROOT) == HORIZONTAL) {
            setButtonListOrientation(HORIZONTAL)
        } else {
            setButtonListOrientation(VERTICAL)
        }

        val onButtonClickListener = chatButtonListAdapter?.onButtonClickListener
        chatButtonListAdapter = ChatButtonListAdapter(this, buttonConfigModel, buttonTitleList)
        binding.rvButtonList.adapter = chatButtonListAdapter
        if (onButtonClickListener != null) {
            chatButtonListAdapter?.setButtonListClickListener(onButtonClickListener)
        }

    }

    /**
     * This method is used to Orientation of Button List RecyclerView
     */
    private fun setButtonListOrientation(orientation: String) {
        when (orientation) {
            HORIZONTAL -> {
                binding.rvButtonList.apply {
                    layoutManager =
                        LinearLayoutManager(
                            this@ChatActivity,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                }
            }

            VERTICAL -> {
                val layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    getSizeInSDP(this, R.dimen._150sdp)
                )
                binding.rvButtonList.layoutParams = layoutParams
                binding.rvButtonList.apply {
                    layoutManager = LinearLayoutManager(this@ChatActivity)
                }
            }
        }
    }

    /**
     * This method is used to set conversation bar style
     */
    private fun setUpConversationBarStylingDisplay(conversationBarConfigModel: ConversationBarConfigModel) {
        val bgColor = getDesiredColorFromXML(this, R.color.colorBorder)
        val floatingImageUrl = conversationBarConfigModel.floatingIconUrl
        binding.rlConversationBar.visibility = View.VISIBLE
        val onClickListener = sendButtonImageView.clickListener
        val flashBtnOnClickListener = flashButtonImageView.clickListener

        when (conversationBarConfigModel.conversationBarShapeSelected) {
            ROUNDED_CORNERS_RECT -> {
                binding.rlRoundedRect.visibility = View.VISIBLE
                setStrokeColorAndWidth(
                    binding.cvRoundedRect, bgColor, getSizeInSDP(
                        this,
                        R.dimen._1sdp
                    )
                )
                loadImageWithExecutor(
                    this,
                    floatingImageUrl!!, binding.ivRoundedRectFlash,
                    R.drawable.flash_small
                )
                setBackgroundColorOfDrawable(binding.ibRoundedRectSend, bgColor)

                editTextView = binding.etRoundedRect
                sendButtonImageView = binding.ibRoundedRectSend
                flashButtonImageView = binding.ivRoundedRectFlash
            }

            SQUARE_RECT -> {
                binding.rlSquareRect.visibility = View.VISIBLE
                setStrokeColorAndWidth(
                    binding.cvSquareRect, bgColor, getSizeInSDP(
                        this,
                        R.dimen._1sdp
                    )
                )
                loadImageWithExecutor(
                    this,
                    floatingImageUrl!!,
                    binding.ivSquareRectFlash,
                    R.drawable.flash_small
                )

                editTextView = binding.etSquareRect
                sendButtonImageView = binding.ivSquareRectSend
                flashButtonImageView = binding.ivSquareRectFlash
            }

            SEMI_ROUNDED_CORNERS_RECT -> {
                binding.rlSemiRoundedRect.visibility = View.VISIBLE
                setStrokeColorAndWidth(
                    binding.cvSemiRoundedRect,
                    bgColor,
                    getSizeInSDP(this, R.dimen._1sdp)
                )
                loadImageWithExecutor(
                    this,
                    floatingImageUrl!!,
                    binding.ivSemiRoundedRectFlash,
                    R.drawable.flash_small
                )

                editTextView = binding.etSemiRoundedRect
                sendButtonImageView = binding.ivSemiRoundedRectFlash
                flashButtonImageView = binding.ivSemiRoundedRectFlash
            }

            OVAL_RECT -> {
                binding.rlOvalRect.visibility = View.VISIBLE
                setStrokeColorAndWidth(
                    binding.cvOvalRect, bgColor, getSizeInSDP(
                        this,
                        R.dimen._1sdp
                    )
                )
                loadImageWithExecutor(
                    this,
                    floatingImageUrl!!,
                    binding.ivOvalRectFlash,
                    R.drawable.flash_small
                )

                editTextView = binding.etOvalRect
                sendButtonImageView = binding.ivOvalRectSend
                flashButtonImageView = binding.ivOvalRectFlash
            }

            CIRCLE_RECT -> {
                binding.rlCircleRect.visibility = View.VISIBLE
                setStrokeColorAndWidth(
                    binding.cvCircleRect, bgColor, getSizeInSDP(
                        this,
                        R.dimen._1sdp
                    )
                )
                loadImageWithExecutor(
                    this,
                    floatingImageUrl!!,
                    binding.ivCircleRectFlash,
                    R.drawable.flash_small
                )

                editTextView = binding.etCircleRect
                sendButtonImageView = binding.ivCircleRectSend
                flashButtonImageView = binding.ivCircleRectFlash
            }
        }

        if (onClickListener != null) {
            sendButtonImageView.setOnClickListener(onClickListener)
        }

        if (flashBtnOnClickListener != null) {
            flashButtonImageView.setOnClickListener(flashBtnOnClickListener)
        }
        editTextView?.setText(organizationName)
    }

    /**
     * This method is used to show under development dialog
     */
    private fun showUnderDevelopmentDialog() {
        AlertDialogView.showAlert(
            this,
            getString(R.string.app_name),
            getString(R.string.under_development),
            getString(R.string.ok)
        )?.show()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        chatListAdapter?.notifyDataSetChanged()
    }

    open inner class ChatScreen(baseUrl: String, userCompanyId: String) {
        init {
            companyId = userCompanyId
            AppConstants.BASE_URL = baseUrl
            init()
        }

        /**
         * Returns Company Id
         */
        fun getCompanyId(): String {
            return companyId
        }

        /**
         * Returns Title of Chat Screen
         */
        fun getTitle(): String {
            return binding.titleLayout.tvTitle.text.toString()
        }

        /**
         * Returns ChatList Adapter
         */
        fun getChatListAdapter(): ChatListAdapter? {
            return chatListAdapter
        }

        /**
         * Returns ChatButtonList Adapter
         */
        fun getChatButtonListAdapter(): ChatButtonListAdapter? {
            return chatButtonListAdapter
        }

        /**
         * Returns ChatList Recyclerview
         */
        fun getChatListRecyclerview(): RecyclerView {
            return binding.rvChatList
        }

        /**
         * Returns ButtonList Recyclerview
         */
        fun getButtonListRecyclerview(): RecyclerView {
            return binding.rvButtonList
        }

        /**
         * Returns CustomEditText
         */
        fun getEditText(): CustomEditText? {
            return editTextView
        }

        /**
         * Returns CustomEditText
         */
        fun getSendButtonImageView(): CustomShapeableImageView {
            return sendButtonImageView
        }

        /**
         * Returns CustomEditText
         */
        fun getFlashButtonImageView(): CustomShapeableImageView {
            return flashButtonImageView
        }

        /**
         * Set Button click Listener
         * @param onButtonClickListener: OnButtonClickListener
         */
        fun setButtonListClickListener(onButtonClickListener: OnButtonClickListener) {
            chatButtonListAdapter?.setButtonListClickListener(onButtonClickListener)
        }

        /**
         * Set Title of Chat Screen
         * @param title: String
         */
        fun setTitle(title: String) {
            binding.titleLayout.tvTitle.text = title
        }

        /**
         * Adds Message Model to current Chat List
         * @param messageModel: MessageModel
         */
        fun addMessage(messageModel: MessageModel) {
            chatList.add(messageModel)
            chatListAdapter?.notifyDataSetChanged()
            binding.rvChatList.scrollToPosition(chatList.size - 1)
        }

        /**
         * Adds Message Model List to current Chat List
         * @param messageModelList: List<MessageModel>
         */
        fun addMessageList(messageModelList: List<MessageModel>) {
            chatList.addAll(messageModelList)
            chatListAdapter?.notifyDataSetChanged()
            binding.rvChatList.scrollToPosition(chatList.size - 1)
        }

        /**
         * Adds Button List
         * @param buttonTitleArrayList: ArrayList<String>
         */
        fun addButtonList(buttonTitleArrayList: ArrayList<String>) {
            buttonTitleList.clear()
            buttonTitleList.addAll(buttonTitleArrayList)
            binding.rlConversationBar.visibility = View.GONE
            binding.rlButtonList.visibility = View.VISIBLE
            chatButtonListAdapter?.notifyDataSetChanged()
        }
    }


}