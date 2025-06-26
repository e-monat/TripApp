package com.example.inventory.ui

import android.app.Application
import android.content.Context
import com.example.inventory.tools.LocaleHelper

class LocaleApp : Application() {

    override fun attachBaseContext(base: Context) {
        val wrapped = LocaleHelper.wrap(base)
        super.attachBaseContext(wrapped)
    }

    override fun onCreate() {
        super.onCreate()
    }
}

