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
import androidx.navigation.fragment.navArgs
import com.app.newsnation.databinding.FragmentDetailBinding
import com.app.newsnation.model.DetailModel
import com.app.newsnation.utils.Constants.TAG
import com.app.newsnation.utils.Objects.testToast

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val dataReceived = DetailModel(
            imgUrl = args.imageUrl,
            title = args.newstitle,
            description = args.description,
            newsUrl = args.newsUrl
        )
        binding.data = dataReceived
    }


    fun redirectToBrowser() {
        val newsSourceUrl = Uri.parse(args.newsUrl)?.let {
            Intent(Intent.ACTION_VIEW)
        }
        try {
            startActivity(newsSourceUrl!!)
            Log.d(TAG, "redirectToBrowser: tried")
        } catch (e: ActivityNotFoundException) {
            testToast(requireContext(), "Something went wrong!")
            Log.d(TAG, "redirectToBrowser: something went wrong!")
        }
    }

}



