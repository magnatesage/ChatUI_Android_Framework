package com.example.chatuilib.utils

import android.content.Context
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.example.chatuilib.R
import com.example.chatuilib.listener.ResponseListener
import com.example.chatuilib.model.ResponseModel
import org.json.JSONObject
import java.util.*


/**
 * This class is used to call API
 */
object CallApi {
    private val TAG = CallApi::class.java.simpleName

    /**
     * This method is used to call post API
     * @param context the context is used to show progressbar and alert dialog
     * @param url the url string of API endpoint
     * @param params the params for post API call
     * @param showProgressBar the boolean to show progressbar or not
     * @param responseListener the response listener to notify API response
     */
    fun callPostApi(
        context: Context,
        url: String,
        params: HashMap<String, String>,
        showProgressBar: Boolean,
        responseListener: ResponseListener
    ) {
        if (showProgressBar) {
            Utils.showProgressBar(context)
        }

        AndroidNetworking.post(Endpoints.BASE_URL + url)
            .addBodyParameter(params)
            .build()
            .getAsObject(ResponseModel::class.java, object : ParsedRequestListener<ResponseModel> {
                override fun onResponse(response: ResponseModel?) {
                    if (showProgressBar) {
                        Utils.dismissProgressBar()
                    }
                    if (response?.status == "success" && response.status_code == 200) {
                        responseListener.onSuccessResponse(response.data)
                    } else {
                        AlertDialogView.showAlert(
                            context,
                            context.getString(R.string.app_name),
                            context.getString(R.string.error_api_msg)
                            , context.getString(R.string.ok)
                        )?.show()
                        responseListener.onErrorResponse(context.getString(R.string.error_api_msg))
                    }
                }

                override fun onError(anError: ANError?) {
                    if (showProgressBar) {
                        Utils.dismissProgressBar()
                    }
                    var message: String = context.getString(R.string.error_api_msg)
                    if (anError?.errorCode == 0) {
                        message = anError.errorDetail
                    } else {
                        val jsonObject = JSONObject(anError?.errorBody)
                        val errorObj = jsonObject.optJSONObject("error")
                        if (errorObj != null) {
                            val stringArray = errorObj.optJSONArray("application_id")
                            if (stringArray != null && stringArray.length() > 0) {
                                message = stringArray[0].toString()
                            }
                        }
                    }
                    AlertDialogView.showAlert(
                        context,
                        context.getString(R.string.app_name),
                        message,
                        context.getString(R.string.ok)
                    )?.show()
                    responseListener.onErrorResponse(anError)
                }

            })
    }
}
