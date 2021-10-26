package com.example.virtualfridge.ui.search

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.virtualfridge.R
import com.example.virtualfridge.adapters.RecipeAdapter
import com.example.virtualfridge.databinding.SearchRecipesFragmentBinding
import com.example.virtualfridge.other.Status
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchRecipesFragment : Fragment(R.layout.search_recipes_fragment) {


    @Inject
    lateinit var auth: FirebaseAuth
    private lateinit var binding: SearchRecipesFragmentBinding
    private lateinit var recipeAdapter: RecipeAdapter
    val viewModel: SearchRecipesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SearchRecipesFragmentBinding.bind(view)

        setupRecyclerView()
        subscribeToObservers()

        binding.btnSearch.setOnClickListener {
            val query = binding.etSearch.text.toString()
            if (query.isBlank()) {
                Toast.makeText(requireContext(), "The field is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.provideRecipes(query)
        }

        recipeAdapter.setOnItemClickListener { result ->
            findNavController().navigate(
                SearchRecipesFragmentDirections.actionRecipesFragmentToRecipeFragment(result.id)
            )
        }
    }

    private fun setupRecyclerView() = binding.rvRecipes.apply {
        recipeAdapter = RecipeAdapter()
        adapter = recipeAdapter
        layoutManager = LinearLayoutManager(requireContext())

    }

    private fun subscribeToObservers() {

        viewModel.recipeResponseStatus.observe(viewLifecycleOwner, { result ->
            when(result.status) {
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    val data = result.data
                    if (data != null) {
                        recipeAdapter.recipes = data.results
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

    private fun logout() {
        auth.signOut()
        findNavController().navigate(
            SearchRecipesFragmentDirections.actionRecipesFragmentToAuthActivity()
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.miLogout -> logout()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_log_out, menu)
    }
}