package com.example.virtualfridge.ui.fridge

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.virtualfridge.R
import com.example.virtualfridge.data.model.Product
import com.example.virtualfridge.databinding.FridgeFragmentBinding
import com.example.virtualfridge.ui.dialogs.AddDialogListener
import com.example.virtualfridge.ui.dialogs.AddProductDialog
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FridgeFragment : Fragment(R.layout.fridge_fragment) {

    @Inject
    lateinit var auth: FirebaseAuth
    private lateinit var binding : FridgeFragmentBinding

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

        binding.fab.setOnClickListener {
            AddProductDialog(requireContext(), object : AddDialogListener {
                override fun onAddButtonClicked(product: Product) {
                    "Hey"
                }
            }).show()
        }

    }

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