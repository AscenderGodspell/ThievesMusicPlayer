package com.example.thievesmusicplayer.communicators

import android.content.Context
import android.media.MediaPlayer
import android.view.View
import androidx.fragment.app.Fragment
import com.example.thievesmusicplayer.adpterClasses.Song
import com.gauravk.audiovisualizer.visualizer.BarVisualizer

interface MainCommunicator {
    fun replaceFragment(fragment: Fragment)

    fun returnToPreviousFragment()

    fun playAudio()
    fun pauseAudio()
    fun resumeAudio()

    fun getSongCover()

    fun playNextSong(skipped: Boolean)
    fun playPreviousSong()

    fun initializeAudioVisualizer(view: View)



    fun setCurrentSongData()

    fun setPlayButton()

    fun setIsPlayingSong(isPlayingSongTemp: Boolean)
    fun getIsPlayingSong() : Boolean

    fun getIsOnPause() : Boolean

    fun getCurrentSongPlaying(): Song

    fun getCurrentSongPlayingId(): Int

    fun getSongList(): MutableList<Song>

    fun setCurrentFragment(currentFragmentTemp: String)

    fun setCurrentSongPlaying(currentSongPlayingTemp: Int)

    fun setTotalSongDuration(totalSongDurationTemp: Int)
    fun getTotalSongDuration(): Int

    fun getMediaPlayer(): MediaPlayer



    fun savePlayOrder(playOrder: Int)
    fun loadPlayOrder(): Int
}