package com.example.chatuilib.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import com.example.chatuilib.R

/**
 * This AlertDialogView class is used to show alert
 */
internal object AlertDialogView {
    const val FIRST_BUTTON = 1
    const val SECOND_BUTTON = 2
    var dialog: Dialog? = null

    /**
     * To show alert dialog with only one button
     * @param context the context in which the dialog should run
     * @param title the title of alert dialog
     * @param message the message of alert dialog
     * @param firstButtonText the text of button
     * @return Dialog
     */
    fun showAlert(
        context: Context,
        title: String,
        message: String,
        firstButtonText: String
    ): Dialog? {
        return showAlertDialog(context, title, message, firstButtonText, "", null)
    }

    /**
     * To show alert dialog with only one button
     * @param context the context in which the dialog should run
     * @param title the title of alert dialog
     * @param message the message of alert dialog
     * @param firstButtonText the text of button
     * @param clickListener click listener to notify click of button
     * @return Dialog
     */
    fun showAlert(
        context: Context,
        title: String,
        message: String,
        firstButtonText: String,
        clickListener: OnAlertDialogViewButtonClickListener?
    ): Dialog? {
        return showAlertDialog(
            context,
            title,
            message,
            firstButtonText,
            "",
            clickListener
        )
    }

    /**
     * To show alert dialog with only one button
     * @param context the context in which the dialog should run
     * @param title the title of alert dialog
     * @param message the message of alert dialog
     * @param firstButtonText the text of first button
     * @param secondButtonText the text of second button
     * @param clickListener click listener to notify click of button
     * @return Dialog
     */
    fun showAlert(
        context: Context,
        title: String,
        message: String,
        firstButtonText: String,
        secondButtonText: String,
        clickListener: OnAlertDialogViewButtonClickListener?
    ): Dialog? {
        return showAlertDialog(
            context,
            title,
            message,
            firstButtonText,
            secondButtonText,
            clickListener
        )
    }

    /**
     * To show alert dialog with only one button
     * @param context the context in which the dialog should run
     * @param title the title of alert dialog
     * @param message the message of alert dialog
     * @param firstButtonText the text of first button
     * @param secondButtonText the text of second button
     * @param clickListener click listener to notify click of button
     * @return Dialog
     */
    private fun showAlertDialog(
        context: Context,
        title: String,
        message: String,
        firstButtonText: String,
        secondButtonText: String,
        clickListener: OnAlertDialogViewButtonClickListener?
    ): Dialog? {
        try {
            try {
                if (dialog != null) {
                    dialog!!.dismiss()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            dialog = Dialog(context)
            dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog!!.setContentView(R.layout.dialog_alert_view)
            dialog!!.setCancelable(true)
            dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.setOnKeyListener { dialog, keyCode, event ->
                if (keyCode === KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss()
                }
                true
            }

            val tvTitle: TextView = dialog!!.findViewById(R.id.tv_title)
            val tvMessage: TextView = dialog!!.findViewById(R.id.tv_message)
            val tvFirstButton: TextView = dialog!!.findViewById(R.id.tv_first_button)
            val tvSecondButton: TextView = dialog!!.findViewById(R.id.tv_second_button)
            val view: View = dialog!!.findViewById(R.id.view)

            tvTitle.text = title
            tvMessage.text = message
            tvFirstButton.text = firstButtonText
            tvFirstButton.setOnClickListener { v: View? ->
                clickListener?.onAlertDialogViewButtonClick(
                    FIRST_BUTTON
                )
                dialog!!.dismiss()
            }
            if (secondButtonText != "") {
                view.visibility = View.VISIBLE
                tvSecondButton.visibility = View.VISIBLE
                tvSecondButton.text = secondButtonText
                tvSecondButton.setOnClickListener { v: View? ->
                    clickListener?.onAlertDialogViewButtonClick(
                        SECOND_BUTTON
                    )
                    dialog!!.dismiss()
                }
            } else {
                tvSecondButton.visibility = View.GONE
                view.visibility = View.GONE
            }

            dialog!!.getWindow()?.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            )
            dialog!!.setOnShowListener {
                try {
                    dialog!!.window?.decorView?.setSystemUiVisibility(
                        (context as Activity).window.decorView.systemUiVisibility
                    )
                    dialog!!.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dialog
    }

    /**
     * Interface for returning click event of button with button index to identify which button is clicked
     */
    interface OnAlertDialogViewButtonClickListener {
        fun onAlertDialogViewButtonClick(buttonIndex: Int)
    }
}
