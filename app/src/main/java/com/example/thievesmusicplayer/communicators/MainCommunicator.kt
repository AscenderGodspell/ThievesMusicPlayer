package com.example.thievesmusicplayer.communicators

import androidx.fragment.app.Fragment

interface MainCommunicator {
    fun replaceFragment(fragment: Fragment)

    fun returnToPreviousFragment()
}