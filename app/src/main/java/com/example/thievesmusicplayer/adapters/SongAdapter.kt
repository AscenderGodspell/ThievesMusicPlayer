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




class SongAdapter(var songs: List<Song>, var mainCommunicator: MainCommunicator) : RecyclerView.Adapter<SongAdapter.SongViewHolder>(){

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private var clicked = false
    private var songId = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)

        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        //mainCommunicator = context as MainCommunicator
        //this.mainCommunicator = mainCommunicator

        holder.itemView.apply{
            songs_title_tv.text = songs[position].title
            songs_artist_tv.text = songs[position].artist
        }

        holder.itemView.setOnClickListener {

            if(clicked && (songId == holder.adapterPosition)){
                mainCommunicator.setCurrentFragment("SONG")
                mainCommunicator.replaceFragment(SongFragment())
                clicked = false
            }
            else{
                mainCommunicator.setCurrentSongPlaying(position)
                clicked = true
                songId = holder.adapterPosition
            }
        }
    }

    override fun getItemCount(): Int {
        return songs.size
    }

}



/*
        holder.itemView.setOnClickListener { v ->

            val activity = v!!.context as AppCompatActivity
            val songFragment = SongFragment()
            activity.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, songFragment).commit()
        }
 */