package com.example.playlistmaker.mvvm


import android.content.Context
import android.os.Build
import java.util.Locale

object LocaleUtils {

    fun setLocale(context: Context, language: String, country: String? = null): Context {
        val locale = if (country != null) {
            Locale(language, country)
        } else {
            Locale(language)
        }

        val configuration = context.resources.configuration

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(locale)
            context.createConfigurationContext(configuration)
        } else {
            @Suppress("DEPRECATION")
            configuration.locale = locale
            context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
            context
        }
    }
}