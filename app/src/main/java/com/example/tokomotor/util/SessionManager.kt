package com.example.tokomotor.util

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    // 🔹 SharedPreferences untuk simpan data user secara lokal
    private val prefs: SharedPreferences =
        context.getSharedPreferences(Constants.PREFS_USER, Context.MODE_PRIVATE)

    // 🔹 Simpan username & password ketika registrasi
    fun saveUser(username: String, password: String) {
        prefs.edit()
            .putString(Constants.KEY_USERNAME, username)
            .putString(Constants.KEY_PASSWORD, password)
            .apply()
    }

    // 🔹 Validasi login → cocokkan input dengan data yang disimpan
    fun isValidLogin(username: String, password: String): Boolean {
        val u = prefs.getString(Constants.KEY_USERNAME, null)
        val p = prefs.getString(Constants.KEY_PASSWORD, null)
        return u == username && p == password
    }

    // 🔹 Simpan status login (true/false)
    fun setLoggedIn(isLoggedIn: Boolean) {
        prefs.edit().putBoolean(Constants.KEY_IS_LOGGED_IN, isLoggedIn).apply()
    }

    // 🔹 Cek apakah user sudah login atau belum
    fun isLoggedIn(): Boolean = prefs.getBoolean(Constants.KEY_IS_LOGGED_IN, false)

    // 🔹 Logout → set status login ke false
    fun logout() {
        prefs.edit().putBoolean(Constants.KEY_IS_LOGGED_IN, false).apply()
    }
}
