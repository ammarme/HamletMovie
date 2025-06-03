package com.android.movieapp.ui.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.android.movieapp.R
import com.android.movieapp.databinding.ItemSearchMovieBinding
import com.android.movieapp.model.Movie

class SearchAdapter(
    private val onMovieClick: (Movie) -> Unit
) : ListAdapter<Movie, SearchAdapter.SearchViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding =
            ItemSearchMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SearchViewHolder(
        private val binding: ItemSearchMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("DefaultLocale", "SetTextI18n")
        fun bind(movie: Movie) {
            binding.apply {
                titleText.text = movie.title
                overviewText.text = movie.overview
                ratingText.text = "★ ${String.format("%.1f", movie.vote_average)}"
                releaseDateText.text = movie.release_date

                posterImage.load("https://image.tmdb.org/t/p/w200${movie.poster_path}") {
                    crossfade(true)
                    placeholder(R.drawable.pulse_loader)
                    error(R.drawable.pulse_loader)
                }

                root.setOnClickListener {
                    onMovieClick(movie)
                }
            }
        }
    }

    private class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }
}
