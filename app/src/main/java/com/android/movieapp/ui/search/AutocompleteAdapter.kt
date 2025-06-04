package com.android.movieapp.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.movieapp.databinding.ItemAutocompleteMovieBinding
import com.android.movieapp.model.Movie
import com.android.movieapp.ui.utils.ViewAnimationUtils

class AutocompleteAdapter(
    private val onItemClick: (Movie) -> Unit
) : ListAdapter<Movie, AutocompleteAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAutocompleteMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class ViewHolder(
        private val binding: ItemAutocompleteMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    ViewAnimationUtils.animateClick(binding.root)
                    onItemClick(getItem(position))
                }
            }
        }

        fun bind(movie: Movie, position: Int) {
            binding.titleTextView.text = movie.title

            if (movie.release_date.isNotEmpty()) {
                val year = movie.release_date.substring(0, 4)
                binding.yearTextView.text = year
                binding.yearTextView.visibility = View.VISIBLE
            } else {
                binding.yearTextView.visibility = View.GONE
            }

            ViewAnimationUtils.animateEntrance(binding.root, position)
        }

    }

    private class DiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }
}
