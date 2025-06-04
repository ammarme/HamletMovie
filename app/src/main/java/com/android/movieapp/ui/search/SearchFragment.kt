package com.android.movieapp.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.movieapp.databinding.FragmentSearchBinding
import com.android.movieapp.model.Movie
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var autocompleteAdapter: AutocompleteAdapter

    private var showFullResults = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupAutocompleteRecyclerView()
        setupSearchInput()

        binding.searchEditText.requestFocus()
        val imm = requireContext().getSystemService(InputMethodManager::class.java)
        imm?.showSoftInput(binding.searchEditText, InputMethodManager.SHOW_IMPLICIT)
        observeSearchResults()
    }

    private fun setupRecyclerView() {
        searchAdapter = SearchAdapter { movie ->
            val action =
                SearchFragmentDirections.actionSearchFragmentToDetailsFragment(movie)
            findNavController().navigate(action)
        }

        binding.searchRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchAdapter
        }
    }

    private fun setupAutocompleteRecyclerView() {
        autocompleteAdapter = AutocompleteAdapter { movie ->
            selectSuggestion(movie)
        }

        binding.autocompleteRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = autocompleteAdapter
        }
    }

    private fun setupSearchInput() {
        binding.searchEditText.addTextChangedListener { text ->
            val query = text.toString().trim()
            if (query.length >= 2) {
                showFullResults = false
                viewModel.searchMovies(query)
            } else {
                viewModel.clearResults()
                binding.autocompleteRecyclerView.visibility = View.GONE
                binding.searchRecyclerView.visibility = View.GONE
            }
        }

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.searchEditText.text.toString().trim()
                if (query.isNotEmpty()) {
                    showFullResults = true
                    viewModel.searchMovies(query)
                }
                true
            } else {
                false
            }
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observeSearchResults() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchResults.collect { movies ->
                if (showFullResults) {
                    searchAdapter.submitList(movies)
                    binding.searchRecyclerView.visibility = View.VISIBLE
                    binding.autocompleteRecyclerView.visibility = View.GONE
                } else {
                    autocompleteAdapter.submitList(movies.take(10))
                    binding.autocompleteRecyclerView.visibility =
                        if (movies.isEmpty()) View.GONE else View.VISIBLE
                    binding.searchRecyclerView.visibility = View.GONE
                }

                binding.emptyStateText.visibility =
                    if (movies.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.searchProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
    }

    private fun selectSuggestion(movie: Movie) {
        binding.searchEditText.setText(movie.title)
        binding.searchEditText.setSelection(movie.title.length)

        showFullResults = true
        viewModel.searchMovies(movie.title)

        binding.autocompleteRecyclerView.visibility = View.GONE
        binding.searchRecyclerView.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
