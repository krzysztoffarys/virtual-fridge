package com.example.virtualfridge.ui.saved

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.virtualfridge.R
import com.example.virtualfridge.adapters.RecipeAdapter
import com.example.virtualfridge.databinding.SavedRecipesFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SavedRecipesFragment : Fragment(R.layout.saved_recipes_fragment) {

    @Inject
    lateinit var auth: FirebaseAuth
    private lateinit var binding: SavedRecipesFragmentBinding
    private lateinit var recipeAdapter: RecipeAdapter
    val viewModel: SavedRecipesViewModel by viewModels()


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
        binding = SavedRecipesFragmentBinding.bind(view)
        setupRecyclerView()
        subscribeToObservers()
        viewModel.savedRecipes()

        recipeAdapter.setOnItemClickListener {
            findNavController().navigate(
                SavedRecipesFragmentDirections.actionSavedRecipesFragmentToRecipeFragment(it)
            )
        }
    }

    private fun subscribeToObservers() {

        viewModel.savedRecipes().observe(viewLifecycleOwner, { recipe ->
            recipeAdapter.recipes = recipe
        })
    }

    private fun setupRecyclerView() = binding.rv.apply {
        recipeAdapter = RecipeAdapter()
        adapter = recipeAdapter
        layoutManager = LinearLayoutManager(requireContext())

    }

    private fun logout() {
        auth.signOut()
        findNavController().navigate(
            SavedRecipesFragmentDirections.actionSavedRecipesFragmentToAuthActivity()
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