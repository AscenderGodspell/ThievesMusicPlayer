package com.example.thievesmusicplayer.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.thievesmusicplayer.R
import com.example.thievesmusicplayer.communicators.MainCommunicator
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.fragment_playlist_list.view.*

class PlaylistListFragment : Fragment() {

    private lateinit var mainCommunicator: MainCommunicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainCommunicator = activity as MainCommunicator

        val view = inflater.inflate(R.layout.fragment_playlist_list, container, false)

        view.all_titles_button_tv.setOnClickListener {
            mainCommunicator.returnToPreviousFragment()
        }

        return view
    }

}