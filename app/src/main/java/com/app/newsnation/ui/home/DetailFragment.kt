package com.app.newsnation.ui.home

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.app.newsnation.R
import com.app.newsnation.databinding.FragmentDetailBinding
import com.app.newsnation.ui.home.MainViewModel
import com.app.newsnation.utils.Constants.TAG
import com.app.newsnation.utils.Objects.testToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        binding.lifecycleOwner = this
        setUp()
        return binding.root
    }

    private fun setUp() {

        viewModel.currentSelection.observe(viewLifecycleOwner) { newsData ->

            binding.data = newsData

            Log.d(TAG, "onViewCreated: $newsData")

            binding.readMore.setOnClickListener {
                redirectToBrowser(newsData.url)
            }
        }
    }

    private fun redirectToBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            testToast(requireContext(), "Something went wrong!. Try again latter.")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}



