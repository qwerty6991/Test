package com.example.test.ui.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.test.R
import com.example.test.databinding.FragmentGifBinding
import com.example.test.ui.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GifFragment : Fragment(R.layout.fragment_gif) {

    private val viewModel by viewModels<GifViewModel>()

    private lateinit var binding: FragmentGifBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGifBinding.bind(view)

        val adapter = ViewPagerAdapter()
        binding.idViewPager.adapter = adapter


        val searchName = GifFragmentArgs.fromBundle(requireArguments()).searchName
        viewModel.setSearchName(searchName)

        lifecycleScope.launch {
            viewModel.gifsFlow.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }
}