package com.example.test.ui.screens

import android.os.Bundle
import android.view.View
import androidx.core.view.isInvisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.R
import com.example.test.databinding.FragmentRootBinding
import com.example.test.domain.Gif
import com.example.test.ui.adapter.GifsAdapter
import com.example.test.ui.base.DefaultLoadStateAdapter
import com.example.test.ui.base.TryAgainAction
import com.example.test.ui.base.simpleScan
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class RootFragment : Fragment(R.layout.fragment_root), GifsAdapter.GifActionListener {

    private lateinit var viewModel: SharedViewModel
    private lateinit var adapter: GifsAdapter
    private lateinit var binding: FragmentRootBinding
    private lateinit var mainLoadStateHolder: DefaultLoadStateAdapter.Holder

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRootBinding.bind(view)

        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        adapter = GifsAdapter()

        setupGifsList()

        setupSearchInput()
    }

    private fun setupGifsList() {

        adapter.gifActionListener = this

        val tryAgainAction: TryAgainAction = { adapter.retry() }

        val footerAdapter = DefaultLoadStateAdapter(tryAgainAction)

        val adapterWithLoadState = adapter.withLoadStateFooter(footerAdapter)

        binding.gifsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.gifsRecyclerView.adapter = adapterWithLoadState
        (binding.gifsRecyclerView.itemAnimator as? DefaultItemAnimator)
            ?.supportsChangeAnimations = false

        mainLoadStateHolder = DefaultLoadStateAdapter.Holder(
            binding.loadStateView,
            binding.swipeRefreshLayout,
            tryAgainAction
        )

        setupSwipeToRefresh(adapter)

        observeGifs(adapter)
        observeLoadState(adapter)

        handleScrollingToTopWhenSearching(adapter)
        handleListVisibility(adapter)
    }

    private fun setupSearchInput() {
        binding.searchEditText.addTextChangedListener {
            val search = binding.searchEditText.text.toString()
            viewModel.search = search
        }
    }

    private fun setupSwipeToRefresh(adapter: GifsAdapter) {
        binding.swipeRefreshLayout.setOnRefreshListener {
            adapter.refresh()
        }
    }

    private fun observeGifs(adapter: GifsAdapter) {
        lifecycleScope.launch {
            viewModel.gifsBySearchFlow.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }

    private fun observeLoadState(adapter: GifsAdapter) {

        lifecycleScope.launch {
            adapter.loadStateFlow.debounce(200).collectLatest { state ->
                // main indicator in the center of the screen
                mainLoadStateHolder.bind(state.refresh)
            }
        }
    }

    private fun handleScrollingToTopWhenSearching(adapter: GifsAdapter) =
        lifecycleScope.launch {
            // list should be scrolled to the 1st item (index = 0) if data has been reloaded:
            // (prev state = Loading, current state = NotLoading)
            getRefreshLoadStateFlow(adapter)
                .simpleScan(count = 2)
                .collectLatest { (previousState, currentState) ->
                    if (previousState is LoadState.Loading && currentState is LoadState.NotLoading) {
                        binding.gifsRecyclerView.scrollToPosition(0)
                    }
                }
        }

    private fun handleListVisibility(adapter: GifsAdapter) = lifecycleScope.launch {
        // list should be hidden if an error is displayed OR if items are being loaded after the error:
        // (current state = Error) OR (prev state = Error)
        //   OR
        // (before prev state = Error, prev state = NotLoading, current state = Loading)
        getRefreshLoadStateFlow(adapter)
            .simpleScan(count = 3)
            .collectLatest { (beforePrevious, previous, current) ->
                binding.gifsRecyclerView.isInvisible = current is LoadState.Error
                        || previous is LoadState.Error
                        || (beforePrevious is LoadState.Error
                        && previous is LoadState.NotLoading
                        && current is LoadState.Loading)
            }
    }

    private fun getRefreshLoadStateFlow(adapter: GifsAdapter): Flow<LoadState> {
        return adapter.loadStateFlow
            .map { it.refresh }
    }

    override fun onOpenGifFragment(position: Int) {
        val direction =
            RootFragmentDirections.actionRootFragmentToGifFragment(position)
        findNavController().navigate(direction)
    }

    override fun onGifDelete(gif: Gif) {
        TODO("Not yet implemented")
    }
}