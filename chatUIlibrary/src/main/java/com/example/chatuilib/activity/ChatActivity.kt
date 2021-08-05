package activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Size
import android.view.*
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatuilib.R
import com.example.chatuilib.adapter.ChatButtonListAdapter
import com.example.chatuilib.adapter.ChatListAdapter
import com.example.chatuilib.customviews.CustomEditText
import com.example.chatuilib.customviews.CustomShapeableImageView
import com.example.chatuilib.customviews.CustomTextView
import com.example.chatuilib.listener.OnButtonClickListener
import com.example.chatuilib.listener.OnTopMenuItemClickListener
import com.example.chatuilib.model.*
import com.example.chatuilib.utils.AlertDialogView
import com.example.chatuilib.utils.AppConstants
import com.example.chatuilib.utils.AppConstants.CIRCLE_RECT
import com.example.chatuilib.utils.AppConstants.HORIZONTAL
import com.example.chatuilib.utils.AppConstants.OVAL_RECT
import com.example.chatuilib.utils.AppConstants.ROUNDED_CORNERS_RECT
import com.example.chatuilib.utils.AppConstants.SEMI_ROUNDED_CORNERS_RECT
import com.example.chatuilib.utils.AppConstants.SQUARE_RECT
import com.example.chatuilib.utils.AppConstants.VERTICAL
import com.example.chatuilib.utils.Utils
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
import android.os.Build
import android.view.WindowManager
import android.util.DisplayMetrics
import com.example.chatuilib.utils.AppLog
import android.view.MotionEvent
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.get
import com.example.chatuilib.customviews.CustomMaterialCardView


open class ChatActivity : AppCompatActivity() {
    private val TAG = ChatActivity::class.java.simpleName
    private var jsonObject: JSONObject? = null
    private var onTopMenuItemClickListener: OnTopMenuItemClickListener? = null
    private var chatButtonListAdapter: ChatButtonListAdapter? = null
    private var chatListAdapter: ChatListAdapter? = null
    private var chatList: ArrayList<MessageModel> = ArrayList()
    private var loaderList: ArrayList<Int> = ArrayList()
    private var buttonTitleList: ArrayList<String> = ArrayList()
    private var editTextView: CustomEditText? = null
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
    private lateinit var tvMenu: CustomTextView
    private lateinit var cvFlag: CustomMaterialCardView
    private lateinit var tvFlag: CustomTextView
    private lateinit var tvUser: CustomTextView
    private lateinit var cvUserNumber: CustomMaterialCardView
    private lateinit var tvUserNumber: CustomTextView
    private lateinit var rlUser: RelativeLayout
    private lateinit var rlTop: RelativeLayout
    private lateinit var context: Context

    override fun setContentView(layoutResID: Int) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.lib_activity_chat)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun init() {
        context = this@ChatActivity
        chatListAdapter = ChatListAdapter(this, chatList, null, null, loaderList, "Roboto")
        chatButtonListAdapter = ChatButtonListAdapter(this, null, buttonTitleList, "Roboto")

        titleLayout = findViewById(R.id.title_layout)
        tvBack = titleLayout.findViewById(R.id.tv_back)
        tvTitle = titleLayout.findViewById(R.id.tv_title)
        tvMenu = titleLayout.findViewById(R.id.tv_menu)
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
        cvFlag = findViewById(R.id.cv_flag)
        tvFlag = findViewById(R.id.tv_flag)
        rlUser = findViewById(R.id.rl_user)
        tvUser = findViewById(R.id.tv_user)
        cvUserNumber = findViewById(R.id.cv_user_number)
        tvUserNumber = findViewById(R.id.tv_user_number)
        rlTop = findViewById(R.id.rl_top)

        sendButtonImageView = ivSquareRectSend
        flashButtonImageView = ivSemiRoundedRectFlash
        editTextView = etRoundedRect

        tvMenu.setOnClickListener {
            showTopMenuPopup(tvMenu)
        }

        tvBack.setOnClickListener {
            finish()
        }

        cvFlag.setOnClickListener {
            showTopFlagMenuPopUp(it)
        }

        rlUser.setOnClickListener {
            showTopUserDialogPopUp(it)
        }

        if (jsonObject == null) {
            jsonObject = Utils.getJSONObject(this)
        }

        try {
            val themeJsonObject = jsonObject!!.optJSONObject("theme_color")

            val themeModel = ThemeModel(
                themeJsonObject?.optString("primary_color"),
                themeJsonObject?.optString("secondary_color"),
                themeJsonObject?.optString("common_font_color")
            )

            titleLayout.setBackgroundColor(getParsedColorValue(themeModel.secondaryColor!!))
            tvTitle.setTextColor(getParsedColorValue(themeModel.primaryColor!!))
            tvTitle.setCustomFont("${jsonObject?.optString("font_family")}.ttf")
            jsonObject?.optJSONObject("font_size")?.optString("title_header")?.let {
                Utils.getFontSizeInSSP(
                    it
                )
            }?.let {
                Utils.setTextSizeInSSP(
                    tvTitle,
                    it
                )
            }

            tvBack.setTextColor(getParsedColorValue(themeModel.primaryColor))
            tvMenu.setTextColor(getParsedColorValue(themeModel.primaryColor))

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
                buttonObject.optInt("button_shape_id")
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

        chatListAdapter = ChatListAdapter(
            this,
            chatList,
            chatBubbleConfigModel,
            cardViewConfigModel,
            loaderList,
            jsonObject?.optString("font_family")
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
        chatButtonListAdapter = ChatButtonListAdapter(
            this,
            buttonConfigModel,
            buttonTitleList,
            jsonObject?.optString("font_family")
        )
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

        editTextView?.setCustomFont("${jsonObject?.optString("font_family")}.ttf")

        flashButtonImageView.setOnClickListener { v ->
            showBottomMenuPopUp(v)
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

    /**
     * This method is used to show top menu
     */
    private fun showTopMenuPopup(v: View) {
        val popup = PopupMenu(this, v)
        val inflater: MenuInflater = popup.menuInflater

        val chatObject = jsonObject?.optJSONObject("chat")
        val topMenuArray = chatObject?.optJSONArray("top_menu")

        if (topMenuArray != null && topMenuArray.length() > 0) {
            tvMenu.visibility = View.VISIBLE
            for (i in 0 until topMenuArray.length()) {
                val menuName: String = topMenuArray[i] as String
                popup.menu.add(0, i, 0, menuName)
            }
        } else {
            tvMenu.visibility = View.GONE
        }

        inflater.inflate(R.menu.lib_top_menu, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            onTopMenuItemClickListener?.onTopMenuItemClick(menuItem.itemId)
            true
        }
        popup.show()
    }

    /**
     * This method is used to show bottom menu
     */
    @SuppressLint("InflateParams")
    private fun showBottomMenuPopUp(v: View) {
        val changeStatusPopUp = PopupWindow(this)
        val layoutInflater: LayoutInflater =
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout: View = layoutInflater.inflate(R.layout.lib_layout_bottom_menu,null)
        val llMain: LinearLayout = layout.findViewById(R.id.ll_main)
        changeStatusPopUp.contentView = layout

        val chatObject = jsonObject?.optJSONObject("chat")
        val bottomMenuArray = chatObject?.optJSONArray("bottom_menu")

        if (bottomMenuArray != null && bottomMenuArray.length() > 0) {
            flashButtonImageView.visibility = View.VISIBLE
            for (index in 0 until bottomMenuArray.length()) {
                val menuItem: View = layoutInflater.inflate(R.layout.lib_item_bottom_menu,null)
                val tvMenuIcon: CustomTextView = menuItem.findViewById(R.id.tv_menu_icon)
                val tvMenu: CustomTextView = menuItem.findViewById(R.id.tv_menu)
                val textValue: String = bottomMenuArray.getJSONObject(index).getString("text_value")
                val iconValue: String = bottomMenuArray.getJSONObject(index).getString("icon_value")
                tvMenu.text = textValue
                "&#x${iconValue}".also { tvMenuIcon.text = it }
                llMain.addView(menuItem)
            }
        } else {
            flashButtonImageView.visibility = View.GONE
        }

        layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        changeStatusPopUp.width = LinearLayout.LayoutParams.WRAP_CONTENT
        changeStatusPopUp.height =
            View.MeasureSpec.makeMeasureSpec(layout.measuredHeight, View.MeasureSpec.UNSPECIFIED)
        changeStatusPopUp.isFocusable = true
        changeStatusPopUp.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        changeStatusPopUp.elevation = 10F
        val metrics = DisplayMetrics()
        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getRealMetrics(metrics)
        val realHeight = metrics.heightPixels
        val height = realHeight - v.height - changeStatusPopUp.height - titleLayout.height
        changeStatusPopUp.showAtLocation(
            v, Gravity.START or Gravity.TOP, v.width, height
        )
    }

    /**
     * This method is used to show top flag menu
     */
    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("InflateParams")
    private fun showTopFlagMenuPopUp(v: View) {
        val layoutInflater: LayoutInflater =
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val menuItem: View = layoutInflater.inflate(R.layout.lib_item_flag, null)
        val menuItem1: View = layoutInflater.inflate(R.layout.lib_item_flag, null)
        val menuItem2: View = layoutInflater.inflate(R.layout.lib_item_flag, null)
        val layout: View = layoutInflater.inflate(R.layout.lib_layout_top_flag_menu, null)

        val llMain: LinearLayout = layout.findViewById(R.id.ll_main)
        val tvFlag: CustomTextView = menuItem.findViewById(R.id.tv_flag)
        val tvFlag1: CustomTextView = menuItem1.findViewById(R.id.tv_flag)
        val tvFlag2: CustomTextView = menuItem2.findViewById(R.id.tv_flag)
        tvFlag.setTextColor(getColor(R.color.lib_colorGreenFlag))
        tvFlag1.setTextColor(getColor(R.color.lib_colorUmberFlag))
        tvFlag2.setTextColor(getColor(R.color.lib_colorRedFlag))
        llMain.addView(menuItem)
        llMain.addView(menuItem1)
        llMain.addView(menuItem2)

        val changeStatusPopUp = PopupWindow(this)
        changeStatusPopUp.contentView = layout
        changeStatusPopUp.width = LinearLayout.LayoutParams.WRAP_CONTENT
        changeStatusPopUp.height = LinearLayout.LayoutParams.WRAP_CONTENT
        changeStatusPopUp.isFocusable = true
        changeStatusPopUp.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        changeStatusPopUp.elevation = 10F
        changeStatusPopUp.showAsDropDown(v,0,15)
    }

    /**
     * This method is used to show top user dialog
     */
    @SuppressLint("InflateParams")
    private fun showTopUserDialogPopUp(v: View) {
        val layoutInflater: LayoutInflater =
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val menuItem: View = layoutInflater.inflate(R.layout.lib_item_user, null)
        val menuItem1: View = layoutInflater.inflate(R.layout.lib_item_user, null)
        val layout: View = layoutInflater.inflate(R.layout.lib_layout_top_user, null)

        val llMain: LinearLayout = layout.findViewById(R.id.ll_main)
        val tvUserName1: CustomTextView = menuItem.findViewById(R.id.tv_user_name)
        val tvUserDesignation1: CustomTextView = menuItem1.findViewById(R.id.tv_user_designation)
        tvUserName1.text = getString(R.string.lib_manager)
        tvUserDesignation1.text = getString(R.string.lib_manager_1)
        llMain.addView(menuItem)
        llMain.addView(menuItem1)

        val changeStatusPopUp = PopupWindow(this)
        changeStatusPopUp.contentView = layout
        changeStatusPopUp.width = LinearLayout.LayoutParams.WRAP_CONTENT
        changeStatusPopUp.height = LinearLayout.LayoutParams.WRAP_CONTENT
        changeStatusPopUp.isFocusable = true
        changeStatusPopUp.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        changeStatusPopUp.elevation = 10F
        changeStatusPopUp.showAsDropDown(v,0,15)
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
         * Set Top Menu Item click Listener
         * @param onTopMenuItemClickListener: OnTopMenuItemClickListener
         */
        fun setTopMenuItemClickListener(onTopMenuItemClickListener: OnTopMenuItemClickListener) {
            this@ChatActivity.onTopMenuItemClickListener = onTopMenuItemClickListener
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