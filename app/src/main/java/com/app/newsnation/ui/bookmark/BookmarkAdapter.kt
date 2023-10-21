package com.app.newsnation.ui.bookmark

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.newsnation.data.local.ArticleEntity
import com.app.newsnation.databinding.ItemBookmarkBinding

class BookmarkAdapter(
    private val onUncheckedClicked: (ArticleEntity, Boolean) -> Unit,
) : ListAdapter<ArticleEntity, BookmarkAdapter.BookmarkAdapterViewHolder>(DiffCallBack) {

    inner class BookmarkAdapterViewHolder(var binding: ItemBookmarkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindTo(item: ArticleEntity) {
            binding.data = item
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkAdapterViewHolder {
        val layout = ItemBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookmarkAdapterViewHolder(layout)
    }

    override fun onBindViewHolder(holder: BookmarkAdapterViewHolder, position: Int) {
        val item = getItem(position)

        with(holder) {
            bindTo(item)
            with(binding) {

                cbBookmark.setOnClickListener {
                    onUncheckedClicked(item, false)
                }

                feedSurface.setOnClickListener {

                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))

                    try {
                        it.context.startActivity(intent)
                    } catch (_: ActivityNotFoundException) {
                    }

                }
            }
        }
    }

    object DiffCallBack : DiffUtil.ItemCallback<ArticleEntity>() {

        override fun areContentsTheSame(oldItem: ArticleEntity, newItem: ArticleEntity): Boolean {
            return oldItem.isBookmark == newItem.isBookmark
        }

        override fun areItemsTheSame(oldItem: ArticleEntity, newItem: ArticleEntity): Boolean {
            return oldItem == newItem
        }

    }
}