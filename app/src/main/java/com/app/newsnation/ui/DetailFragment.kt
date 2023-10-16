package com.app.newsnation.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.app.newsnation.data.local.ArticleEntity
import com.app.newsnation.databinding.FragmentDetailBinding
import com.app.newsnation.utils.Constants.TAG
import com.app.newsnation.utils.Objects.testToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var newsReceived: ArticleEntity


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(layoutInflater)
        setUp()
        return binding.root
    }

    fun setUp() {

        viewModel.currentSelection.observe(viewLifecycleOwner) { newsData ->
            newsReceived = newsData
            binding.data = newsReceived

            Log.d(TAG, "onViewCreated: $newsData")
            if (newsData.urlToImage.isNullOrBlank()) {
                binding.imageView.visibility = View.GONE
            }
        }
    }


    fun redirectToBrowser() {
        if (newsReceived.url != null) {
            val newsSourceUrl = Uri.parse(newsReceived.url)?.let {
                Intent(Intent.ACTION_VIEW)
            }
            try {
                startActivity(newsSourceUrl!!)
                Log.d(TAG, "redirectToBrowser: tried")
            } catch (e: ActivityNotFoundException) {
                testToast(requireContext(), "Something went wrong!")
                Log.d(TAG, "redirectToBrowser: something went wrong!")
            }
        } else {
            binding.btnReadmore.visibility = View.GONE
        }
    }

}



