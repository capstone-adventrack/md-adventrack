package com.example.adventrack.features.transaction

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adventrack.databinding.ActivityTransactionBinding
import com.example.adventrack.domain.model.CheckoutModel
import com.example.adventrack.utils.withCurrencyFormat

class TransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransactionBinding
    private var data: CheckoutModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        fetchData()
        setupData()
    }

    private fun setupData() {
        binding.apply {
            layoutTransactionPay.tvTotalPrice.text = data?.totalPrice.toString().withCurrencyFormat()
        }
    }

    private fun fetchData() {
        data = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(
                EXTRA_CHECKOUT,
                CheckoutModel::class.java
            )
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_CHECKOUT)
        }
    }

    companion object {
        const val EXTRA_CHECKOUT = "extra_checkout"
    }
}