package com.android.movieapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.movieapp.R
import com.android.movieapp.databinding.FragmentHomeBinding
import com.android.movieapp.ui.ConnectivityRestoredListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), ConnectivityRestoredListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearchButton()
        observeHomeState()
        viewModel.loadMovies()

        binding.scrollToTopButton.setOnClickListener {
            binding.recyclerView.scrollToPosition(0)
        }
    }

    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter { movie ->
            val action =
                HomeFragmentDirections.actionHomeFragmentToDetailsFragment(
                    movie
                )
            findNavController().navigate(action)
        }

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = movieAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as GridLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    binding.scrollToTopButton.visibility =
                        if (firstVisibleItemPosition > 5) View.VISIBLE else View.GONE

                    val state = viewModel.homeState.value
                    val isLoading = state is HomeState.Loading
                    val isLastPage = (state as? HomeState.Success)?.isLastPage ?: false

                    if (!isLoading && !isLastPage) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 5) {
                            viewModel.loadMovies()
                        }
                    }
                }
            })
        }
    }

    private fun setupSearchButton() {
        binding.searchButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun observeHomeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.homeState.collect { state ->
                when (state) {
                    is HomeState.Idle -> {
                    }

                    is HomeState.Loading -> {
                        binding.progressBar.visibility =
                            if (movieAdapter.itemCount == 0) View.VISIBLE else View.GONE
                    }

                    is HomeState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        movieAdapter.submitList(state.movies)
                    }

                    is HomeState.Error -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onConnectivityRestored() {
        viewModel.refresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
