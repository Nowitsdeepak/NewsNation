package com.app.newsnation.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.newsnation.databinding.FragmentMainBinding
import com.app.newsnation.utils.Constants
import com.app.newsnation.utils.Constants.TAG
import com.app.newsnation.utils.NetworkUtils
import com.app.newsnation.utils.Objects.testToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var networkUtils: NetworkUtils


    private val connectivityReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (networkUtils.isConnectedToNetwork()) {
                viewModel.refresh()
                Log.d(TAG, "onReceive: refresh called bordcast")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        requireActivity().registerReceiver(connectivityReceiver, filter)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(layoutInflater)

        init()
        setUp()

        return binding.root
    }


    private fun init() {

        with(binding) {
            rvNews.layoutManager = LinearLayoutManager(requireContext())
        }

    }

    private fun setUp() {


        with(binding) {

            rvCategory.adapter = CategoryAdapter(Constants.clist, onItemClicked = { categoryName ->

                viewModel.setCategory(categoryName)
                viewModel.refresh()

            })

            val adapter = FeedAdapter()
            rvNews.adapter = adapter

            viewModel.getNews.observe(viewLifecycleOwner) { news ->
                news?.let { list ->
                    adapter.submitList(list)
                }
            }



            viewModel.status.observe(viewLifecycleOwner) { status ->
                when (status!!) {
                    Constants.STATUS.LOADING -> {
                        ProgressIndicator.visibility = View.VISIBLE
                    }
                    Constants.STATUS.LOADED -> {
                        ProgressIndicator.visibility = View.GONE
                    }
                    Constants.STATUS.NETWORK_ERROR -> {

                        includedConnectionError.root.visibility = View.VISIBLE
                        includedConnectionError.retryConnection.setOnClickListener {

                        }
                    }

                    Constants.STATUS.EMPTY_LIST -> {
                        testToast(requireContext(), "Empty data")
                    }
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onStop() {
        super.onStop()
        requireActivity().unregisterReceiver(connectivityReceiver)
    }
}
