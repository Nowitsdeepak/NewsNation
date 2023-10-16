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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.newsnation.databinding.FragmentMainBinding
import com.app.newsnation.ui.bookmark.BookmarkViewModel
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

    private val mainViewModel: MainViewModel by activityViewModels()

    private val bookMarkViewModel by viewModels<BookmarkViewModel>()

    @Inject
    lateinit var networkUtils: NetworkUtils


    private val connectivityReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (networkUtils.isConnectedToNetwork()) {
                mainViewModel.refresh()
                Log.d(TAG, "onReceive: Boardcast")
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

                mainViewModel.setCategory(categoryName)
                mainViewModel.refresh()

            })

            val adapter = FeedAdapter(onClickedItem = { news ->
                mainViewModel.setCurrentSelection(news)

            }, onBookmarkClicked = { news, isMarked ->

                mainViewModel.markOffline(news, isMarked)

                if (isMarked) {
                    bookMarkViewModel.checked(news)

                    Log.d(TAG, "setUp: Marked")

                    testToast(requireContext(), "Bookmark Added")
                } else {
                    news.title?.let { bookMarkViewModel.unChecked(it) }
                    Log.d(TAG, "setUp: unMarked ${news.id}")

                    testToast(requireContext(), "Bookmark Removed")
                }
            })

            mainViewModel.getNews.observe(viewLifecycleOwner) { news ->
                Log.d(TAG, "setUp: ${news.size}")
                news?.let { list ->
                    Log.d(TAG, "setUp: ${list.size}")
                    adapter.submitList(list)
                    ProgressIndicator.visibility = View.GONE

                }
            }

            rvNews.adapter = adapter

            mainViewModel.status.observe(viewLifecycleOwner) { status ->
                when (status!!) {
                    Constants.STATUS.LOADING -> {
                        ProgressIndicator.visibility = View.VISIBLE
                    }
                    Constants.STATUS.LOADED -> {
                        ProgressIndicator.visibility = View.GONE
                    }
                    Constants.STATUS.NETWORK_ERROR -> {
                        includedConnectionError.root.visibility = View.VISIBLE
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
