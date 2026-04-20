package com.example.playlistmaker.mvvm.sharing.data

import android.content.Context
import com.example.playlistmaker.R


class SharingRepositoryImpl(private val context: Context):SharingRepository {

    override fun provideLink():String {
        return context.getString(R.string.link)
    }
    override fun provideTextMessage():String {
        return context.getString(R.string.text_message)
    }
    override fun provideSubjectMessage():String {
        return context.getString(R.string.subject_message)
    }
    override fun provideEmailAddress():String {
        return context.getString(R.string.mail_adress)
    }
    override fun provideLinkOffer():String {
        return context.getString(R.string.link_offer)
    }

}