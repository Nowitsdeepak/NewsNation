package com.app.newsnation.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.app.newsnation.data.local.ArticleEntity
import com.app.newsnation.databinding.ItemFeedBinding
import com.app.newsnation.utils.Constants.TAG

class FeedAdapter(
    private val onClickedItem: (ArticleEntity) -> Unit,
    private val onBookmarkClicked: (ArticleEntity, Boolean) -> Unit,
) : ListAdapter<ArticleEntity, FeedAdapter.FeedAdapterViewHolder>(DiffCallBack) {

    inner class FeedAdapterViewHolder(var binding: ItemFeedBinding) :
        ViewHolder(binding.root) {
        fun bindTo(item: ArticleEntity) {
            binding.data = item
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedAdapterViewHolder {
        val layout = ItemFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeedAdapterViewHolder(layout)
    }

    override fun onBindViewHolder(holder: FeedAdapterViewHolder, position: Int) {
        val item = getItem(position)

        with(holder) {
            bindTo(item)
            binding.feedSurface.setOnClickListener {
                onClickedItem(item)
                val action = MainFragmentDirections.actionMainFragmentToDetailFragment()
                it.findNavController().navigate(action)
            }

            binding.cbBookmark.setOnClickListener {
                val isMarked = binding.cbBookmark.isChecked
                onBookmarkClicked(item, isMarked)
                Log.d(TAG, "onBindViewHolder: $isMarked ")
            }
        }
    }
}

object DiffCallBack : ItemCallback<ArticleEntity>() {
    override fun areItemsTheSame(oldItem: ArticleEntity, newItem: ArticleEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ArticleEntity, newItem: ArticleEntity): Boolean {
        return oldItem == newItem
    }

}
