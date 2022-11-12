package com.example.thievesmusicplayer.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.thievesmusicplayer.MainActivity
import com.example.thievesmusicplayer.R
import com.example.thievesmusicplayer.adapters.SongAdapter
import com.example.thievesmusicplayer.adpterClasses.Song
import com.example.thievesmusicplayer.communicators.MainCommunicator
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment : Fragment() {

    private lateinit var mainCommunicator: MainCommunicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainCommunicator = activity as MainCommunicator

        val view = inflater.inflate(R.layout.fragment_main, container, false)

        var songList = mainCommunicator.getSongList()

        var currentSong = mainCommunicator.getCurrentSongPlaying()

        view.song_title_tv.text = currentSong.title
        view.song_artist_tv.text = currentSong.artist

        val adapter = SongAdapter(songList)
        view.main_rv.adapter = adapter
        view.main_rv.layoutManager = LinearLayoutManager(context)

        view.playlist_button_tv.setOnClickListener {
            mainCommunicator.setCurrentFragment("PLAYLISTLIST")
            mainCommunicator.replaceFragment(PlaylistListFragment())
        }

        view.bottom_bar_ll.setOnClickListener {
            mainCommunicator.setCurrentFragment("SONG")
            mainCommunicator.replaceFragment(SongFragment())
        }

        view.menu_button.setOnClickListener {
            if(view.menu_ll.visibility == View.GONE){
                view.menu_ll.visibility = View.VISIBLE
            }
            else{
                view.menu_ll.visibility = View.GONE
            }
        }

        view.load_songs_tv.setOnClickListener {
            adapter.notifyItemInserted(songList.size-1)
        }

        view.play_button_iv.setOnClickListener {
            if(mainCommunicator.getIsPlayingSong()){
                mainCommunicator.pauseAudio()
                view.play_button_iv.setImageResource(R.drawable.ic_play)
            }
            else{
                if(mainCommunicator.getIsOnPause()){
                    mainCommunicator.resumeAudio()
                    view.play_button_iv.setImageResource(R.drawable.ic_pause)
                }
                else{
                    mainCommunicator.playAudio()
                    view.play_button_iv.setImageResource(R.drawable.ic_pause)
                }

            }

        }

        view.next_button_iv.setOnClickListener {
            if(mainCommunicator.getIsPlayingSong() || mainCommunicator.getIsOnPause()){
                mainCommunicator.playNextSong(true)
            }
            else{
                mainCommunicator.playNextSong(false)
            }

            currentSong = mainCommunicator.getCurrentSongPlaying()

            view.song_title_tv.text = currentSong.title
            view.song_artist_tv.text = currentSong.artist
            view.play_button_iv.setImageResource(R.drawable.ic_pause)
        }

        return view
    }


}