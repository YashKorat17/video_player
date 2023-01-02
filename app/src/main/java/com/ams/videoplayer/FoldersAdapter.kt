package com.ams.videoplayer

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ams.videoplayer.databinding.FoldersViewBinding

class FoldersAdapter(private val context: Context, private var folderList: ArrayList<Folder>):
    RecyclerView.Adapter<FoldersAdapter.MyHolder>() {
    class MyHolder(binding: FoldersViewBinding): RecyclerView.ViewHolder(binding.root){
        val folderName = binding.folderNameFV



    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {

        return MyHolder(FoldersViewBinding.inflate(LayoutInflater.from(context),parent,false))
    }
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.folderName.text = folderList[position].foldername
    }
    override fun getItemCount(): Int {
        return folderList.size
    }

}