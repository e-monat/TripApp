package com.example.inventory.tools

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Build
import java.util.*

object LocaleHelper {

    fun wrap(context: Context): ContextWrapper {
        val locale = Locale.getDefault()
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
        } else {
            config.locale = locale
        }

        val updatedContext = context.createConfigurationContext(config)
        return ContextWrapper(updatedContext)
    }
}

