package com.example.adventrack.features.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.adventrack.R
import com.example.adventrack.databinding.ActivityRegisterBinding
import com.example.adventrack.features.HomeActivity
import com.example.adventrack.features.login.LoginViewEffect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val mViewModel by viewModels<RegisterViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setUpButton()
        setupObserver()
        setupStatusBar()
    }
    private fun setupStatusBar() {
        val window = this.window ?: return // Safety check in case window is null
        // Get the WindowInsetsController
        WindowCompat.getInsetsController(
            window,
            window.decorView
        ).let { controller ->
            controller.isAppearanceLightStatusBars = true // Set status bar icons to dark
            controller.show(WindowInsetsCompat.Type.systemBars())
        }
    }
    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mViewModel.viewEffect.collectLatest {
                        observeEffect(it)
                    }
                }
            }
        }
    }

    private fun register() {
        val name = binding.etFullName.text.toString()
        val email = binding.etUsername.text.toString()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()
        mViewModel.processEvent(RegisterViewEvent.Register(name, email, password, confirmPassword))
    }

    private fun setUpButton() {
        binding.btnRegister.setOnClickListener {
            register()
        }
    }

    private fun observeEffect(it: RegisterViewEffect) {
        when (it) {
            is RegisterViewEffect.OnLoading -> {
                showLoading(true)
                showError(false)
            }

            is RegisterViewEffect.OnSuccess -> {
                showLoading(false)
                navigateToHome()
            }

            is RegisterViewEffect.OnError -> {
                showLoading(false)
                showError(true)
            }

            is RegisterViewEffect.AuthError -> {
                showLoading(false)
                showError(true)
            }
        }
    }

    private fun showError(isError : Boolean) {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.cpiRegister.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}