package com.example.chatuilib.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.GradientDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.chatuilib.R
import com.example.chatuilib.customviews.CustomButton
import com.example.chatuilib.customviews.CustomEditText
import com.example.chatuilib.customviews.CustomShapeableImageView
import com.example.chatuilib.customviews.CustomTextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject
import java.io.IOException
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.concurrent.Executors

/**
 * This class is used for global functions used in entire application
 */
internal object Utils {

    private var dialog: Dialog? = null

    /**
     * This method is used to show Progressbar
     * @param context the context is used to create dialog
     */
    fun showProgressBar(context: Context) {
        if (dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss()
            dialog = null
        }
        dialog = Dialog(context)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.lib_custom_progress_dialog)
        dialog!!.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setCancelable(false)
        dialog!!.show()
    }

    /**
     * This method is used to dismiss Progressbar
     */
    fun dismissProgressBar() {
        if (dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss()
            dialog = null
        }
    }

    /**
     * This method is used to convert size in sdp
     * @param context the context is used to get resources
     * @param dimenSize the size from dimen class
     * @return size in sdp
     */
    fun getSizeInSDP(context: Context, dimenSize: Int): Int {
        return context.resources.getDimensionPixelSize(dimenSize)
    }

    /**
     * This method will return predefined string value from Strings.xml
     * @param context the activity context is used to get resources
     * @param stringValue the id of string from string class
     * @return the string from strings.xml
     */
    fun getStringFromXML(context: Activity, stringValue: Int): String {
        return context.resources.getString(stringValue)
    }

    /**
     * This method will return a view that developer wants to inflate at runtime
     * This is generic method & can be used for any type of layout to inflate at runtime
     * @param shape the id of layout
     * @param parent the parent layout
     * @param aClass the class that will be inflated
     * @return T
     */
    fun <T> getLayoutFromInflater(
        context: Context,
        shape: Int,
        parent: ViewGroup,
        aClass: Class<T>
    ): T? {
        val inflater = LayoutInflater.from(context)
        return aClass.cast(inflater.inflate(shape, parent, false))
    }

    /**
     * This method will return Integer value of Color
     * @param color the string value of color
     * @return integer value of color
     */
    fun getParsedColorValue(color: String): Int {
        return Color.parseColor(color)
    }

    /**
     * This method is used to set shadow to particular CardView
     * @param view the cardview to set shadow
     * @param showShadow: Boolean - to show shadow or not
     */
    fun setCardElevation(context: Context, view: CardView, showShadow: Boolean) {
        if (showShadow) {
            view.cardElevation = getSizeInSDP(context, R.dimen._5sdp).toFloat()
        } else {
            view.cardElevation = 0F
        }
    }

    /**
     * This method is used to change Background Color to the particular view
     * @param view the view to set background color
     * @param newColor the string background color
     */
    fun changeBg(view: View, newColor: String) {
        view.setBackgroundColor(getParsedColorValue(newColor))
    }

    /**
     * This method is used to change Background Color to the particular view
     * @param view the view to set background color
     * @param newColor the integer background color
     */
    fun changeBg(view: View, newColor: Int) {
        view.setBackgroundColor(newColor)
    }

    /**
     * This method is used to change text color to the particular view
     * @param view the view to set text color
     * @param newColor the string text color
     */
    fun changeTextColor(view: View, newColor: String) {
        (view as TextView).setTextColor(getParsedColorValue(newColor))
    }

    /**
     * This method is used to change text color to the particular view
     * @param view the view to set text color
     * @param newColor the integer text color
     */
    fun changeTextColor(view: View, newColor: Int) {
        (view as TextView).setTextColor(newColor)
    }

    /**
     * This method is used to get predefined font ttf file from assets/fonts folder.
     * @param font the id of font name from strings.xml
     * @return font name
     */
    fun getFontsFromApp(context: Activity, font: Int): String {
        return context.resources.getString(font)
    }

    /**
     * This method is used to set text size in SSP
     * And make text caps as false
     * And Set text in Center.
     * @param dimenSize the size from dimen class
     */
    fun setTextSizeInSSPAlignmentCaps(view: View, context: Activity, dimenSize: Int) {
        (view as TextView).setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            context.resources.getDimensionPixelSize(dimenSize).toFloat()
        )
        view.isAllCaps = false
        view.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY)
        view.gravity = Gravity.CENTER
    }

    /**
     * This method is used to set shadow to particular view
     * @param view the view to set shadow
     * @param showShadow: Boolean - to show shadow or not
     */
    fun setElevationShadow(context: Activity, view: View, showShadow: Boolean) {
        if (showShadow) {
            view.elevation = getSizeInSDP(context, R.dimen._5sdp).toFloat()
        } else {
            view.elevation = 0F
        }
    }

    /**
     * This method is used to set border color and border width to particular view
     * @param view the view to set border color and border width
     * @param strokeColor the integer border color value
     * @param strokeWidth the integer border width
     */
    fun setStrokeColorAndWidth(view: View, strokeColor: Int, strokeWidth: Int) {
        if (view is MaterialButton) {
            view.strokeColor = ColorStateList.valueOf(strokeColor)
            view.strokeWidth = strokeWidth
        } else if (view is MaterialCardView) {
            view.strokeColor = strokeColor
            view.strokeWidth = strokeWidth
        }
    }

    /**
     * This method is used to get predefined Integer value of Color from Colors.xml
     * @param id the id of color
     * @return integer color value from Colors.xml
     */
    fun getDesiredColorFromXML(context: Activity, id: Int): Int {
        return ContextCompat.getColor(context, id)
    }

    /**
     * This method is used to set background color of drawable to the particular view
     * @param view the view to set background color
     * @param selectedColor the integer color value
     */
    fun setBackgroundColorOfDrawable(view: View, selectedColor: Int) {
        if (checkForOsVersionIsNotLollipop()) {
            view.background.setTint(selectedColor)
        } else {
            val gDrawable = view.background as GradientDrawable
            gDrawable.setColor(selectedColor)
        }
    }

    /**
     * This method will check that current version of os is less than 5.0 or greater than 5.0
     */
    fun checkForOsVersionIsNotLollipop(): Boolean {
        val osVersion = Build.VERSION.SDK_INT
        var isNotLollipop = false
        if (osVersion > Build.VERSION_CODES.LOLLIPOP) {
            isNotLollipop = true
        } else if (osVersion == Build.VERSION_CODES.LOLLIPOP || osVersion == Build.VERSION_CODES.LOLLIPOP_MR1) {
            isNotLollipop = false
        }
        return isNotLollipop
    }

    /**
     * This method is used to hide keyboard
     * @param view the view is used to get window token
     */
    fun hideKeyboard(view: View) {
        val inputMethodManager =
            view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * This method is used to load Image to ImageView using Executor
     * @param context Context of Activity or Fragment
     * @param imageUrl the string image url
     * @param imageView the view to load image
     * @param placeHolder the id of the resource to use as a placeholder and error holder
     */
    fun loadImageWithExecutor(
        imageUrl: String,
        imageView: CustomShapeableImageView,
        placeHolder: Int
    ) {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        imageView.setImageResource(placeHolder)
        executor.execute {
            val url = URL(imageUrl)
            val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            handler.post {
                imageView.setImageBitmap(bmp);
            }
        }
    }

    fun loadImageWithExecutor(
        imageUrl: String,
        recyclerView: RecyclerView,
        placeHolderId: Int
    ) {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        recyclerView.setBackgroundColor(Color.WHITE)
        executor.execute {
            val url = URL(imageUrl)
            val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            val drawable = BitmapDrawable(recyclerView.context.resources, bmp)
            handler.post {
                recyclerView.background = drawable
            }
        }
    }

    /**
     * This method is used to check internet connection
     * @param context
     * @param informToUser Boolean - to inform user via alert dialog or not
     * @return Boolean - Whether there is an internet connection
     */
    fun isNetworkAvailable(context: Context, informToUser: Boolean): Boolean {
        var isConnected = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val activeNetwork =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            isConnected = when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                activeNetworkInfo?.run {
                    isConnected = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
        }

        if (informToUser && !isConnected) {
            AlertDialogView.showAlert(
                context,
                context.getString(R.string.lib_app_name),
                context.getString(R.string.lib_error_internet_connection),
                context.getString(R.string.lib_ok)
            )?.show()
        }

        return isConnected
    }

    /**
     * This method will set Text size in SSP
     * @param dimenSize: Int
     */
    fun setTextSizeInSSP(view: View, dimenSize: Int) {
        val fontSize = getSizeInSDP(view.context, dimenSize).toFloat()
        when (view) {
            is CustomTextView -> {
                view.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    fontSize
                )
            }
            is CustomEditText -> {
                view.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    fontSize
                )
            }
            is CustomButton -> {
                view.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    fontSize
                )
            }
        }
    }

    /**
     * This method used to get size in ssp
     * @param fontSize: String
     */
    fun getFontSizeInSSP(fontSize: String): Int {
        return when (fontSize) {
            "5" -> R.dimen._5ssp
            "6" -> R.dimen._6ssp
            "7" -> R.dimen._7ssp
            "8" -> R.dimen._8ssp
            "9" -> R.dimen._9ssp
            "10" -> R.dimen._10ssp
            "11" -> R.dimen._11ssp
            "12" -> R.dimen._12ssp
            "13" -> R.dimen._13ssp
            "14" -> R.dimen._14ssp
            "15" -> R.dimen._15ssp
            "16" -> R.dimen._16ssp
            "17" -> R.dimen._17ssp
            "18" -> R.dimen._18ssp
            "19" -> R.dimen._19ssp
            "20" -> R.dimen._20ssp
            "21" -> R.dimen._21ssp
            "22" -> R.dimen._22ssp
            "23" -> R.dimen._23ssp
            "24" -> R.dimen._24ssp
            "25" -> R.dimen._25ssp
            "26" -> R.dimen._26ssp
            "27" -> R.dimen._27ssp
            "28" -> R.dimen._28ssp
            "29" -> R.dimen._29ssp
            "30" -> R.dimen._30ssp
            else -> R.dimen._10ssp
        }
    }

    /**
     * This method is used to get default json object
     *
     * @param activity
     */
    fun getJSONObject(activity: Activity): JSONObject {
        var json = ""
        try {
            val inputStream = activity.assets.open("lib_default.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, StandardCharsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return JSONObject(json)
    }
}