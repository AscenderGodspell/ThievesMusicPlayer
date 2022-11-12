package com.example.thievesmusicplayer.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.thievesmusicplayer.R
import com.example.thievesmusicplayer.communicators.MainCommunicator
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.fragment_song.*
import kotlinx.android.synthetic.main.fragment_song.view.*

class SongFragment : Fragment() {

    private lateinit var mainCommunicator: MainCommunicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainCommunicator = activity as MainCommunicator

        val view = inflater.inflate(R.layout.fragment_song, container, false)

        var currentSongSF = mainCommunicator.getCurrentSongPlaying()



        //Prepare View
        view.fragment_song_title_tv.text = currentSongSF.title
        view.fragment_songs_artist_tv.text = currentSongSF.artist

        if(mainCommunicator.getIsPlayingSong()){
            view.play_song_iv.setImageResource(R.drawable.ic_pause_circle)
        }



        //Other Code
        mainCommunicator.getSongCover()



        //OnClickListeners
        view.close_song_button_iv.setOnClickListener {
            mainCommunicator.setCurrentFragment("MAIN")
            mainCommunicator.replaceFragment(MainFragment())
        }

        view.play_song_iv.setOnClickListener {
            if(mainCommunicator.getIsPlayingSong()){
                mainCommunicator.pauseAudio()
                view.play_song_iv.setImageResource(R.drawable.ic_play_circle)
            }
            else{
                if(mainCommunicator.getIsOnPause()){
                    mainCommunicator.resumeAudio()
                    view.play_song_iv.setImageResource(R.drawable.ic_pause_circle)
                }
                else{
                    mainCommunicator.playAudio()
                    view.play_song_iv.setImageResource(R.drawable.ic_pause_circle)
                }

            }
        }

        view.skip_song_iv.setOnClickListener {
            if(mainCommunicator.getIsPlayingSong() || mainCommunicator.getIsOnPause()){
                mainCommunicator.playNextSong(true)
            }
            else{
                mainCommunicator.playNextSong(false)
            }

            currentSongSF = mainCommunicator.getCurrentSongPlaying()

            view.fragment_song_title_tv.text = currentSongSF.title
            view.fragment_songs_artist_tv.text = currentSongSF.artist
            view.play_song_iv.setImageResource(R.drawable.ic_pause_circle)
        }

        view.skip_previous_song_iv.setOnClickListener {
            mainCommunicator.playPreviousSong()

            currentSongSF = mainCommunicator.getCurrentSongPlaying()

            view.fragment_song_title_tv.text = currentSongSF.title
            view.fragment_songs_artist_tv.text = currentSongSF.artist
            view.play_song_iv.setImageResource(R.drawable.ic_pause_circle)
        }

        return view
    }
}