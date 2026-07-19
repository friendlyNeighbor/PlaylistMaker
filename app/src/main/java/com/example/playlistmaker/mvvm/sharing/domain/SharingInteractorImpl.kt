package com.example.playlistmaker.mvvm.sharing.domain

import com.example.playlistmaker.mvvm.sharing.data.EmailData
import com.example.playlistmaker.mvvm.sharing.data.ExternalNavigator
import com.example.playlistmaker.mvvm.sharing.data.SharingRepository

class SharingInteractorImpl (private val externalNavigator: ExternalNavigator, private val repository: SharingRepository): SharingInteractor {

    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun sharePlaylist(message: String) {
        externalNavigator.sharePlaylist(message)
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }


    private fun getShareAppLink(): String {
        return repository.provideLink()
    }

    private fun getSupportEmailData(): EmailData {
        val text = repository.provideTextMessage()
        val subject = repository.provideSubjectMessage()
        val address = repository.provideEmailAddress()
        return EmailData(text, subject, address)
    }

    private fun getTermsLink(): String {
        return repository.provideLinkOffer()
    }
}