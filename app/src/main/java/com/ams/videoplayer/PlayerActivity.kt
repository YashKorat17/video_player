package com.ams.videoplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ams.videoplayer.databinding.ActivityPlayerBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPlayerBinding

    companion object{
        private lateinit var player : ExoPlayer
        lateinit var playerlist:ArrayList<Video>
        var position :Int = -1

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        initializeLayout()
        initalizeBinding()
        }

    private fun initializeLayout(){
        when(intent.getStringExtra("class")) {
            "AllVideos"->{
                playerlist = ArrayList()
                playerlist.addAll(MainActivity.videoList)
                createplayer()

            }
            "FolderActivity"->{
                playerlist = ArrayList()
                playerlist.addAll(FoldersActivity.currentFolderVideoes)
                createplayer()
            }
        }
    }

    private fun initalizeBinding(){
        binding.backButton.setOnClickListener{
            finish()
        }
        binding.playPauseBtn.setOnClickListener{
            if(player.isPlaying) pauseVideo()
            else playVideo()
        }

    }

    private fun createplayer(){
        binding.videoTitle.text = playerlist[position].title
        binding.videoTitle.isSelected = true
        player = ExoPlayer.Builder(this).build()
        binding.playerView.player= player
        val mediaItem = MediaItem.fromUri(playerlist[position].artUri)
        player.setMediaItem(mediaItem)
        player.prepare()
        playVideo()
    }

    private fun playVideo(){
        binding.playPauseBtn.setImageResource(R.drawable.pause_icon)
        player.play()
    }
    private fun pauseVideo(){
        binding.playPauseBtn.setImageResource(R.drawable.play_icon)
        player.pause()
    }
    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}