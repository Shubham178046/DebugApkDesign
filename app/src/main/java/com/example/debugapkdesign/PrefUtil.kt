package com.example.debugapkdesign

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

class PrefUtil(context: Context) {
    var DOCUMENT_VIEWER = "DocumentViewer"
    lateinit var contexts: Context
    lateinit var SharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    init {
        contexts = context
        SharedPreferences = contexts.getSharedPreferences(DOCUMENT_VIEWER, 0)
        editor = SharedPreferences.edit()
    }

    fun setBoolean(str: String, bool: Boolean) {
        val edit: Editor = contexts.getSharedPreferences(DOCUMENT_VIEWER, 0).edit()
        edit.putBoolean(str, bool)
        edit.commit()
    }

    fun getBoolean(str: String, bool: Boolean): Boolean {
        return contexts.getSharedPreferences(DOCUMENT_VIEWER, 0).getBoolean(str, bool)
    }
}