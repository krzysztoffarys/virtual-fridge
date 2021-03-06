package com.example.virtualfridge.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.virtualfridge.R
import com.example.virtualfridge.databinding.ActivityAuthBinding
import com.example.virtualfridge.other.Status
import com.example.virtualfridge.ui.FridgeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private val viewModel: AuthViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //
        if (viewModel.isLoggedIn()) {
            redirectLogin()
        }


        subscribeToObservers()
        binding.btnLogin.setOnClickListener {
            val email = binding.etLoginEmail.text.toString()
            val password = binding.etLoginPassword.text.toString()
            viewModel.loginUser(email, password)
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.etRegisterEmail.text.toString()
            val password = binding.etRegisterPassword.text.toString()
            val confirmedPassword = binding.etRegisterPasswordConfirm.text.toString()
            viewModel.registerUser(email, password, confirmedPassword)
        }



    }

    private fun subscribeToObservers() {

        viewModel.loginStatus.observe(this, { result ->
            when(result.status) {
                Status.SUCCESS -> {
                    binding.loginProgressBar.visibility = View.GONE
                    val message = result.message ?: getString(R.string.successful_login)
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    redirectLogin()
                }
                Status.ERROR -> {
                    binding.loginProgressBar.visibility = View.GONE
                    val message = result.message ?: getString(R.string.unknown_error)
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                    binding.loginProgressBar.visibility = View.VISIBLE
                }
            }
        })


        //
        //
        viewModel.registerStatus.observe(this, { result ->
            when(result.status) {
                Status.SUCCESS -> {
                    binding.registerProgressBar.visibility = View.GONE
                    val message = result.message ?: getString(R.string.successful_registration)
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
                Status.ERROR -> {
                    binding.registerProgressBar.visibility = View.GONE
                    val message = result.message ?: getString(R.string.unknown_error)
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                    binding.registerProgressBar.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun redirectLogin() {
        Intent(this, FridgeActivity::class.java).also {
            startActivity(it)
        }
    }
}