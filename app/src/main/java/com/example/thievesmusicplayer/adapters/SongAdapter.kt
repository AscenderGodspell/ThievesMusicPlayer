package com.example.thievesmusicplayer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.thievesmusicplayer.MainActivity
import com.example.thievesmusicplayer.R
import com.example.thievesmusicplayer.adpterClasses.Song
import com.example.thievesmusicplayer.communicators.MainCommunicator
import com.example.thievesmusicplayer.fragments.SongFragment
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.item_song.view.*


class SongAdapter(var songs: List<Song>, var mainCommunicator: MainCommunicator) : RecyclerView.Adapter<SongAdapter.SongViewHolder>(){

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private var clicked = false
    private var songId = mainCommunicator.getCurrentSongPlayingId()
    private lateinit var tempHolder: SongViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)

        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.itemView.apply{
            songs_title_tv.text = songs[position].title
            songs_artist_tv.text = songs[position].artist
        }
        if(mainCommunicator.getCurrentSongPlayingId() == holder.adapterPosition){
            holder.itemView.is_playing_iv.visibility = View.VISIBLE
            tempHolder = holder
            clicked = true
        }
        else{
            holder.itemView.is_playing_iv.visibility = View.INVISIBLE
        }

        holder.itemView.setOnClickListener {
            if(clicked && (mainCommunicator.getCurrentSongPlayingId() == holder.adapterPosition)){
                mainCommunicator.setCurrentFragment("SONG")
                mainCommunicator.replaceFragment(SongFragment())
                clicked = false
            }
            else{
                if(songId != holder.adapterPosition){
                    tempHolder.itemView.is_playing_iv.visibility = View.INVISIBLE
                }
                tempHolder = holder
                holder.itemView.is_playing_iv.visibility = View.VISIBLE
                mainCommunicator.setCurrentSongPlaying(position)
                clicked = true
                songId = holder.adapterPosition

                mainCommunicator.setPlayButton()
            }
        }
    }

    override fun getItemCount(): Int {
        return songs.size
    }

}