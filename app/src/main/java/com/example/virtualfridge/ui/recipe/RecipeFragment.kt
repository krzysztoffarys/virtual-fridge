package com.example.virtualfridge.ui.recipe

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.virtualfridge.R
import com.example.virtualfridge.databinding.RecipeFragmentBinding
import com.example.virtualfridge.other.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeFragment : Fragment(R.layout.recipe_fragment) {

    private lateinit var binding: RecipeFragmentBinding
    val viewModel: RecipeViewModel by viewModels()
    private val args: RecipeFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RecipeFragmentBinding.bind(view)

        binding.webView.webViewClient = WebViewClient()
        val id = args.id
        subscribeToObservers()
        viewModel.getRecipeDetail(id)

        binding.fab.setOnClickListener {
            viewModel.id = id
            viewModel.saveRecipe()
        }
    }

    private fun subscribeToObservers() {

        viewModel.detailRecipeResponseStatus.observe(viewLifecycleOwner, { result ->
            when(result.status) {
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    binding.fab.visibility = View.VISIBLE
                    val data = result.data
                    if (data != null) {
                        binding.webView.loadUrl(result.data.sourceUrl)
                        viewModel.url = result.data.sourceUrl
                        viewModel.title = result.data.title
                    }
                }
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    val message = result.message ?: "An unknown error occurred"
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING ->{
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        })
    }

}