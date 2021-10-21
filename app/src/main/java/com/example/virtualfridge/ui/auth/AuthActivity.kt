package com.example.virtualfridge.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.virtualfridge.databinding.ActivityAuthBinding
import com.example.virtualfridge.other.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private val viewModel: AuthViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        subscribeToObservers()

        binding.btnRegister.setOnClickListener {
            val email = binding.etRegisterEmail.text.toString()
            val password = binding.etRegisterPassword.text.toString()
            val confirmedPassword = binding.etRegisterPasswordConfirm.text.toString()
            viewModel.registerUser(email, password, confirmedPassword)
        }



    }

    private fun subscribeToObservers() {
        viewModel.registerStatus.observe(this, { result ->
            when(result.status) {
                Status.SUCCESS -> {
                    binding.registerProgressBar.visibility = View.GONE
                    val message = result.message ?: "Successfully registered an account"
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
                Status.ERROR -> {
                    binding.registerProgressBar.visibility = View.GONE
                    val message = result.message ?: "An unknown error occurred"
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                    binding.registerProgressBar.visibility = View.VISIBLE
                }
            }
        })
    }
}