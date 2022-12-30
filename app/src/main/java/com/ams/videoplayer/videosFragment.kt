package com.ams.videoplayer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ams.videoplayer.databinding.FragmentVideosBinding

class videosFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view  = inflater.inflate(R.layout.fragment_videos, container, false)
        val binding = FragmentVideosBinding.bind(view)
        val templist = ArrayList<String>()
        templist.add("Video 1")
        templist.add("Video 2")
        templist.add("Video 2")
        templist.add("Video 2")
        templist.add("Video 2")

        binding.videoRV.setHasFixedSize(true)
        binding.videoRV.setItemViewCacheSize(10)
        binding.videoRV.layoutManager = LinearLayoutManager(requireContext())
        binding.videoRV.adapter = VideoAdapter(requireContext(),templist)
        return view
    }


}