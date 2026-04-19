package com.example.playlistmaker.mvvm.sharing.data

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri


class ExternalNavigator(private val context: Context) {

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