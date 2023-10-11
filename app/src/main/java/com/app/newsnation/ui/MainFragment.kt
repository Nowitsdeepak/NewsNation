package com.app.newsnation.ui

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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MainViewModel>()

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
                    Log.d(TAG, "setUp: $list")
                    adapter.submitList(list)
                }
            }


        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
