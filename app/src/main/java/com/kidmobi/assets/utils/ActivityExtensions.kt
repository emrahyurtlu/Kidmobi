package com.kidmobi.assets.utils

import android.app.Activity
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat

fun Activity.hideSoftKeyboard() {
    currentFocus?.let {
        val inputMethodManager =
            ContextCompat.getSystemService(this, InputMethodManager::class.java)!!
        inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

fun Activity.goto(target: Class<*>){
    Intent(this, target).also {
        this.startActivity(it)
    }
}