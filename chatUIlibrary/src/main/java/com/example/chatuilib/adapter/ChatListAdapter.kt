package com.example.chatuilib.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.text.method.ScrollingMovementMethod
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.chatuilib.R
import com.example.chatuilib.customviews.CustomMaterialButton
import com.example.chatuilib.customviews.CustomMaterialCardView
import com.example.chatuilib.customviews.CustomTextView
import com.example.chatuilib.databinding.ItemChatListBinding
import com.example.chatuilib.model.CardViewConfigModel
import com.example.chatuilib.model.ChatBubbleConfigModel
import com.example.chatuilib.model.MessageModel
import com.example.chatuilib.utils.AppConstants
import com.example.chatuilib.utils.Utils.changeBg
import com.example.chatuilib.utils.Utils.changeTextColor
import com.example.chatuilib.utils.Utils.getFontsFromApp
import com.example.chatuilib.utils.Utils.getLayoutFromInflater
import com.example.chatuilib.utils.Utils.getParsedColorValue
import com.example.chatuilib.utils.Utils.getSizeInSDP
import com.example.chatuilib.utils.Utils.getStringFromXML
import com.example.chatuilib.utils.Utils.setCardElevation
import com.example.chatuilib.utils.Utils.setElevationShadow
import com.example.chatuilib.utils.Utils.setStrokeColorAndWidth
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import java.text.SimpleDateFormat
import java.util.*

const val MAX_LINES = 10

class ChatListAdapter(
    val context: Activity, private val chatList: ArrayList<MessageModel>,
    private val chatBubbleConfigModel: ChatBubbleConfigModel?,
    private val cardViewConfigModel: CardViewConfigModel?,
    private val loaderList: ArrayList<Int>
) : RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemChatListBinding.inflate(inflater, parent, false)
        return ChatListViewHolder(binding)
    }

    override fun getItemCount(): Int = chatList.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        if (chatList[position].isCardView) {
            if (cardViewConfigModel != null) {
                holder.bindCardView(
                    context,
                    chatList,
                    position,
                    cardViewConfigModel
                )
            }
        } else {
            if (chatBubbleConfigModel != null) {
                holder.bindMessageView(
                    context,
                    chatList,
                    chatBubbleConfigModel,
                    position,
                    loaderList
                )
            }
        }
    }

    class ChatListViewHolder(private val itemBinding: ItemChatListBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        private val llParent: LinearLayout = itemBinding.llParent
        lateinit var chatBubbleLayout: RelativeLayout
        private val timeStampLayout: RelativeLayout = getLayoutFromInflater(
            itemBinding.llParent.context, R.layout.item_date_time_header,
            itemBinding.llParent, RelativeLayout::class.java
        )!!
        private val tvDateTimeStamp: CustomTextView =
            timeStampLayout.findViewById(R.id.tv_date_time_stamp)
        private val cvDateTimeStamp: CustomMaterialCardView =
            timeStampLayout.findViewById(R.id.cv_date_time_stamp)

        @SuppressLint("ClickableViewAccessibility")
        fun bindMessageView(
            context: Activity,
            chatList: ArrayList<MessageModel>,
            chatBubbleConfigModel: ChatBubbleConfigModel,
            position: Int,
            loaderList: ArrayList<Int>
        ) {
            val chatListModel = chatList[position]
            llParent.removeAllViews()

            showDateSectionHeader(chatList, position)

            val chatBubbleShape = when (chatBubbleConfigModel.chatBubbleStyle) {
                AppConstants.CHAT_BUBBLE_SLOPE -> R.layout.item_chat_bubble_image
                AppConstants.CHAT_BUBBLE_TALKIE_TOP -> R.layout.item_chat_bubble_image
                AppConstants.CHAT_BUBBLE_TALKIE_BOTTOM -> R.layout.item_chat_bubble_image
                AppConstants.CHAT_BUBBLE_WITH_DOTS -> {
                    if (chatListModel.isSender)
                        R.layout.sender_chatbubble_shape_circle_tail
                    else
                        R.layout.receiver_chatbubble_shape_circle_tail
                }
                else -> R.layout.item_chat_bubble_cardview
            }

            chatBubbleLayout = getLayoutFromInflater(
                context, chatBubbleShape,
                llParent, RelativeLayout::class.java
            )!!

            val cvChatBubble =
                chatBubbleLayout.findViewById<CustomMaterialCardView>(R.id.cv_chat_bubble)
            val tvChatBubble =
                chatBubbleLayout.findViewById<CustomTextView>(R.id.tv_chat_bubble)

            tvChatBubble.text = chatListModel.data
            tvChatBubble.textSize = getSizeInSDP(context, R.dimen._5ssp).toFloat()

            var llChatBubble: LinearLayout? = null
            if (chatBubbleShape == R.layout.item_chat_bubble_image) {
                llChatBubble = chatBubbleLayout.findViewById(R.id.ll_chat_bubble)
            }
            tvChatBubble.maxLines = MAX_LINES
            tvChatBubble.movementMethod = ScrollingMovementMethod()
            tvChatBubble.setOnTouchListener { v, _ -> // Disallow the touch request for parent scroll on touch of child view
                v?.parent?.requestDisallowInterceptTouchEvent(true)
                false
            }

            val smallCornerSize = getSizeInSDP(context, R.dimen._8sdp).toFloat()

            if (chatListModel.isSender) {
                tvChatBubble.setTextColor(getParsedColorValue(chatBubbleConfigModel.senderTextColor!!))

                if (chatBubbleShape == R.layout.item_chat_bubble_image) {
                    when (chatBubbleConfigModel.chatBubbleStyle) {
                        AppConstants.CHAT_BUBBLE_SLOPE -> {
                            llChatBubble?.setBackgroundResource(R.drawable.chat_bubble_shape_slant)
                        }
                        AppConstants.CHAT_BUBBLE_TALKIE_TOP -> {
                            llChatBubble?.setBackgroundResource(R.drawable.chat_bubble_shape_sender_top_tail)
                        }
                        AppConstants.CHAT_BUBBLE_TALKIE_BOTTOM -> {
                            llChatBubble?.setBackgroundResource(R.drawable.chat_bubble_shape_sender_bottom_tail)
                        }
                    }
                } else {
                    when (chatBubbleConfigModel.chatBubbleStyle) {
                        AppConstants.CHAT_BUBBLE_CORNER_CUT_UPPER -> {
                            cvChatBubble.setCornerFamily(CornerFamily.CUT)
                            cvChatBubble.setTopLeftCorner(smallCornerSize)
                        }
                        AppConstants.CHAT_BUBBLE_CORNER_CUT_LOWER -> {
                            cvChatBubble.setCornerFamily(CornerFamily.CUT)
                            cvChatBubble.setBottomLeftCorner(smallCornerSize)
                        }
                        AppConstants.CHAT_BUBBLE_CORNER_RADIUS -> {
                            cvChatBubble.setCornerFamily(CornerFamily.ROUNDED)
                            cvChatBubble.setAllCorners(smallCornerSize)
                        }
                        AppConstants.CHAT_BUBBLE_TWO_CORNER_RADIUS -> {
                            cvChatBubble.setCornerFamily(CornerFamily.ROUNDED)
                            cvChatBubble.setTopLeftCorner(smallCornerSize)
                            cvChatBubble.setBottomRightCorner(smallCornerSize)
                        }
                        AppConstants.CHAT_BUBBLE_ROUND -> {
                            cvChatBubble.setCornerFamily(CornerFamily.ROUNDED)
                            cvChatBubble.setAllCornersInPercent(0.5F)
                            setBothLeftRightMargin(cvChatBubble, tvChatBubble, 0.4F)
                        }
                        AppConstants.CHAT_BUBBLE_ARROW_INSIDE -> {
                            cvChatBubble.setCornerFamily(CornerFamily.CUT)
                            cvChatBubble.setTopRightCornerInPercent(0.5F)
                            cvChatBubble.setBottomRightCornerInPercent(0.5F)
                            setRightMargin(cvChatBubble, tvChatBubble, 0.5F)
                        }
                        AppConstants.CHAT_BUBBLE_ARROW_OUTSIDE -> {
                            cvChatBubble.setCornerFamily(CornerFamily.CUT)
                            cvChatBubble.setTopLeftCornerInPercent(0.5F)
                            cvChatBubble.setBottomLeftCornerInPercent(0.5F)
                            setLeftMargin(cvChatBubble, tvChatBubble, 0.5F)
                        }
                        AppConstants.CHAT_BUBBLE_ARROW_BOTH -> {
                            cvChatBubble.setCornerFamily(CornerFamily.CUT)
                            cvChatBubble.setAllCornersInPercent(0.5F)
                            setBothLeftRightMargin(cvChatBubble, tvChatBubble, 0.5F)
                        }
                        AppConstants.CHAT_BUBBLE_LEFT_OR_RIGHT_ROUND -> {
                            cvChatBubble.setCornerFamily(CornerFamily.ROUNDED)
                            cvChatBubble.setTopLeftCornerInPercent(0.5F)
                            cvChatBubble.setBottomLeftCornerInPercent(0.5F)
                            setLeftMargin(cvChatBubble, tvChatBubble, 0.4F)
                        }
                        AppConstants.CHAT_BUBBLE_WITH_DOTS -> {
                            cvChatBubble.setCornerFamily(CornerFamily.ROUNDED)
                            cvChatBubble.setAllCornersInPercent(0.5F)
                            setBothLeftRightMargin(cvChatBubble, tvChatBubble, 0.4F)
                            val tailCircle1: ShapeableImageView? =
                                chatBubbleLayout.findViewById(R.id.iv_circle_1)
                            val tailCircle2: ShapeableImageView? =
                                chatBubbleLayout.findViewById(R.id.iv_circle_3)
                            val tailCircle3: ShapeableImageView? =
                                chatBubbleLayout.findViewById(R.id.iv_circle_2)

                            val bgColor =
                                getParsedColorValue(chatBubbleConfigModel.senderChatBubbleColor!!)
                            tailCircle1?.setColorFilter(bgColor)
                            tailCircle2?.setColorFilter(bgColor)
                            tailCircle3?.setColorFilter(bgColor)
                        }
                    }
                }

                if (chatBubbleShape == R.layout.item_chat_bubble_image) {
                    llChatBubble?.background?.setTint(
                        getParsedColorValue(
                            chatBubbleConfigModel.senderChatBubbleColor!!
                        )
                    )
                } else {
                    cvChatBubble.setCardBackgroundColor(
                        getParsedColorValue(
                            chatBubbleConfigModel.senderChatBubbleColor!!
                        )
                    )
                    setElevationShadow(
                        context,
                        cvChatBubble,
                        chatBubbleConfigModel.cardBgDropShadow
                    )
                }

                val layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.setMargins(
                    getSizeInSDP(context, R.dimen._50sdp), getSizeInSDP(context, R.dimen._6sdp),
                    getSizeInSDP(context, R.dimen._6sdp), getSizeInSDP(context, R.dimen._6sdp)
                )

                if (chatBubbleShape == R.layout.item_chat_bubble_image) {
                    llChatBubble?.layoutParams = layoutParams
                } else if (chatBubbleShape == R.layout.item_chat_bubble_cardview) {
                    cvChatBubble.layoutParams = layoutParams
                }

                chatBubbleLayout.gravity = Gravity.END
                if (timeStampLayout.isVisible) {
                    llParent.addView(timeStampLayout)
                }
                llParent.addView(chatBubbleLayout)
            } else {
                tvChatBubble.setTextColor(getParsedColorValue(chatBubbleConfigModel.receiverTextColor!!))

                if (chatBubbleShape == R.layout.item_chat_bubble_image) {
                    when (chatBubbleConfigModel.chatBubbleStyle) {
                        AppConstants.CHAT_BUBBLE_SLOPE -> {
                            llChatBubble?.setBackgroundResource(R.drawable.chat_bubble_shape_slant)
                        }
                        AppConstants.CHAT_BUBBLE_TALKIE_TOP -> {
                            llChatBubble?.setBackgroundResource(R.drawable.chat_bubble_shape_receiver_top_tail)
                        }
                        AppConstants.CHAT_BUBBLE_TALKIE_BOTTOM -> {
                            llChatBubble?.setBackgroundResource(R.drawable.chat_bubble_shape_receiver_bottom_tail)
                        }
                    }
                } else {
                    when (chatBubbleConfigModel.chatBubbleStyle) {
                        AppConstants.CHAT_BUBBLE_CORNER_CUT_UPPER -> {
                            cvChatBubble.setCornerFamily(CornerFamily.CUT)
                            cvChatBubble.setTopRightCorner(smallCornerSize)
                        }
                        AppConstants.CHAT_BUBBLE_CORNER_CUT_LOWER -> {
                            cvChatBubble.setCornerFamily(CornerFamily.CUT)
                            cvChatBubble.setBottomRightCorner(smallCornerSize)
                        }
                        AppConstants.CHAT_BUBBLE_CORNER_RADIUS -> {
                            cvChatBubble.setCornerFamily(CornerFamily.ROUNDED)
                            cvChatBubble.setAllCorners(smallCornerSize)
                        }
                        AppConstants.CHAT_BUBBLE_TWO_CORNER_RADIUS -> {
                            cvChatBubble.setCornerFamily(CornerFamily.ROUNDED)
                            cvChatBubble.setTopLeftCorner(smallCornerSize)
                            cvChatBubble.setBottomRightCorner(smallCornerSize)
                        }
                        AppConstants.CHAT_BUBBLE_ROUND -> {
                            cvChatBubble.setCornerFamily(CornerFamily.ROUNDED)
                            cvChatBubble.setAllCornersInPercent(0.5F)
                            setBothLeftRightMargin(cvChatBubble, tvChatBubble, 0.4F)
                        }
                        AppConstants.CHAT_BUBBLE_ARROW_INSIDE -> {
                            cvChatBubble.setCornerFamily(CornerFamily.CUT)
                            cvChatBubble.setTopLeftCornerInPercent(0.5F)
                            cvChatBubble.setBottomLeftCornerInPercent(0.5F)
                            setLeftMargin(cvChatBubble, tvChatBubble, 0.5F)
                        }
                        AppConstants.CHAT_BUBBLE_ARROW_OUTSIDE -> {
                            cvChatBubble.setCornerFamily(CornerFamily.CUT)
                            cvChatBubble.setTopRightCornerInPercent(0.5F)
                            cvChatBubble.setBottomRightCornerInPercent(0.5F)
                            setRightMargin(cvChatBubble, tvChatBubble, 0.5F)
                        }
                        AppConstants.CHAT_BUBBLE_ARROW_BOTH -> {
                            cvChatBubble.setCornerFamily(CornerFamily.CUT)
                            cvChatBubble.setAllCornersInPercent(0.5F)
                            setBothLeftRightMargin(cvChatBubble, tvChatBubble, 0.5F)
                        }
                        AppConstants.CHAT_BUBBLE_LEFT_OR_RIGHT_ROUND -> {
                            cvChatBubble.setCornerFamily(CornerFamily.ROUNDED)
                            cvChatBubble.setTopRightCornerInPercent(0.5F)
                            cvChatBubble.setBottomRightCornerInPercent(0.5F)
                            setRightMargin(cvChatBubble, tvChatBubble, 0.4F)
                        }
                        AppConstants.CHAT_BUBBLE_WITH_DOTS -> {
                            cvChatBubble.setCornerFamily(CornerFamily.ROUNDED)
                            cvChatBubble.setAllCornersInPercent(0.5F)
                            setBothLeftRightMargin(cvChatBubble, tvChatBubble, 0.4F)
                            val tailCircle1: ShapeableImageView? =
                                chatBubbleLayout.findViewById(R.id.iv_circle_1)
                            val tailCircle2: ShapeableImageView? =
                                chatBubbleLayout.findViewById(R.id.iv_circle_3)
                            val tailCircle3: ShapeableImageView? =
                                chatBubbleLayout.findViewById(R.id.iv_circle_2)

                            val bgColor =
                                getParsedColorValue(chatBubbleConfigModel.receiverChatBubbleColor!!)
                            tailCircle1?.setColorFilter(bgColor)
                            tailCircle2?.setColorFilter(bgColor)
                            tailCircle3?.setColorFilter(bgColor)
                        }
                    }
                }

                if (chatBubbleShape == R.layout.item_chat_bubble_image) {
                    llChatBubble?.background?.setTint(
                        getParsedColorValue(chatBubbleConfigModel.receiverChatBubbleColor!!)
                    )
                } else {
                    cvChatBubble.setCardBackgroundColor(
                        getParsedColorValue(chatBubbleConfigModel.receiverChatBubbleColor!!)
                    )
                    setCardElevation(
                        context,
                        cvChatBubble,
                        chatBubbleConfigModel.cardBgDropShadow
                    )
                }

                val layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.setMargins(
                    getSizeInSDP(context, R.dimen._6sdp), getSizeInSDP(context, R.dimen._6sdp),
                    getSizeInSDP(context, R.dimen._50sdp), getSizeInSDP(context, R.dimen._6sdp)
                )

                if (chatBubbleShape == R.layout.item_chat_bubble_image) {
                    llChatBubble?.layoutParams = layoutParams
                } else if (chatBubbleShape == R.layout.item_chat_bubble_cardview) {
                    cvChatBubble.layoutParams = layoutParams
                }

                chatBubbleLayout.gravity = Gravity.START

                if (timeStampLayout.isVisible) {
                    llParent.addView(timeStampLayout)
                }
                llParent.addView(chatBubbleLayout)
            }
        }

        private fun showDateSectionHeader(chatList: ArrayList<MessageModel>, position: Int) {
            val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)
            val todayDate = sdf.format(Date())
            val cal = Calendar.getInstance()
            cal.add(Calendar.DATE, -1)
            val yesterdayDate = sdf.format(cal.time)

            if (position > 0) {
                if (chatList[position].date.equals((chatList[position - 1].date), true)) {
                    timeStampLayout.visibility = View.GONE
                } else {
                    timeStampLayout.visibility = View.VISIBLE
                }
            } else {
                timeStampLayout.visibility = View.VISIBLE
            }
            if (timeStampLayout.isVisible) {
                val dateTextValue: String = when (val date = chatList[position].date) {
                    todayDate ->
                        tvDateTimeStamp.context.getString(R.string.today)
                    yesterdayDate ->
                        tvDateTimeStamp.context.getString(R.string.yesterday)
                    else ->
                        date
                }
                tvDateTimeStamp.text = dateTextValue
            }
        }

        fun bindCardView(
            context: Activity,
            chatList: ArrayList<MessageModel>,
            position: Int,
            cardViewConfigModel: CardViewConfigModel
        ) {
            val cardViewParent: CustomMaterialCardView = itemBinding.cvParent
            val header: CustomMaterialButton = itemBinding.btnHeader
            val content: CustomTextView = itemBinding.tvContent
            val llContent: LinearLayout = itemBinding.llContent
            val footer: CustomMaterialButton = itemBinding.btnFooter

            cardViewParent.visibility = View.VISIBLE
            val messageModel = chatList[position]

            showDateSectionHeader(chatList, position)

            if (timeStampLayout.isVisible) {
                if (timeStampLayout.parent != null) {
                    (timeStampLayout.parent as ViewGroup).removeView(timeStampLayout)
                }
                llParent.addView(timeStampLayout, 0)
            }

            setCardElevation(context, cardViewParent, cardViewConfigModel.cardviewBgDropShadow)

            setStrokeColorAndWidth(
                cardViewParent, getParsedColorValue(cardViewConfigModel.cardviewBorderColor!!),
                cardViewConfigModel.cardviewBorderSize?.toInt()!!
            )
            changeBg(header, cardViewConfigModel.cardviewHeaderBgColor!!)
            changeBg(footer, cardViewConfigModel.cardviewFooterButtonBgColor!!)
            changeTextColor(header, cardViewConfigModel.cardviewHeaderTextColor!!)
            changeTextColor(
                footer,
                cardViewConfigModel.cardviewFooterButtonTextColor!!
            )

            header.text = messageModel.cardViewHeader
            footer.text = getStringFromXML(context, R.string.full_view)
            header.textSize = cardViewConfigModel.cardviewHeaderTextSize!!.toFloat()
            footer.textSize = cardViewConfigModel.cardviewFooterButtonTextSize!!.toFloat()
            content.gravity = Gravity.CENTER
            header.isAllCaps = false
            footer.isAllCaps = false

            if (messageModel.data == "attendance") {
                content.visibility = View.GONE
                val imageView = ImageView(context)
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
                params.setMargins(
                    getSizeInSDP(context, R.dimen._5sdp), getSizeInSDP(context, R.dimen._5sdp),
                    getSizeInSDP(context, R.dimen._5sdp), getSizeInSDP(context, R.dimen._5sdp)
                )
                imageView.layoutParams = params
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.attendance
                    )
                )
                llContent.addView(imageView)
            } else {
                content.visibility = View.VISIBLE
                content.text = messageModel.data
                content.setCustomFont(getFontsFromApp(context, R.string.Roboto_Regular))
            }

            changeTextColor(content, Color.BLACK)
            changeBg(llContent, Color.WHITE)

            cardViewParent.viewTreeObserver.addOnPreDrawListener(object :
                ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    cardViewParent.viewTreeObserver.removeOnPreDrawListener(this)
                    val headerHeight = header.height.toFloat()
                    val footerHeight = footer.height.toFloat()
                    when (cardViewConfigModel.cardviewShapeSelectedId) {
                        AppConstants.CARDVIEW_CORNER_RADIUS_SMALL -> {
                            cardViewParent.setCornerFamily(CornerFamily.ROUNDED)
                            cardViewParent.setTopLeftCorner(headerHeight / 2)
                            cardViewParent.setTopRightCorner(headerHeight / 2)
                            cardViewParent.setBottomLeftCorner(footerHeight / 2)
                            cardViewParent.setBottomRightCorner(footerHeight / 2)

                            header.setCornerFamily(CornerFamily.ROUNDED)
                            header.setTopLeftCorner(headerHeight / 2)
                            header.setTopRightCorner(headerHeight / 2)

                            footer.setCornerFamily(CornerFamily.ROUNDED)
                            footer.setBottomLeftCorner(footerHeight / 2)
                            footer.setBottomRightCorner(footerHeight / 2)
                        }
                        AppConstants.CARDVIEW_TOP_LEFT_BOTTOM_RIGHT_RADIUS_SMALL -> {
                            cardViewParent.setCornerFamily(CornerFamily.ROUNDED)
                            cardViewParent.setTopLeftCorner(headerHeight / 2)
                            cardViewParent.setBottomRightCorner(footerHeight / 2)

                            header.setCornerFamily(CornerFamily.ROUNDED)
                            header.setTopLeftCorner(headerHeight / 2)

                            footer.setCornerFamily(CornerFamily.ROUNDED)
                            footer.setBottomRightCorner(footerHeight / 2)
                        }
                        AppConstants.CARDVIEW_TOP_RIGHT_BOTTOM_LEFT_RADIUS_SMALL -> {
                            cardViewParent.setCornerFamily(CornerFamily.ROUNDED)
                            cardViewParent.setTopRightCorner(headerHeight / 2)
                            cardViewParent.setBottomLeftCorner(headerHeight / 2)

                            header.setCornerFamily(CornerFamily.ROUNDED)
                            header.setTopRightCorner(headerHeight / 2)

                            footer.setCornerFamily(CornerFamily.ROUNDED)
                            footer.setBottomLeftCorner(footerHeight / 2)
                        }
                        AppConstants.CARDVIEW_FOUR_CORNER_CUT_SMALL -> {
                            cardViewParent.setCornerFamily(CornerFamily.CUT)
                            cardViewParent.setTopLeftCorner(headerHeight / 2)
                            cardViewParent.setTopRightCorner(headerHeight / 2)
                            cardViewParent.setBottomLeftCorner(footerHeight / 2)
                            cardViewParent.setBottomRightCorner(footerHeight / 2)

                            header.setCornerFamily(CornerFamily.CUT)
                            header.setTopLeftCorner(headerHeight / 2)
                            header.setTopRightCorner(headerHeight / 2)

                            footer.setCornerFamily(CornerFamily.CUT)
                            footer.setBottomLeftCorner(footerHeight / 2)
                            footer.setBottomRightCorner(footerHeight / 2)
                        }
                        AppConstants.CARDVIEW_TOP_LEFT_BOTTOM_RIGHT_CUT_SMALL -> {
                            cardViewParent.setCornerFamily(CornerFamily.CUT)
                            cardViewParent.setTopLeftCorner(headerHeight / 2)
                            cardViewParent.setBottomRightCorner(footerHeight / 2)

                            header.setCornerFamily(CornerFamily.CUT)
                            header.setTopLeftCorner(headerHeight / 2)

                            footer.setCornerFamily(CornerFamily.CUT)
                            footer.setBottomRightCorner(footerHeight / 2)
                        }
                        AppConstants.CARDVIEW_TOP_RIGHT_BOTTOM_LEFT_CUT_SMALL -> {
                            cardViewParent.setCornerFamily(CornerFamily.CUT)
                            cardViewParent.setTopRightCorner(headerHeight / 2)
                            cardViewParent.setBottomLeftCorner(footerHeight / 2)

                            header.setCornerFamily(CornerFamily.CUT)
                            header.setTopRightCorner(headerHeight / 2)

                            footer.setCornerFamily(CornerFamily.CUT)
                            footer.setBottomLeftCorner(footerHeight / 2)
                        }
                        AppConstants.CARDVIEW_FOUR_CORNER_CUT_BIG -> {
                            cardViewParent.setCornerFamily(CornerFamily.CUT)
                            cardViewParent.setTopLeftCorner(headerHeight)
                            cardViewParent.setTopRightCorner(headerHeight)
                            cardViewParent.setBottomLeftCorner(footerHeight)
                            cardViewParent.setBottomRightCorner(footerHeight)

                            header.setCornerFamily(CornerFamily.CUT)
                            header.setTopLeftCorner(headerHeight)
                            header.setTopRightCorner(headerHeight)

                            footer.setCornerFamily(CornerFamily.CUT)
                            footer.setBottomLeftCorner(footerHeight)
                            footer.setBottomRightCorner(footerHeight)
                        }
                        AppConstants.CARDVIEW_TOP_LEFT_BOTTOM_RIGHT_CUT_BIG -> {
                            cardViewParent.setCornerFamily(CornerFamily.CUT)
                            cardViewParent.setTopLeftCorner(headerHeight)
                            cardViewParent.setBottomRightCorner(footerHeight)

                            header.setCornerFamily(CornerFamily.CUT)
                            header.setTopLeftCorner(headerHeight)

                            footer.setCornerFamily(CornerFamily.CUT)
                            footer.setBottomRightCorner(footerHeight)
                        }
                        AppConstants.CARDVIEW_TOP_RIGHT_BOTTOM_LEFT_CUT_BIG -> {
                            cardViewParent.setCornerFamily(CornerFamily.CUT)
                            cardViewParent.setTopRightCorner(headerHeight)
                            cardViewParent.setBottomLeftCorner(footerHeight)

                            header.setCornerFamily(CornerFamily.CUT)
                            header.setTopRightCorner(headerHeight)

                            footer.setCornerFamily(CornerFamily.CUT)
                            footer.setBottomLeftCorner(footerHeight)
                        }
                        AppConstants.CARDVIEW_CORNER_RADIUS_BIG -> {
                            cardViewParent.setCornerFamily(CornerFamily.ROUNDED)
                            cardViewParent.setTopLeftCorner(headerHeight)
                            cardViewParent.setTopRightCorner(headerHeight)
                            cardViewParent.setBottomLeftCorner(footerHeight)
                            cardViewParent.setBottomRightCorner(footerHeight)

                            header.setCornerFamily(CornerFamily.ROUNDED)
                            header.setTopLeftCorner(headerHeight)
                            header.setTopRightCorner(headerHeight)

                            footer.setCornerFamily(CornerFamily.ROUNDED)
                            footer.setBottomLeftCorner(footerHeight)
                            footer.setBottomRightCorner(footerHeight)
                        }
                        AppConstants.CARDVIEW_TOP_LEFT_BOTTOM_RIGHT_RADIUS_BIG -> {
                            cardViewParent.setCornerFamily(CornerFamily.ROUNDED)
                            cardViewParent.setTopLeftCorner(headerHeight)
                            cardViewParent.setBottomRightCorner(footerHeight)

                            header.setCornerFamily(CornerFamily.ROUNDED)
                            header.setTopLeftCorner(headerHeight)

                            footer.setCornerFamily(CornerFamily.ROUNDED)
                            footer.setBottomRightCorner(footerHeight)
                        }
                        AppConstants.CARDVIEW_TOP_RIGHT_BOTTOM_LEFT_RADIUS_BIG -> {
                            cardViewParent.setCornerFamily(CornerFamily.ROUNDED)
                            cardViewParent.setTopRightCorner(headerHeight)
                            cardViewParent.setBottomLeftCorner(footerHeight)

                            header.setCornerFamily(CornerFamily.ROUNDED)
                            header.setTopRightCorner(headerHeight)

                            footer.setCornerFamily(CornerFamily.ROUNDED)
                            footer.setBottomLeftCorner(footerHeight)
                        }
                    }
                    return false
                }
            })
        }

        private fun setLeftMargin(
            cvChatBubble: CustomMaterialCardView,
            tvChatBubble: CustomTextView,
            shapeSize: Float
        ) {
            val layoutParams: FrameLayout.LayoutParams =
                tvChatBubble.layoutParams as FrameLayout.LayoutParams

            cvChatBubble.viewTreeObserver.addOnPreDrawListener(object :
                ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    cvChatBubble.viewTreeObserver.removeOnPreDrawListener(this)
                    var totalLines = tvChatBubble.lineCount

                    layoutParams.leftMargin = (cvChatBubble.height * shapeSize).toInt()
                    tvChatBubble.layoutParams = layoutParams

                    tvChatBubble.viewTreeObserver.addOnPreDrawListener(object :
                        ViewTreeObserver.OnPreDrawListener {
                        override fun onPreDraw(): Boolean {
                            if (tvChatBubble.lineCount != totalLines) {
                                if (tvChatBubble.lineCount < MAX_LINES) {
                                    totalLines = tvChatBubble.lineCount
                                    layoutParams.leftMargin =
                                        (cvChatBubble.height * shapeSize).toInt()
                                    tvChatBubble.layoutParams = layoutParams
                                } else {
                                    layoutParams.leftMargin =
                                        (cvChatBubble.height * shapeSize).toInt()
                                    tvChatBubble.layoutParams = layoutParams
                                    tvChatBubble.viewTreeObserver.removeOnPreDrawListener(this)
                                }
                            } else {
                                tvChatBubble.viewTreeObserver.removeOnPreDrawListener(this)
                            }
                            return false
                        }
                    })
                    return false
                }
            })
        }

        private fun setRightMargin(
            cvChatBubble: CustomMaterialCardView,
            tvChatBubble: CustomTextView,
            shapeSize: Float
        ) {
            val layoutParams: FrameLayout.LayoutParams =
                tvChatBubble.layoutParams as FrameLayout.LayoutParams

            cvChatBubble.viewTreeObserver.addOnPreDrawListener(object :
                ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    cvChatBubble.viewTreeObserver.removeOnPreDrawListener(this)
                    var totalLines = tvChatBubble.lineCount

                    layoutParams.rightMargin = (cvChatBubble.height * shapeSize).toInt()
                    tvChatBubble.layoutParams = layoutParams

                    tvChatBubble.viewTreeObserver.addOnPreDrawListener(object :
                        ViewTreeObserver.OnPreDrawListener {
                        override fun onPreDraw(): Boolean {
                            if (tvChatBubble.lineCount != totalLines) {
                                if (tvChatBubble.lineCount < MAX_LINES) {
                                    totalLines = tvChatBubble.lineCount
                                    layoutParams.rightMargin =
                                        (cvChatBubble.height * shapeSize).toInt()
                                    tvChatBubble.layoutParams = layoutParams
                                } else {
                                    layoutParams.rightMargin =
                                        (cvChatBubble.height * shapeSize).toInt()
                                    tvChatBubble.layoutParams = layoutParams
                                    tvChatBubble.viewTreeObserver.removeOnPreDrawListener(this)
                                }
                            } else {
                                tvChatBubble.viewTreeObserver.removeOnPreDrawListener(this)
                            }
                            return false
                        }
                    })
                    return false
                }
            })
        }

        private fun setBothLeftRightMargin(
            cvChatBubble: CustomMaterialCardView,
            tvChatBubble: CustomTextView,
            shapeSize: Float
        ) {
            val layoutParams: FrameLayout.LayoutParams =
                tvChatBubble.layoutParams as FrameLayout.LayoutParams

            cvChatBubble.viewTreeObserver.addOnPreDrawListener(object :
                ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    cvChatBubble.viewTreeObserver.removeOnPreDrawListener(this)
                    var totalLines = tvChatBubble.lineCount

                    layoutParams.leftMargin = (cvChatBubble.height * shapeSize).toInt()
                    layoutParams.rightMargin = (cvChatBubble.height * shapeSize).toInt()
                    tvChatBubble.layoutParams = layoutParams

                    tvChatBubble.viewTreeObserver.addOnPreDrawListener(object :
                        ViewTreeObserver.OnPreDrawListener {
                        override fun onPreDraw(): Boolean {
                            if (tvChatBubble.lineCount != totalLines) {
                                if (tvChatBubble.lineCount < MAX_LINES) {
                                    totalLines = tvChatBubble.lineCount
                                    layoutParams.leftMargin =
                                        (cvChatBubble.height * shapeSize).toInt()
                                    layoutParams.rightMargin =
                                        (cvChatBubble.height * shapeSize).toInt()
                                    tvChatBubble.layoutParams = layoutParams
                                } else {
                                    layoutParams.leftMargin =
                                        (cvChatBubble.height * shapeSize).toInt()
                                    layoutParams.rightMargin =
                                        (cvChatBubble.height * shapeSize).toInt()
                                    tvChatBubble.layoutParams = layoutParams
                                    tvChatBubble.viewTreeObserver.removeOnPreDrawListener(this)
                                }
                            } else {
                                tvChatBubble.viewTreeObserver.removeOnPreDrawListener(this)
                            }
                            return false
                        }
                    })
                    return false
                }
            })
        }
    }
}