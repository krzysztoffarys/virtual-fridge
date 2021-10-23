package com.example.virtualfridge.ui.fridge

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.virtualfridge.R
import com.example.virtualfridge.adapters.ProductAdapter
import com.example.virtualfridge.data.model.Product
import com.example.virtualfridge.databinding.FridgeFragmentBinding
import com.example.virtualfridge.other.Status
import com.example.virtualfridge.ui.dialogs.AddDialogListener
import com.example.virtualfridge.ui.dialogs.AddProductDialog
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FridgeFragment : Fragment(R.layout.fridge_fragment) {

    @Inject
    lateinit var auth: FirebaseAuth
    private val viewModel: FridgeViewModel by viewModels()
    private lateinit var binding : FridgeFragmentBinding
    private lateinit var productAdapter: ProductAdapter

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
        binding = FridgeFragmentBinding.bind(view)

        //setting our recyclerView
        setupRecyclerView()
        subscribeToObservers()
        viewModel.subscribeToRealtimeUpdates()

        //
        binding.fab.setOnClickListener {
            AddProductDialog(requireContext(), object : AddDialogListener {
                override fun onAddButtonClicked(product: Product) {
                    viewModel.insertProduct(product)
                }
            }).show()
        }

    }

    private fun subscribeToObservers() {

        viewModel.productsStatus.observe(viewLifecycleOwner, { result ->
            when(result.status) {
                Status.SUCCESS -> {
                    productAdapter.products = result.data!!
                }
                Status.ERROR -> {
                    val message = result.message ?: "An unknown error occurred"
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING ->{

                }
            }
        })
    }


    private fun setupRecyclerView() = binding.rv.apply {
        productAdapter = ProductAdapter(viewModel)
        adapter = productAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    //Setting menu

    private fun logout() {
        auth.signOut()
        findNavController().navigate(
            FridgeFragmentDirections.actionFridgeFragmentToAuthActivity()
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