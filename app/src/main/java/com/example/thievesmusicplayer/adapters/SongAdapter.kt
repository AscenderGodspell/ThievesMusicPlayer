package com.example.thievesmusicplayer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.thievesmusicplayer.R
import com.example.thievesmusicplayer.adpterClasses.Song
import com.example.thievesmusicplayer.communicators.MainCommunicator
import com.example.thievesmusicplayer.fragments.SongFragment
import kotlinx.android.synthetic.main.item_song.view.*

class SongAdapter(var songs: List<Song>, /*context: Context*/) : RecyclerView.Adapter<SongAdapter.SongViewHolder>(){

    private lateinit var mainCommunicator: MainCommunicator
    private val context: Context? = null

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)

        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        //mainCommunicator = context as MainCommunicator

        holder.itemView.apply{
            songs_title_tv.text = songs[position].title
            songs_artist_tv.text = songs[position].artist
        }

        holder.itemView.setOnClickListener {
            //mainCommunicator.replaceFragment(SongFragment())
        }
    }

    override fun getItemCount(): Int {
        return songs.size
    }


}