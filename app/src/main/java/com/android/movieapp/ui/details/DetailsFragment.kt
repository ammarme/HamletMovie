package com.android.movieapp.ui.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.android.movieapp.R
import com.android.movieapp.databinding.FragmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        displayMovieDetails()
    }

    private fun setupViews() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    @SuppressLint("DefaultLocale")
    private fun displayMovieDetails() {
        val movie = args.movie

        binding.apply {
            titleText.text = movie.title
            overviewText.text = movie.overview
            ratingText.text = getString(R.string.rating_format, movie.vote_average)
            releaseDateText.text = getString(R.string.release_date_format, movie.release_date)
            popularityText.text = getString(R.string.popularity_format, movie.popularity)
            voteCountText.text = getString(R.string.vote_count_format, movie.vote_count)
            originalLanguageText.text =
                getString(R.string.language_format, movie.original_language.uppercase())

            backdropImage.load("https://image.tmdb.org/t/p/w780${movie.backdrop_path}") {
                crossfade(true)
                placeholder(R.drawable.pulse_loader)
                error(R.drawable.pulse_loader)
            }

            posterImage.load("https://image.tmdb.org/t/p/w500${movie.poster_path}") {
                crossfade(true)
                placeholder(R.drawable.pulse_loader)
                error(R.drawable.pulse_loader)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
