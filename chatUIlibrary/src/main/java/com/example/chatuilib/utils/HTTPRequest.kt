package com.example.chatuilib.utils

import android.os.Handler
import android.os.Looper
import com.example.chatuilib.listener.HTTPCallback
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection

internal class HTTPRequest(
    requestURL: String,
    params: HashMap<String, String>?,
    httpCallback: HTTPCallback?
) {
    private val executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())

    init {
        executor.execute {
            val url: URL
            var response: String = ""
            var responseCode = 0
            try {
                url = URL(requestURL)
                val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
                conn.readTimeout = 30000
                conn.connectTimeout = 60000
                conn.requestMethod = "POST"
                conn.doInput = true
                conn.doOutput = true
                val os: OutputStream = conn.outputStream
                val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
                writer.write(getPostDataString(params))
                writer.flush()
                writer.close()
                os.close()
                responseCode = conn.responseCode
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    var line: String?
                    val br = BufferedReader(InputStreamReader(conn.inputStream))
                    while (br.readLine().also { line = it } != null) {
                        response += line
                    }
                } else {
                    var line: String?
                    val br = BufferedReader(InputStreamReader(conn.inputStream))
                    while (br.readLine().also { line = it } != null) {
                        response += line
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            handler.post {
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    httpCallback!!.onSuccessResponse(response)
                } else {
                    httpCallback!!.onErrorResponse(responseCode, response)
                }
            }
        }
    }

    private fun getPostDataString(params: HashMap<String, String>?): String {
        val result = StringBuilder()
        var first = true
        try {
            for ((key, value) in params!!.entries) {
                if (first) first = false else result.append("&")
                result.append(URLEncoder.encode(key, "UTF-8"))
                result.append("=")
                result.append(URLEncoder.encode(value, "UTF-8"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result.toString()
    }
}