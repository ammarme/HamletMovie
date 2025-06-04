package com.android.movieapp.ui.search

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.movieapp.databinding.ItemAutocompleteMovieBinding
import com.android.movieapp.model.Movie

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
                    animateClick()
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

            animateEntrance(position)
        }

        private fun animateEntrance(position: Int) {
            val delay = position * 50L

            binding.root.alpha = 0f
            binding.root.translationX = 100f

            val fadeIn = ObjectAnimator.ofFloat(binding.root, "alpha", 0f, 1f)
            val slideIn = ObjectAnimator.ofFloat(binding.root, "translationX", 100f, 0f)

            val animatorSet = AnimatorSet()
            animatorSet.playTogether(fadeIn, slideIn)
            animatorSet.duration = 300
            animatorSet.startDelay = delay
            animatorSet.interpolator = AccelerateDecelerateInterpolator()
            animatorSet.start()
        }

        private fun animateClick() {
            val scaleX = ObjectAnimator.ofFloat(binding.root, "scaleX", 1f, 0.95f, 1f)
            val scaleY = ObjectAnimator.ofFloat(binding.root, "scaleY", 1f, 0.95f, 1f)

            val animatorSet = AnimatorSet()
            animatorSet.playTogether(scaleX, scaleY)
            animatorSet.duration = 150
            animatorSet.start()
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
