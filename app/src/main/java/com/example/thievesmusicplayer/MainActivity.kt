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
import android.widget.ImageView
import android.widget.TextView
import com.example.thievesmusicplayer.fragments.SongFragment
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_song.*


class MainActivity : AppCompatActivity(), MainCommunicator {

    private val mainFragment = MainFragment()

    private lateinit var mediaPlayer : MediaPlayer

    private var songList = mutableListOf<Song>()

    private val path: String = Environment.getExternalStorageDirectory().getPath() + "/Download/Musik/"

    private var isPlayingSong = false
    private var isOnPause = false
    private var currentSongPlaying = 0
    private lateinit var currentFragment: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currentFragment = "MAIN"
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
    }

    override fun onDestroy() {
        if(this::mediaPlayer.isInitialized){
            mediaPlayer.stop()
            mediaPlayer.release()
        }
        super.onDestroy()
    }

    override fun replaceFragment(fragment: Fragment) {
        if(fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            if(fragment != mainFragment){
                transaction.addToBackStack(null)
            }

            transaction.commit()
        }
    }

    override fun onBackPressed() {
        //super.onBackPressed()

        if(currentFragment == "MAIN"){

        }
        else if(currentFragment == "SONG"){
            currentFragment = "MAIN"
            replaceFragment(MainFragment())
        }
    }

    override fun returnToPreviousFragment(){
        supportFragmentManager.popBackStack()
    }

    fun fetchMusicFromFolder(){
        File(path).walkTopDown().forEach { it ->
            val tempSong = Song(it.name, it.name.substringAfter(" - ").substringBefore(".mp3"), it.name.substringBefore(" - "), it.path, 0f)

            songList.add(tempSong)
        }
        songList.removeFirst()
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

            mediaPlayer.setOnCompletionListener {
                playNextSong(false)
            }
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

    override fun playNextSong(skipped: Boolean){
        if(skipped){
            mediaPlayer.stop()
        }
        currentSongPlaying += 1
        if(currentSongPlaying >= songList.size)
        {
            currentSongPlaying = 0
        }

        setCurrentSongData()
        playAudio()
    }

    override fun playPreviousSong(){
        mediaPlayer.stop()
        currentSongPlaying -= 1
        if(currentSongPlaying == -1)
        {
            currentSongPlaying = songList.size-1
        }
        playAudio()
    }

    override fun setCurrentSongPlaying(currentSongPlayingTemp: Int){
        currentSongPlaying = currentSongPlayingTemp

        if(isPlayingSong || isOnPause){
            mediaPlayer.stop()
        }

        setCurrentSongData()
        playAudio()
    }

    override fun setCurrentSongData(){
        if(currentFragment == "MAIN"){
            val titleTV: TextView = (this as MainActivity).findViewById(R.id.song_title_tv) as TextView
            titleTV.setText(songList.elementAt(currentSongPlaying).title)
            val artistTV: TextView = (this as MainActivity).findViewById(R.id.song_artist_tv) as TextView
            artistTV.setText(songList.elementAt(currentSongPlaying).artist)
        }
        else if(currentFragment == "SONG"){
            val titleTV: TextView = (this as MainActivity).findViewById(R.id.fragment_song_title_tv) as TextView
            titleTV.setText(songList.elementAt(currentSongPlaying).title)
            val artistTV: TextView = (this as MainActivity).findViewById(R.id.fragment_songs_artist_tv) as TextView
            artistTV.setText(songList.elementAt(currentSongPlaying).artist)
        }
    }

    override fun setPlayButton(){
        val buttonIV: ImageView = (this as MainActivity).findViewById(R.id.play_button_iv) as ImageView
        buttonIV.setImageResource(R.drawable.ic_pause)
    }

    override fun getSongCover(){
        val mmr = MediaMetadataRetriever()
        mmr.setDataSource(path + songList.elementAt(currentSongPlaying).fileName)

        val data = mmr.embeddedPicture

        // convert the byte array to a bitmap
        /*if (data != null) {
            val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
            song_cover_iv.setImageBitmap(bitmap) //associated cover art in bitmap
        }
        else {
            song_cover_iv.setImageResource(R.drawable.ascension) //any default cover resourse folder
        }*/

        //val coverIV: ImageView = (this as MainActivity).findViewById(R.id.song_cover_iv) as ImageView
        //coverIV.setImageResource(R.drawable.ascension)
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

    override  fun getCurrentSongPlayingId(): Int{
        return currentSongPlaying
    }

    override fun getSongList(): MutableList<Song>{
        return songList
    }

    override fun setCurrentFragment(currentFragmentTemp: String){
        currentFragment = currentFragmentTemp
    }
}