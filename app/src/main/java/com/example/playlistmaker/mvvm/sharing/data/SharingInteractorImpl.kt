package com.example.playlistmaker.mvvm.sharing.data

import com.example.playlistmaker.R
import com.example.playlistmaker.mvvm.App
import com.example.playlistmaker.mvvm.settings.data.EmailData
import com.example.playlistmaker.mvvm.sharing.ExternalNavigator
import com.example.playlistmaker.mvvm.sharing.domain.SharingInteractor

class SharingInteractorImpl ( private val externalNavigator: ExternalNavigator): SharingInteractor {

    val context = App.Companion.instance.applicationContext

    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return context.getString(R.string.link)
    }

    private fun getSupportEmailData(): EmailData {
        val text = context.getString(R.string.text_message)
        val subject = context.getString(R.string.subject_message)
        val address = context.getString(R.string.mail_adress)
        return EmailData(text, subject, address)

    }

    private fun getTermsLink(): String {
        return context.getString(R.string.link_offer)
    }
}
