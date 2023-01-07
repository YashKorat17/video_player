package com.ams.videoplayer

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.ams.videoplayer.databinding.ActivityMainBinding
import java.io.File
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    lateinit var toggle : ActionBarDrawerToggle
    lateinit var  toolbar: Toolbar

    companion object{
        lateinit var videoList:ArrayList<Video>
        lateinit var folderList:ArrayList<Folder>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = binding.toolbar
        setSupportActionBar(toolbar)  //for toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)




//        for Nav Drawer
        toggle = ActionBarDrawerToggle(this,binding.root,R.string.open,R.string.close)
        binding.root.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //  Get PerMission
        if (requestRuntimePermission()){
//        GET ALL FOLDERS
            folderList = ArrayList()
//        GET ALL VIDEOS
            videoList = getAllVideos()
//        set Video Fragment
            setFragement(videosFragment())

        }



        binding.bottomNav.setOnItemSelectedListener{
            when(it.itemId){
                R.id.videoview -> setFragement(videosFragment())
                R.id.folderview -> setFragement(FolderFragment())
            }
            return@setOnItemSelectedListener true
        }

        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.feedbackNav -> Toast.makeText(this,"Feedback",Toast.LENGTH_SHORT).show()
                R.id.themeNav -> Toast.makeText(this,"Theme",Toast.LENGTH_SHORT).show()
                R.id.aboutNav -> Toast.makeText(this,"About",Toast.LENGTH_SHORT).show()
                R.id.exitNav -> exitProcess(1)
            }
            return@setNavigationItemSelectedListener true
        }
    }
    private fun setFragement(fragment:Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentFL,fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()

    }

//    For requsting permission
    private fun requestRuntimePermission():Boolean{
        if(ActivityCompat.checkSelfPermission(this,WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE),13)
            return false
        }
    return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 13){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission Granted ",Toast.LENGTH_SHORT).show()
                folderList = ArrayList()
                videoList = getAllVideos()
                setFragement(videosFragment())
            }else{
                ActivityCompat.requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE),13)
            }
        }
    }
//Drawer Menu Item Method Override
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }

//    return all videos

    @SuppressLint("Recycle", "Range", "SuspiciousIndentation")
    private fun getAllVideos():ArrayList<Video>{
        val tempList = ArrayList<Video>()
        val tempFolderList = ArrayList<String>()
        val projection = arrayOf(MediaStore.Video.Media.TITLE,MediaStore.Video.Media.SIZE
        ,MediaStore.Video.Media._ID,MediaStore.Video.Media.BUCKET_DISPLAY_NAME,MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DATE_ADDED,MediaStore.Video.Media.DURATION,MediaStore.Video.Media.BUCKET_ID)

        val cursor = this.contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,projection,null,null,MediaStore.Video.Media.DATE_ADDED+" DESC")
        if (cursor != null)
            if (cursor.moveToNext())
                do {
                    val titleC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE))
                    val idC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID))
                    val foldernameC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
                    val folderId = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID))
                    val sizeC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE))
                    val pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
                    val durationC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION)).toLong()

                    try {
                        val file  = File(pathC)
                        val artUri = Uri.fromFile(file)

                        val video = Video(idC,titleC,durationC,foldernameC,sizeC,pathC,artUri)

                        if (file.exists()) tempList.add(video)

//                        for adding folder
                        if(!tempFolderList.contains(foldernameC)){
                            tempFolderList.add(foldernameC)
                            folderList.add(Folder(foldername = foldernameC,id=folderId))
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }

                }while (cursor.moveToNext())
                cursor?.close()
        return tempList
    }
}

