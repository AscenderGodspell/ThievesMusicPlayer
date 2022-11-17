package com.example.thievesmusicplayer.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.example.thievesmusicplayer.R
import com.example.thievesmusicplayer.communicators.MainCommunicator
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.fragment_song.*
import kotlinx.android.synthetic.main.fragment_song.view.*

class SongFragment : Fragment() {

    private lateinit var mainCommunicator: MainCommunicator

    var progressTemp = 0
    var isTouched = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainCommunicator = activity as MainCommunicator

        val view = inflater.inflate(R.layout.fragment_song, container, false)

        var currentSongSF = mainCommunicator.getCurrentSongPlaying()



        //Prepare View
        mainCommunicator.initializeAudioVisualizer(view)

        view.fragment_song_title_tv.text = currentSongSF.title
        view.fragment_songs_artist_tv.text = currentSongSF.artist

        when(mainCommunicator.loadPlayOrder()){
            0 -> {
                view?.order_iv?.setImageResource(R.drawable.ic_repeat)
            }
            1 -> {
                view?.order_iv?.setImageResource(R.drawable.ic_repeat_one)
            }
            2 -> {
                view?.order_iv?.setImageResource(R.drawable.ic_shuffle)
            }
        }

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

        view.order_iv.setOnClickListener{
            if(mainCommunicator.loadPlayOrder() == 2){
                mainCommunicator.savePlayOrder(0)
            }
            else{
                mainCommunicator.savePlayOrder(mainCommunicator.loadPlayOrder() + 1)
            }

            setPlayOrder()
        }

        view.position_sb.max = mainCommunicator.getTotalSongDuration()
        view.position_sb.setOnSeekBarChangeListener(
            object: SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    //Log.d("KEK", "AAAAAAAAAAA")
                    if (fromUser){
                        progressTemp = progress
                        isTouched = true
                        var elapsedTime = createTimeLabel(progress)
                        view?.song_timer_tv?.text = elapsedTime
                    }
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    isTouched = false
                    mainCommunicator.getMediaPlayer().seekTo(progressTemp)
                }
            }
        )

        Thread(Runnable {
            while (mainCommunicator.getMediaPlayer() != null) {
                try {
                    var msg = Message()
                    msg.what = mainCommunicator.getMediaPlayer().currentPosition
                    handler.sendMessage(msg)
                    Thread.sleep(1000)
                }
                catch (e: InterruptedException){}
            }
        }).start()

        return view
    }

    @SuppressLint("HandlerLeak")
    var handler = object: Handler(){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            var currentPosition = msg.what
            if(!isTouched){
                view?.position_sb?.progress = currentPosition

                var elapsedTime = createTimeLabel(currentPosition)
                view?.song_timer_tv?.text = elapsedTime
            }
            //var timerTV: TextView = findViewById(R.id.song_timer_tv) as TextView
            //timerTV.setText(elapsedTime)

            var totalTime = createTimeLabel((mainCommunicator.getTotalSongDuration()))
            view?.song_length_tv?.text = totalTime
            //var lengthTV: TextView = findViewById(R.id.song_length_tv) as TextView
            //lengthTV.setText(totalTime)
        }
    }

    fun createTimeLabel(time: Int): String{
        var timeLabel = ""
        var min = time / 1000 / 60
        var sec = time / 1000 % 60

        timeLabel = "$min:"
        if(sec < 10){
            timeLabel += "0"
        }
        timeLabel += sec

        return timeLabel
    }

    fun setPlayOrder(){
        when(mainCommunicator.loadPlayOrder()){
            0 -> {
                view?.order_iv?.setImageResource(R.drawable.ic_repeat)
            }
            1 -> {
                view?.order_iv?.setImageResource(R.drawable.ic_repeat_one)
            }
            2 -> {
                view?.order_iv?.setImageResource(R.drawable.ic_shuffle)
            }
        }
    }
}