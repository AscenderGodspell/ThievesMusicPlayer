package com.example.thievesmusicplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.thievesmusicplayer.communicators.MainCommunicator
import com.example.thievesmusicplayer.fragments.MainFragment

class MainActivity : AppCompatActivity(), MainCommunicator {

    private val mainFragment = MainFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replaceFragment(mainFragment)
    }

    override fun replaceFragment(fragment: Fragment){
        if(fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            if(fragment != mainFragment){
                transaction.addToBackStack(null)
            }
            else{
                //TODO add double click to prevent unintentional leave
            }
            transaction.commit()
        }
    }

    override fun returnToPreviousFragment(){
        supportFragmentManager.popBackStack()
        //TODO prevent returning to SongFragment on clicking the "Back-Button"
    }

}