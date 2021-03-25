package com.example.chatuilib.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * This class is used for Application Shared Preferences
 */
object AppPref {
    private var sharedPreferences: SharedPreferences? = null

    /**
     * This method is used to initialize shared preferences
     * @param context the context is used to initialize shared preferences
     */
    private fun openPref(context: Context) {
        sharedPreferences = context.getSharedPreferences(
            "chatLibraryPref",
            Context.MODE_PRIVATE
        )
    }

    /**
     * This method is used to get string preference value
     * @param context the context is used to initialize shared Preference
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return Returns the string preference value if it exists, or defaultValue
     */
    fun getValue(
        context: Context, key: String?,
        defaultValue: String?
    ): String? {
        openPref(context)
        val result = sharedPreferences!!.getString(key, defaultValue)
        sharedPreferences = null
        return result
    }

    /**
     * This method is used to set string preference value
     * @param context the context is used to initialize shared Preference
     * @param key The name of the preference to modify
     * @param value The new value for the preference.  Passing {@code null}
     *    for this argument is equivalent to calling {@link #remove(String)} with
     *    this key.
     */
    fun setValue(context: Context, key: String?, value: String?) {
        openPref(context)
        val prefsPrivateEditor = sharedPreferences!!.edit()
        prefsPrivateEditor.putString(key, value)
        prefsPrivateEditor.apply()
        sharedPreferences = null
    }

    /**
     * This method is used to get integer preference value
     * @param context the context is used to initialize shared Preference
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return Returns the integer preference value if it exists, or defaultValue
     */
    fun getValue(context: Context, key: String?, defaultValue: Int): Int {
        openPref(context)
        val result = sharedPreferences!!.getInt(key, defaultValue)
        sharedPreferences = null
        return result
    }

    /**
     * This method is used to set integer preference value
     * @param context the context is used to initialize shared Preference
     * @param key The name of the preference to modify
     * @param value The new value for the preference.  Passing {@code null}
     *    for this argument is equivalent to calling {@link #remove(String)} with
     *    this key.
     */
    fun setValue(context: Context, key: String?, value: Int) {
        openPref(context)
        val prefsPrivateEditor = sharedPreferences!!.edit()
        prefsPrivateEditor.putInt(key, value)
        prefsPrivateEditor.apply()
        sharedPreferences = null
    }
}
