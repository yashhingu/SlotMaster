package com.app.demoslotmachne.utils

import android.content.Context
import java.util.Calendar

object PrefHelper {
    private const val PREF_NAME = "casumo_pref"
    private const val KEY_LAST_CLICK_DATE = "last_click_date"
    private const val KEY_COIN_BALANCE = "coin_balance"

    fun hasClickedToday(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val lastClick = prefs.getLong(KEY_LAST_CLICK_DATE, 0L)

        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        return lastClick >= today
    }

    fun setClickedToday(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putLong(KEY_LAST_CLICK_DATE, System.currentTimeMillis()).apply()
    }

    fun getCoins(context: Context): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_COIN_BALANCE, 1000)
    }

    fun addCoins(context: Context, amount: Int) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val current = prefs.getInt(KEY_COIN_BALANCE, 1000)
        prefs.edit().putInt(KEY_COIN_BALANCE, current + amount).apply()
    }
}