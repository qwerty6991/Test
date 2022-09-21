package com.example.test.ui.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.test.R
import com.example.test.databinding.FragmentGifBinding
import com.example.test.ui.adapter.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class GifFragment : Fragment(R.layout.fragment_gif) {

    private lateinit var viewModel: SharedViewModel
    private lateinit var binding: FragmentGifBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGifBinding.bind(view)

        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        val adapter = ViewPagerAdapter()
        binding.idViewPager.adapter = adapter

        val position = GifFragmentArgs.fromBundle(requireArguments()).position

        adapter.startPosition = position

        lifecycleScope.launch {
            viewModel.gifsBySearchFlow.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }
}