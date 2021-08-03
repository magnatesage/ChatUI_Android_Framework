package com.example.chatuilib.adapter

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.chatuilib.R
import com.example.chatuilib.activity.ChatActivity
import com.example.chatuilib.customviews.CustomMaterialButton
import com.example.chatuilib.listener.OnButtonClickListener
import com.example.chatuilib.model.ButtonConfigModel
import com.example.chatuilib.utils.AppConstants
import com.example.chatuilib.utils.Utils.changeBg
import com.example.chatuilib.utils.Utils.changeTextColor
import com.example.chatuilib.utils.Utils.getParsedColorValue
import com.example.chatuilib.utils.Utils.setElevationShadow
import com.example.chatuilib.utils.Utils.setStrokeColorAndWidth
import com.google.android.material.shape.CornerFamily
import java.util.*

class ChatButtonListAdapter(
    val context: Activity,
    private val buttonConfigModel: ButtonConfigModel?,
    private val buttonTitleList: ArrayList<String>,
    private val fontType: String?,
) : RecyclerView.Adapter<ChatButtonListAdapter.ChatButtonListViewHolder>() {

    var onButtonClickListener: OnButtonClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatButtonListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.lib_item_chat_button, parent, false)
        return ChatButtonListViewHolder(v)
    }

    override fun getItemCount(): Int = buttonTitleList.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holderChat: ChatButtonListViewHolder, position: Int) {
        if (buttonConfigModel != null) {
            holderChat.bind(
                context,
                buttonConfigModel,
                buttonTitleList[position],
                onButtonClickListener,
                fontType
            )
        }
    }

    class ChatButtonListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            context: Activity,
            buttonConfigModel: ButtonConfigModel,
            buttonTitle: String,
            onButtonClickListener: OnButtonClickListener?,
            fontType: String?,
        ) {
            val parentLayout: RelativeLayout = itemView.findViewById(R.id.rl_parent)
            val customMaterialButton: CustomMaterialButton =
                itemView.findViewById(R.id.custom_material_button)

            customMaterialButton.text = buttonTitle

            if (buttonConfigModel.buttonPlacementStyle?.lowercase(Locale.ROOT) == AppConstants.HORIZONTAL) {
                val params = parentLayout.layoutParams
                params.width = ViewGroup.LayoutParams.WRAP_CONTENT
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT
                parentLayout.layoutParams = params
            }

            customMaterialButton.setCustomFont("$fontType.ttf")
            setElevationShadow(
                context,
                customMaterialButton,
                buttonConfigModel.buttonBgDropShadow
            )
            setButtonNormal(customMaterialButton, buttonConfigModel)

            customMaterialButton.setOnClickListener {
                setButtonClicked(customMaterialButton, buttonConfigModel)
                (context as ChatActivity).rlConversationBar.visibility = View.VISIBLE
                context.rlButtonList.visibility = View.GONE
                Handler(Looper.getMainLooper()).postDelayed({
                    onButtonClickListener?.onButtonClick(customMaterialButton.text.toString())
                }, 1000)
            }

            when (buttonConfigModel.buttonShapeId) {
                AppConstants.BUTTON_CORNER_RADIUS -> {
                    customMaterialButton.setCornerFamily(CornerFamily.ROUNDED)
                    customMaterialButton.setAllCornersInPercent(0.25F)
                }
                AppConstants.BUTTON_ROUND -> {
                    customMaterialButton.setCornerFamily(CornerFamily.ROUNDED)
                    customMaterialButton.setAllCornersInPercent(0.5F)
                }
                AppConstants.BUTTON_TOP_LEFT_CUT -> {
                    customMaterialButton.setCornerFamily(CornerFamily.CUT)
                    customMaterialButton.setTopLeftCornerInPercent(0.25F)
                }
                AppConstants.BUTTON_BOTTOM_LEFT_CUT -> {
                    customMaterialButton.setCornerFamily(CornerFamily.CUT)
                    customMaterialButton.setBottomLeftCornerInPercent(0.25F)
                }
                AppConstants.BUTTON_TOP_RIGHT_BOTTOM_LEFT_RADIUS -> {
                    customMaterialButton.setCornerFamily(CornerFamily.ROUNDED)
                    customMaterialButton.setTopRightCornerInPercent(0.25F)
                    customMaterialButton.setBottomLeftCornerInPercent(0.25F)
                }
                AppConstants.BUTTON_ARROW_RIGHT_INSIDE -> {
                    customMaterialButton.setCornerFamily(CornerFamily.CUT)
                    customMaterialButton.setTopRightCornerInPercent(0.5F)
                    customMaterialButton.setBottomRightCornerInPercent(0.5F)
                }
                AppConstants.BUTTON_ARROW_BOTH -> {
                    customMaterialButton.setCornerFamily(CornerFamily.CUT)
                    customMaterialButton.setAllCornersInPercent(0.5F)
                }
                AppConstants.BUTTON_ARROW_LEFT_INSIDE -> {
                    customMaterialButton.setCornerFamily(CornerFamily.CUT)
                    customMaterialButton.setTopLeftCornerInPercent(0.5F)
                    customMaterialButton.setBottomLeftCornerInPercent(0.5F)
                }
                AppConstants.BUTTON_LEFT_ROUND -> {
                    customMaterialButton.setCornerFamily(CornerFamily.ROUNDED)
                    customMaterialButton.setTopLeftCornerInPercent(0.5F)
                    customMaterialButton.setBottomLeftCornerInPercent(0.5F)
                }
                AppConstants.BUTTON_RIGHT_ROUND -> {
                    customMaterialButton.setCornerFamily(CornerFamily.ROUNDED)
                    customMaterialButton.setTopRightCornerInPercent(0.5F)
                    customMaterialButton.setBottomRightCornerInPercent(0.5F)
                }
            }
        }

        private fun setButtonNormal(
            buttonShapeLayout: CustomMaterialButton,
            buttonConfigModel: ButtonConfigModel
        ) {
            val normalButtonColor = getParsedColorValue(buttonConfigModel.normalButtonColor!!)
            val normalTextColor = getParsedColorValue(buttonConfigModel.normalTextColor!!)
            val normalBorderColor = getParsedColorValue(buttonConfigModel.normalBorderColor!!)
            val normalBorderSize = buttonConfigModel.normalBorderSize?.toInt()
            makeChangesToButton(
                buttonShapeLayout, normalButtonColor, normalTextColor,
                normalBorderColor, normalBorderSize!!
            )
        }

        private fun makeChangesToButton(
            buttonShapeLayout: CustomMaterialButton, buttonColor: Int,
            textColor: Int, borderColor: Int, borderSize: Int
        ) {
            changeBg(buttonShapeLayout, buttonColor)
            changeTextColor(buttonShapeLayout, textColor)
            setStrokeColorAndWidth(buttonShapeLayout, borderColor, borderSize)
        }

        private fun setButtonClicked(
            buttonShapeLayout: CustomMaterialButton,
            buttonConfigModel: ButtonConfigModel
        ) {
            val clickedButtonColor = getParsedColorValue(buttonConfigModel.clickedButtonColor!!)
            val clickedTextColor = getParsedColorValue(buttonConfigModel.clickedTextColor!!)
            val clickedBorderColor = getParsedColorValue(buttonConfigModel.clickedBorderColor!!)
            val clickedBorderSize = buttonConfigModel.clickedBorderSize?.toInt()
            makeChangesToButton(
                buttonShapeLayout, clickedButtonColor, clickedTextColor,
                clickedBorderColor, clickedBorderSize!!
            )
        }
    }

    fun setButtonListClickListener(onButtonClickListener: OnButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener
    }

}