package com.app.newsnation.ui.bookmark

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.app.newsnation.databinding.FragmentBookmarkBinding
import com.app.newsnation.utils.Constants.TAG
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    private val bookMarkViewModel by viewModels<BookmarkViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBookmarkBinding.inflate(layoutInflater)
        setUp()
        return binding.root
    }

    private fun setUp() {
        with(binding) {

            rvBookmark.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

            val adapter = BookmarkAdapter(onUncheckedClicked = { news ->
                bookMarkViewModel.unChecked(news)
                Log.d(TAG, "setUp: Bookmarked Unchecked : $news")
                Snackbar.make(binding.root, "Bookmark Removed!", Snackbar.LENGTH_SHORT).show()
            })

            rvBookmark.adapter = adapter
            bookMarkViewModel.getMark.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}