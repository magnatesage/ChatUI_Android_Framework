package com.example.chatuilib.activity

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatuilib.R
import com.example.chatuilib.adapter.ChatButtonListAdapter
import com.example.chatuilib.adapter.ChatListAdapter
import com.example.chatuilib.customviews.CustomEditText
import com.example.chatuilib.customviews.CustomShapeableImageView
import com.example.chatuilib.customviews.CustomTextView
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
import com.google.android.material.card.MaterialCardView
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

open class ChatActivity : AppCompatActivity() {
    private val TAG = ChatActivity::class.java.simpleName
    private var jsonObject: JSONObject? = null
    private var chatButtonListAdapter: ChatButtonListAdapter? = null
    private var chatListAdapter: ChatListAdapter? = null
    private var chatList: ArrayList<MessageModel> = ArrayList()
    private var loaderList: ArrayList<Int> = ArrayList()
    private var buttonTitleList: ArrayList<String> = ArrayList()
    private var organizationName = "ABC"
    private var editTextView: CustomEditText? = null
    private lateinit var httpRequest: HTTPRequest
    private lateinit var etRoundedRect: CustomEditText
    private lateinit var etSquareRect: CustomEditText
    private lateinit var etSemiRoundedRect: CustomEditText
    private lateinit var etOvalRect: CustomEditText
    private lateinit var etCircleRect: CustomEditText
    lateinit var rlConversationBar: RelativeLayout
    private lateinit var rlRoundedRect: RelativeLayout
    lateinit var rlButtonList: RelativeLayout
    private lateinit var rlSquareRect: RelativeLayout
    private lateinit var rlSemiRoundedRect: RelativeLayout
    private lateinit var rlOvalRect: RelativeLayout
    private lateinit var rlCircleRect: RelativeLayout
    private lateinit var rvChatList: RecyclerView
    private lateinit var rvButtonList: RecyclerView
    private lateinit var cvRoundedRect: MaterialCardView
    private lateinit var cvSquareRect: MaterialCardView
    private lateinit var cvSemiRoundedRect: MaterialCardView
    private lateinit var cvOvalRect: MaterialCardView
    private lateinit var cvCircleRect: MaterialCardView
    private lateinit var flashButtonImageView: CustomShapeableImageView
    private lateinit var sendButtonImageView: CustomShapeableImageView
    private lateinit var ivSquareRectFlash: CustomShapeableImageView
    private lateinit var ivSquareRectSend: CustomShapeableImageView
    private lateinit var ivSemiRoundedRectFlash: CustomShapeableImageView
    private lateinit var ivSemiRoundedRectSend: CustomShapeableImageView
    private lateinit var ivRoundedRectFlash: CustomShapeableImageView
    private lateinit var ivRoundedRectSend: CustomShapeableImageView
    private lateinit var ivOvalRectFlash: CustomShapeableImageView
    private lateinit var ivOvalRectSend: CustomShapeableImageView
    private lateinit var ivCircleRectFlash: CustomShapeableImageView
    private lateinit var ivCircleRectSend: CustomShapeableImageView
    private lateinit var titleLayout: View
    private lateinit var tvBack: CustomTextView
    private lateinit var tvTitle: CustomTextView

    override fun setContentView(layoutResID: Int) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.lib_activity_chat)
    }

    private fun init() {
        chatListAdapter = ChatListAdapter(this, chatList, null, null, loaderList)
        chatButtonListAdapter = ChatButtonListAdapter(this, null, buttonTitleList)

        titleLayout = findViewById(R.id.title_layout)
        tvBack = titleLayout.findViewById(R.id.tv_back)
        tvTitle = titleLayout.findViewById(R.id.tv_title)
        etRoundedRect = findViewById(R.id.et_rounded_rect)
        etSquareRect = findViewById(R.id.et_square_rect)
        etSemiRoundedRect = findViewById(R.id.et_semi_rounded_rect)
        etOvalRect = findViewById(R.id.et_oval_rect)
        etCircleRect = findViewById(R.id.et_circle_rect)
        rlConversationBar = findViewById(R.id.rl_conversation_bar)
        rlRoundedRect = findViewById(R.id.rl_rounded_rect)
        rlButtonList = findViewById(R.id.rl_button_list)
        rlSquareRect = findViewById(R.id.rl_square_rect)
        rlSemiRoundedRect = findViewById(R.id.rl_semi_rounded_rect)
        rlOvalRect = findViewById(R.id.rl_oval_rect)
        rlCircleRect = findViewById(R.id.rl_circle_rect)
        rvChatList = findViewById(R.id.rv_chat_list)
        rvButtonList = findViewById(R.id.rv_button_list)
        cvRoundedRect = findViewById(R.id.cv_rounded_rect)
        cvSquareRect = findViewById(R.id.cv_square_rect)
        cvSemiRoundedRect = findViewById(R.id.cv_semi_rounded_rect)
        cvOvalRect = findViewById(R.id.cv_oval_rect)
        cvCircleRect = findViewById(R.id.cv_circle_rect)
        ivRoundedRectFlash = findViewById(R.id.iv_rounded_rect_flash)
        ivRoundedRectSend = findViewById(R.id.iv_rounded_rect_send)
        ivSquareRectFlash = findViewById(R.id.iv_square_rect_flash)
        ivSquareRectSend = findViewById(R.id.iv_square_rect_send)
        ivSemiRoundedRectFlash = findViewById(R.id.iv_semi_rounded_rect_flash)
        ivSemiRoundedRectSend = findViewById(R.id.iv_semi_rounded_rect_send)
        ivOvalRectFlash = findViewById(R.id.iv_oval_rect_flash)
        ivOvalRectSend = findViewById(R.id.iv_oval_rect_send)
        ivCircleRectFlash = findViewById(R.id.iv_circle_rect_flash)
        ivCircleRectSend = findViewById(R.id.iv_circle_rect_send)

        sendButtonImageView = ivSquareRectSend
        flashButtonImageView = ivSemiRoundedRectFlash
        editTextView = etRoundedRect

        tvBack.setOnClickListener {
            finish()
        }

        if (jsonObject == null) {
            jsonObject = Utils.getJSONObject(this)
        }

        try {
            AppLog.e("Chat Object :: ", jsonObject.toString())

            val themeJsonObject = jsonObject!!.optJSONObject("theme_color")

            val themeModel = ThemeModel(
                themeJsonObject?.optString("primary_color"),
                themeJsonObject?.optString("secondary_color"),
                themeJsonObject?.optString("common_font_color")
            )

            titleLayout.setBackgroundColor(getParsedColorValue(themeModel.secondaryColor!!))
            tvTitle.setTextColor(getParsedColorValue(themeModel.primaryColor!!))
            jsonObject?.optJSONObject("font_size")?.optString("title_header")?.let {
                Utils.getFontSizeInSSP(
                    it
                )
            }?.let {
                Utils.setTextSizeInSSP(tvTitle,
                    it
                )
            }

            tvBack.setTextColor(getParsedColorValue(themeModel.primaryColor))

            val chatObject = jsonObject?.optJSONObject("chat")

            val chatBubbleObject = chatObject?.optJSONObject("chat_bubble")
            val chatBubbleConfigModel = ChatBubbleConfigModel(
                chatBubbleObject?.optInt("chat_bubble_style")!!,
                chatBubbleObject.optString("chat_screen_bg_type"),
                chatBubbleObject.optString("chat_screen_bg_color"),
                chatBubbleObject.optString("chat_screen_bg_image_url"),
                chatBubbleObject.optString("sender_chat_bubble_color"),
                chatBubbleObject.optString("sender_text_color"),
                chatBubbleObject.optString("receiver_chat_bubble_color"),
                chatBubbleObject.optString("receiver_text_color"),
                chatBubbleObject.optBoolean("card_bg_drop_shadow")
            )
            setBackgroundOfRecyclerView(chatBubbleConfigModel)

            val buttonObject = chatObject.optJSONObject("button")
            val buttonConfigModel = ButtonConfigModel(
                buttonObject?.optString("normal_button_color"),
                buttonObject?.optString("normal_text_color"),
                buttonObject?.optString("normal_border_color"),
                buttonObject?.optString("normal_border_size"),
                buttonObject?.optString("clicked_button_color"),
                buttonObject?.optString("clicked_text_color"),
                buttonObject?.optString("clicked_border_color"),
                buttonObject?.optString("clicked_border_size"),
                buttonObject?.optString("button_placement_style"),
                buttonObject?.optBoolean("button_bg_drop_shadow")!!,
                buttonObject?.optInt("button_shape_id")!!
            )
            setUpButtonShapesRecyclerViewDisplay(buttonConfigModel)

            val conversationBarStylingObject =
                chatObject.optJSONObject("conversation_bar")
            val conversationBarConfigModel = ConversationBarConfigModel(
                conversationBarStylingObject?.optString("conversation_bar_shape"),
                conversationBarStylingObject?.optString("floating_icon_url")
            )
            setUpConversationBarStylingDisplay(conversationBarConfigModel)

            val cardViewObject = chatObject.optJSONObject("card_view")
            val cardViewConfigModel = CardViewConfigModel(
                cardViewObject?.optInt("cardview_shape_id")!!,
                cardViewObject.optBoolean("cardview_bg_drop_shadow"),
                cardViewObject.optString("cardview_border_color"),
                cardViewObject.optString("cardview_border_size"),
                cardViewObject.optString("cardview_header_bg_color"),
                cardViewObject.optString("cardview_header_text_color"),
                cardViewObject.optString("cardview_header_text_size"),
                cardViewObject.optString("cardview_footer_button_bg_color"),
                cardViewObject.optString("cardview_footer_button_text_color"),
                cardViewObject.optString("cardview_footer_button_text_size")
            )
            setupChatRecyclerViewDisplay(
                chatBubbleConfigModel,
                cardViewConfigModel
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

        rlConversationBar.visibility = View.VISIBLE
    }

    /**
     * This method is used to set background Color or Image to Recyclerview of Chat list.
     */
    @SuppressLint("CheckResult")
    private fun setBackgroundOfRecyclerView(chatBubbleConfigModel: ChatBubbleConfigModel) {
        if (chatBubbleConfigModel.chatBotBgType?.toLowerCase(Locale.ROOT) == AppConstants.COLOR) {
            rvChatList.setBackgroundColor(getParsedColorValue(chatBubbleConfigModel.chatBotBgColor!!))
        } else {
            if (!chatBubbleConfigModel.chatBotBgImageUrl.isNullOrBlank()) {
                loadImageWithExecutor(
                    chatBubbleConfigModel.chatBotBgImageUrl,
                    rvChatList, R.drawable.lib_upload
                )
            }
        }
    }

    /**
     * This method is used to set adapter for RecyclerView & bind data to Recyclerview of chat
     */
    private fun setupChatRecyclerViewDisplay(
        chatBubbleConfigModel: ChatBubbleConfigModel,
        cardViewConfigModel: CardViewConfigModel
    ) {
        rvChatList.apply {
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
            rvChatList.adapter = chatListAdapter

            rvChatList.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
                if (bottom < oldBottom) {
                    if (rvChatList.adapter?.itemCount!! > 0) {
                        rvChatList.adapter?.itemCount?.minus(
                            1
                        )?.let { it1 -> rvChatList.smoothScrollToPosition(it1) }
                    }
                }
            }
        }
    }

    /**
     * This method is used to set adapter for RecyclerView & bind data to Recyclerview of Button
     */
    private fun setUpButtonShapesRecyclerViewDisplay(buttonConfigModel: ButtonConfigModel) {
        rlConversationBar.visibility = View.GONE
        changeBg(
            rlButtonList,
            getDesiredColorFromXML(this, R.color.lib_colorWhite)
        )

        if (buttonConfigModel.buttonPlacementStyle?.toLowerCase(Locale.ROOT) == HORIZONTAL) {
            setButtonListOrientation(HORIZONTAL)
        } else {
            setButtonListOrientation(VERTICAL)
        }

        val onButtonClickListener = chatButtonListAdapter?.onButtonClickListener
        chatButtonListAdapter = ChatButtonListAdapter(this, buttonConfigModel, buttonTitleList)
        rvButtonList.adapter = chatButtonListAdapter
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
                rvButtonList.apply {
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
                rvButtonList.layoutParams = layoutParams
                rvButtonList.apply {
                    layoutManager = LinearLayoutManager(this@ChatActivity)
                }
            }
        }
    }

    /**
     * This method is used to set conversation bar style
     */
    private fun setUpConversationBarStylingDisplay(conversationBarConfigModel: ConversationBarConfigModel) {
        val bgColor = getDesiredColorFromXML(this, R.color.lib_colorBorder)
        val floatingImageUrl = conversationBarConfigModel.floatingIconUrl
        rlConversationBar.visibility = View.VISIBLE
        val onClickListener = sendButtonImageView.clickListener
        val flashBtnOnClickListener = flashButtonImageView.clickListener

        when (conversationBarConfigModel.conversationBarShapeSelected) {
            ROUNDED_CORNERS_RECT -> {
                rlRoundedRect.visibility = View.VISIBLE
                setStrokeColorAndWidth(
                    cvRoundedRect, bgColor, getSizeInSDP(
                        this,
                        R.dimen._1sdp
                    )
                )
                loadImageWithExecutor(
                    floatingImageUrl!!, ivRoundedRectFlash,
                    R.drawable.lib_flash_small
                )
                setBackgroundColorOfDrawable(ivRoundedRectSend, bgColor)

                editTextView = etRoundedRect
                sendButtonImageView = ivRoundedRectSend
                flashButtonImageView = ivRoundedRectFlash
            }

            SQUARE_RECT -> {
                rlSquareRect.visibility = View.VISIBLE
                setStrokeColorAndWidth(
                    cvSquareRect, bgColor, getSizeInSDP(
                        this,
                        R.dimen._1sdp
                    )
                )
                loadImageWithExecutor(
                    floatingImageUrl!!,
                    ivSquareRectFlash,
                    R.drawable.lib_flash_small
                )

                editTextView = etSquareRect
                sendButtonImageView = ivSquareRectSend
                flashButtonImageView = ivSquareRectFlash
            }

            SEMI_ROUNDED_CORNERS_RECT -> {
                rlSemiRoundedRect.visibility = View.VISIBLE
                setStrokeColorAndWidth(
                    cvSemiRoundedRect,
                    bgColor,
                    getSizeInSDP(this, R.dimen._1sdp)
                )
                loadImageWithExecutor(
                    floatingImageUrl!!,
                    ivSemiRoundedRectFlash,
                    R.drawable.lib_flash_small
                )

                editTextView = etSemiRoundedRect
                sendButtonImageView = ivSemiRoundedRectSend
                flashButtonImageView = ivSemiRoundedRectFlash
            }

            OVAL_RECT -> {
                rlOvalRect.visibility = View.VISIBLE
                setStrokeColorAndWidth(
                    cvOvalRect, bgColor, getSizeInSDP(
                        this,
                        R.dimen._1sdp
                    )
                )
                loadImageWithExecutor(
                    floatingImageUrl!!,
                    ivOvalRectFlash,
                    R.drawable.lib_flash_small
                )

                editTextView = etOvalRect
                sendButtonImageView = ivOvalRectSend
                flashButtonImageView = ivOvalRectFlash
            }

            CIRCLE_RECT -> {
                rlCircleRect.visibility = View.VISIBLE
                setStrokeColorAndWidth(
                    cvCircleRect, bgColor, getSizeInSDP(
                        this,
                        R.dimen._1sdp
                    )
                )
                loadImageWithExecutor(
                    floatingImageUrl!!,
                    ivCircleRectFlash,
                    R.drawable.lib_flash_small
                )

                editTextView = etCircleRect
                sendButtonImageView = ivCircleRectSend
                flashButtonImageView = ivCircleRectFlash
            }
        }

        if (onClickListener != null) {
            sendButtonImageView.setOnClickListener(onClickListener)
        }

        if (flashBtnOnClickListener != null) {
            flashButtonImageView.setOnClickListener(flashBtnOnClickListener)
        }
    }

    /**
     * This method is used to show under development dialog
     */
    private fun showUnderDevelopmentDialog() {
        AlertDialogView.showAlert(
            this,
            getString(R.string.lib_app_name),
            getString(R.string.lib_under_development),
            getString(R.string.lib_ok)
        )?.show()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        chatListAdapter?.notifyDataSetChanged()
    }

    open inner class ChatScreen(jsonObject: JSONObject?) {
        init {
            this@ChatActivity.jsonObject = jsonObject
            init()
        }

        /**
         * Returns JSONObject
         */
        fun getChatJsonObject(): JSONObject? {
            return this@ChatActivity.jsonObject
        }

        /**
         * Returns Title of Chat Screen
         */
        fun getTitle(): String {
            return tvTitle.text.toString()
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
            return rvChatList
        }

        /**
         * Returns ButtonList Recyclerview
         */
        fun getButtonListRecyclerview(): RecyclerView {
            return rvButtonList
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
         * Returns CustomTextView back button icon instance
         */
        fun getBackButton(): CustomTextView {
            return tvBack
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
            tvTitle.text = title
        }

        /**
         * Adds Message Model to current Chat List
         * @param messageModel: MessageModel
         */
        fun addMessage(messageModel: MessageModel) {
            Utils.hideKeyboard(sendButtonImageView)
            chatList.add(messageModel)
            chatListAdapter?.notifyDataSetChanged()
            rvChatList.scrollToPosition(chatList.size - 1)
        }

        /**
         * Adds Message Model List to current Chat List
         * @param messageModelList: List<MessageModel>
         */
        fun addMessageList(messageModelList: List<MessageModel>) {
            Utils.hideKeyboard(sendButtonImageView)
            chatList.addAll(messageModelList)
            chatListAdapter?.notifyDataSetChanged()
            rvChatList.scrollToPosition(chatList.size - 1)
        }

        /**
         * Adds Button List
         * @param buttonTitleArrayList: ArrayList<String>
         */
        fun addButtonList(buttonTitleArrayList: ArrayList<String>) {
            Utils.hideKeyboard(sendButtonImageView)
            buttonTitleList.clear()
            buttonTitleList.addAll(buttonTitleArrayList)
            rlConversationBar.visibility = View.GONE
            rlButtonList.visibility = View.VISIBLE
            chatButtonListAdapter?.notifyDataSetChanged()
        }
    }


}