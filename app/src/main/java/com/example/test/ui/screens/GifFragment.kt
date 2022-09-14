package com.example.test.ui.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.test.R
import com.example.test.databinding.FragmentGifBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GifFragment : Fragment(R.layout.fragment_gif) {

    val viewModel by viewModels<GifViewModel>()

    private lateinit var binding: FragmentGifBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGifBinding.bind(view)
    }

}