package com.example.thievesmusicplayer.communicators

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.thievesmusicplayer.adpterClasses.Song

interface MainCommunicator {
    fun replaceFragment(fragment: Fragment)

    fun returnToPreviousFragment()

    fun playAudio()
    fun pauseAudio()
    fun resumeAudio()

    fun setIsPlayingSong(isPlayingSongTemp: Boolean)
    fun getIsPlayingSong() : Boolean

    fun getIsOnPause() : Boolean
}