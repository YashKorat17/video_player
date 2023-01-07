package com.ams.videoplayer
import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.ams.videoplayer.databinding.ActivityPlayerBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPlayerBinding

    companion object{
        private lateinit var player : ExoPlayer
        lateinit var playerlist:ArrayList<Video>
        var position :Int = -1
        var repeat : Boolean = false
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        binding = ActivityPlayerBinding.inflate(layoutInflater)


        setContentView(binding.root)

//        for immersive mode
        WindowCompat.setDecorFitsSystemWindows(window,false)
        WindowInsetsControllerCompat(window,binding.root).let {
                controller -> controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_SWIPE
        }

        setTheme(R.style.playerActivityTheme)
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

    @SuppressLint("PrivateResource")
    private fun initalizeBinding(){

        binding.backButton.setOnClickListener{
            finish()
        }
        binding.playPauseBtn.setOnClickListener{
            if(player.isPlaying) pauseVideo()
            else playVideo()
        }

        binding.nextBtn.setOnClickListener {
            nextPlayVideo()
        }
        binding.prevBtn.setOnClickListener {
            nextPlayVideo(isNext = false)
        }

        binding.repeatBtn.setOnClickListener {
            if (repeat){
                repeat = false
                player.repeatMode = Player.REPEAT_MODE_OFF
                binding.repeatBtn.setImageResource(com.google.android.exoplayer2.ui.R.drawable.exo_styled_controls_repeat_off)
            }else{
                repeat = true
                player.repeatMode = Player.REPEAT_MODE_ONE
                binding.repeatBtn.setImageResource(com.google.android.exoplayer2.ui.R.drawable.exo_controls_repeat_all)
            }
        }

    }

    private fun createplayer(){
        try { player.release() } catch (e : Exception){
            e.printStackTrace()
        }

        binding.videoTitle.text = playerlist[position].title
        binding.videoTitle.isSelected = true
        player = ExoPlayer.Builder(this).build()
        binding.playerView.player= player
        val mediaItem = MediaItem.fromUri(playerlist[position].artUri)
        player.setMediaItem(mediaItem)
        player.prepare()
        playVideo()

        player.addListener(object : Player.Listener{
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if (playbackState == Player.STATE_ENDED) nextPlayVideo()
            }
        })
    }

    private fun playVideo(){
        binding.playPauseBtn.setImageResource(R.drawable.pause_icon)
        player.play()
    }
    private fun pauseVideo(){
        binding.playPauseBtn.setImageResource(R.drawable.play_icon)
        player.pause()
    }

    private fun nextPlayVideo(isNext:Boolean = true){
        if (isNext) setPosition()
        else setPosition(isIncrement = false)
        createplayer()
    }

    private fun setPosition(isIncrement:Boolean = true){
        if (!repeat)
        {
            if(isIncrement){
                if(playerlist.size -1 == position)
                    position = 0
                else ++position
            }else{
                if(position == 0)
                    position = playerlist.size - 1
                else --position
            }

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}