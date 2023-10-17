package com.example.learningcenter.appconfig

import android.content.Context
import android.content.SharedPreferences
import com.example.learningcenter.R
import com.example.learningcenter.ui.LoginFragment

class AppConfig() {
    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferences


    constructor(context: Context) : this() {
        this.context = context
        sharedPreferences = context.getSharedPreferences(
            context.getString(R.string.pref_file_key),
            Context.MODE_PRIVATE
        )
    }

    fun isUserLogin(): Boolean {
        return sharedPreferences!!.getBoolean(
            context!!.getString(R.string.pref_is_user_login),
            false
        )
    }

    fun updateUserLoginStatus(status: Boolean) {
        val editor = sharedPreferences!!.edit()
        editor.putBoolean(context!!.getString(R.string.pref_is_user_login), status)
        editor.apply()
    }

    fun saveNameofUser(name: String?) {
        val editor = sharedPreferences!!.edit()
        editor.putString(context!!.getString(R.string.pref_name_of_user), name)
        editor.apply()
    }

    fun getNameOfUser(): String? {
        return sharedPreferences!!.getString(
            context!!.getString(R.string.pref_name_of_user),
            "Unknown"
        )
    }
}