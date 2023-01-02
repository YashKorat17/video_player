package com.ams.videoplayer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ams.videoplayer.databinding.FragmentFolderBinding


class FolderFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view  = inflater.inflate(R.layout.fragment_folder, container, false)
        val binding = FragmentFolderBinding.bind(view)

        binding.FoldersRV.setHasFixedSize(true)
        binding.FoldersRV.setItemViewCacheSize(10)
        binding.FoldersRV.layoutManager = LinearLayoutManager(requireContext())
        binding.FoldersRV.adapter = FoldersAdapter(requireContext(),MainActivity.folderList)
        return view
    }

}