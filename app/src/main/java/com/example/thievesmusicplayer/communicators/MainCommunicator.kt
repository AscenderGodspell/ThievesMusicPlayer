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

    fun getSongCover()

    fun playNextSong(skipped: Boolean)
    fun playPreviousSong()

    fun setCurrentSongData()

    fun setIsPlayingSong(isPlayingSongTemp: Boolean)
    fun getIsPlayingSong() : Boolean

    fun getIsOnPause() : Boolean

    fun getCurrentSongPlaying(): Song

    fun getSongList(): MutableList<Song>

    fun setCurrentFragment(currentFragmentTemp: String)
}