package com.example.thievesmusicplayer


import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.database.Cursor
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.thievesmusicplayer.communicators.MainCommunicator
import com.example.thievesmusicplayer.fragments.MainFragment
import android.util.Log
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.thievesmusicplayer.adpterClasses.Song
import java.io.File
import android.provider.MediaStore

import android.graphics.BitmapFactory

import android.graphics.Bitmap

import android.media.MediaMetadataRetriever
import kotlinx.android.synthetic.main.fragment_song.*


class MainActivity : AppCompatActivity(), MainCommunicator {

    private val mainFragment = MainFragment()

    private lateinit var mediaPlayer : MediaPlayer

    private var songList = mutableListOf<Song>()

    private val path: String = Environment.getExternalStorageDirectory().getPath() + "/Download/Musik/"

    private var isPlayingSong = false
    private var isOnPause = false
    private var currentSongPlaying = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replaceFragment(mainFragment)

        //HANDLING FOR STORAGE PERMISSION
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)){

                Toast.makeText(this, "EXPLANATION", Toast.LENGTH_LONG).show()
            }
            else{
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    1)

                Toast.makeText(this, "EXPLANATION NO NEEDED", Toast.LENGTH_LONG).show()
            }
        }
        //HANDLING FOR STORAGE PERMISSION

        fetchMusicFromFolder()

        //Log.d("TEST", "lul lul lul lul")

    }

    override fun onDestroy() {
        if(this::mediaPlayer.isInitialized){
            mediaPlayer.stop()
            mediaPlayer.release()
        }
        super.onDestroy()
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

    fun fetchMusicFromFolder(){
        File(path).walkTopDown().forEach { it ->
            Log.d("TEST", "name: " + it.name)
            Log.d("TEST", "path: " + it.path)
            val tempSong = Song(it.name, it.name.substringAfter(" - ").substringBefore(".mp3"), it.name.substringBefore(" - "), it.path, 0f)

            songList.add(tempSong)
        }
    }

    override fun playAudio(){

        if(songList.isEmpty()){
            Toast.makeText(this, "No Songs Found", Toast.LENGTH_LONG).show()
        }
        else {
            val songPath: String = songList.elementAt(currentSongPlaying).fileName
            Log.d("Main", " PATH : " + path + songPath)
            var file = File(path + songPath)
            Log.d("Main", " mp3 exists : " + file.exists() + ", can read : " + file.canRead())
            mediaPlayer = MediaPlayer.create(this, Uri.parse(path + songPath))

            isPlayingSong = true
            mediaPlayer.start()
        }
    }

    override fun pauseAudio(){
        isOnPause = true
        isPlayingSong = false
        mediaPlayer.pause()
    }

    override fun resumeAudio(){
        isOnPause = false
        isPlayingSong = true
        mediaPlayer.start()
    }

    override fun playNextSong(){
        mediaPlayer.stop()
        currentSongPlaying += 1
        if(currentSongPlaying >= songList.size)
        {
            currentSongPlaying = 1
        }
        playAudio()
    }

    override fun playPreviousSong(){
        mediaPlayer.stop()
        currentSongPlaying -= 1
        if(currentSongPlaying == 0)
        {
            currentSongPlaying = songList.size-1
        }
        playAudio()
    }

    override fun getSongCover(){
        val mmr = MediaMetadataRetriever()
        mmr.setDataSource(path + songList.elementAt(currentSongPlaying).fileName)

        val data = mmr.embeddedPicture
        //coverart is an Imageview object

        // convert the byte array to a bitmap
        //coverart is an Imageview object

        // convert the byte array to a bitmap
        if (data != null) {
            val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
            song_cover_iv.setImageBitmap(bitmap) //associated cover art in bitmap
        } else {
            song_cover_iv.setImageResource(R.drawable.ascension) //any default cover resourse folder
        }
    }

    override fun setIsPlayingSong(isPlayingSongTemp: Boolean){
        isPlayingSong = isPlayingSongTemp
    }

    override fun getIsPlayingSong() : Boolean{
        return isPlayingSong
    }

    override fun getIsOnPause() : Boolean{
        return isOnPause
    }

    override fun getCurrentSongPlaying(): Song{
        return songList.elementAt(currentSongPlaying)
    }

}