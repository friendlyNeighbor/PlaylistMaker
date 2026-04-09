package com.example.playlistmaker.mvvm.sharing

import android.content.Intent
import androidx.core.net.toUri
import com.example.playlistmaker.mvvm.App
import com.example.playlistmaker.mvvm.settings.data.EmailData

class ExternalNavigator() {
    val context = App.Companion.instance.applicationContext

    fun shareLink(message:String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setType("text/plain")
        intent.putExtra(Intent.EXTRA_TEXT, message)
        context.startActivity(intent)
    }

    fun openLink(url:String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setData(url.toUri())
        context.startActivity(intent)
    }

    fun openEmail(email: EmailData) {
        val text = email.text
        val subject = email.subject
        val mailAddress = email.address
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.data = "mailto:".toUri()
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(mailAddress))
        intent.putExtra(Intent.EXTRA_TEXT, text)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        context.startActivity(intent)
    }
}