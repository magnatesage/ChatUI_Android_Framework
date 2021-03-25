package com.example.chatuilib.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.method.ScrollingMovementMethod
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.chatuilib.R
import com.example.chatuilib.customviews.CustomMaterialButton
import com.example.chatuilib.customviews.CustomMaterialCardView
import com.example.chatuilib.customviews.CustomTextView
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

const val MAX_LINES = 10

class ChatListAdapter(
    val context: Activity, private val chatList: ArrayList<MessageModel>,
    private val chatBubbleConfigModel: ChatBubbleConfigModel?,
    private val cardViewConfigModel: CardViewConfigModel?,
    private val loaderList: ArrayList<Int>
) : RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.item_chat_list, parent, false)
        return ChatListViewHolder(v)
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
                    chatList[position],
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

    class ChatListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var chatBubbleLayout: RelativeLayout

        @SuppressLint("ClickableViewAccessibility")
        fun bindMessageView(
            context: Activity,
            chatList: ArrayList<MessageModel>,
            chatBubbleConfigModel: ChatBubbleConfigModel,
            position: Int,
            loaderList: ArrayList<Int>
        ) {
            val llParent: LinearLayout = itemView.findViewById(R.id.ll_parent)
            val chatListModel = chatList[position]

            llParent.removeAllViews()

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

                if (position == chatList.size - 1 && !loaderList.contains(position)) {
                    val loaderLayout = getLayoutFromInflater(
                        context, R.layout.loader_image_view,
                        llParent, RelativeLayout::class.java
                    )
                    val imageView = loaderLayout!!.findViewById<ImageView>(R.id.iv_loader)

                    Glide.with(context).asGif().load(R.raw.loading)
                        .listener(object : RequestListener<GifDrawable?> {
                            override fun onLoadFailed(
                                @Nullable e: GlideException?,
                                model: Any?,
                                target: Target<GifDrawable?>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                return false
                            }

                            override fun onResourceReady(
                                resource: GifDrawable?,
                                model: Any?,
                                target: Target<GifDrawable?>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                resource?.setLoopCount(3)
                                resource?.registerAnimationCallback(object :
                                    Animatable2Compat.AnimationCallback() {
                                    override fun onAnimationEnd(drawable: Drawable) {
                                        llParent.removeAllViews()
                                        llParent.addView(chatBubbleLayout)
                                    }
                                })
                                return false
                            }
                        }).into(imageView)

                    llParent.addView(loaderLayout)
                    loaderList.add(position)
                } else {
                    llParent.addView(chatBubbleLayout)
                }
            }
        }

        fun bindCardView(
            context: Activity,
            messageModel: MessageModel,
            cardViewConfigModel: CardViewConfigModel
        ) {
            val cardViewParent: CustomMaterialCardView = itemView.findViewById(R.id.cv_parent)
            val header: CustomMaterialButton = itemView.findViewById(R.id.btn_header)
            val content: CustomTextView = itemView.findViewById(R.id.tv_content)
            val llContent: LinearLayout = itemView.findViewById(R.id.ll_content)
            val footer: CustomMaterialButton = itemView.findViewById(R.id.btn_footer)

            cardViewParent.visibility = View.VISIBLE

            setCardElevation(
                context,
                cardViewParent,
                cardViewConfigModel.cardviewBgDropShadow
            )

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
                imageView.setImageDrawable(context.getDrawable(R.drawable.attendance))
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