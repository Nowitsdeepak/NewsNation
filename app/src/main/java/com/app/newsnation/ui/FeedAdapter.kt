package com.app.newsnation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.app.newsnation.data.local.ArticleEntity
import com.app.newsnation.databinding.ItemFeedBinding

class FeedAdapter :
    ListAdapter<ArticleEntity, FeedAdapter.FeedAdapterViewHolder>(DiffCallBack) {

    inner class FeedAdapterViewHolder(private val binding: ItemFeedBinding) :
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
            itemView.setOnClickListener {
                with(item) {

                    val action = MainFragmentDirections.actionMainFragmentToDetailFragment(
                        newsUrl = url!!,
                        imageUrl = urlToImage!!,
                        newstitle = title!!,
                        description = description!!
                    )
                    itemView.findNavController().navigate(action)
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
}

