package com.app.newsnation.ui.home

import android.content.*
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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

    private val mainViewModel: MainViewModel by activityViewModels()


    @Inject
    lateinit var networkUtils: NetworkUtils


    private val connectivityReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (networkUtils.isConnectedToNetwork()) {
                mainViewModel.refresh()
                binding.rvCategory.visibility = View.VISIBLE
            } else {
                binding.rvCategory.visibility = View.GONE
            }
        }
    }

    @Suppress("DEPRECATION")
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


            rvCategory.adapter =

                CategoryAdapter(Constants.clist,

                    onItemClicked = { categoryName ->

                        mainViewModel.setCategory(categoryName)
                        mainViewModel.refresh()

                    })

            val adapter = FeedAdapter(onClickedItem = { news ->
                mainViewModel.setCurrentSelection(news)

            }, onBookmarkClicked = { news, isMarked ->

                mainViewModel.markOffline(news, isMarked)

            }, onShareClicked = { newsUrl ->
                redirectToBrowser(newsUrl)
            })

            mainViewModel.getNews.observe(viewLifecycleOwner) { news ->
                emptylist.visibility = if (news.isEmpty()) View.VISIBLE else View.GONE
                adapter.submitList(news)
            }

            rvNews.adapter = adapter

            mainViewModel.status.observe(viewLifecycleOwner) { status ->
                when (status!!) {
                    Constants.STATUS.LOADING -> {
                        ProgressIndicator.visibility = View.VISIBLE
                    }
                    Constants.STATUS.LOADED -> {
                        ProgressIndicator.visibility = View.INVISIBLE
                    }
                    Constants.STATUS.NETWORK_ERROR -> {
                        emptylist.visibility = View.VISIBLE
                    }
                    Constants.STATUS.EMPTY_LIST -> {

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

    private fun redirectToBrowser(news: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, news)
        }
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            testToast(requireContext(), "Something went wrong!. Try again latter.")
        }
    }

}
