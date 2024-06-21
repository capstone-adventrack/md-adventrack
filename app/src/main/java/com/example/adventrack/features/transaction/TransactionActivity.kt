package com.example.adventrack.features.transaction

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adventrack.R
import com.example.adventrack.databinding.ActivityTransactionBinding
import com.example.adventrack.domain.model.CheckoutModel
import com.example.adventrack.features.HomeActivity
import com.example.adventrack.features.transaction.adapter.PurchasedTicketAdapter
import com.example.adventrack.utils.withCurrencyFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransactionBinding
    private var data: CheckoutModel? = null

    private val mViewModel by viewModels<TransactionViewModel>()

    private val mPurchasedTicketAdapter by lazy { PurchasedTicketAdapter() }
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
        setupObserver()
        setupAdapter()
        setupToolbar()
        setupSwipeListener()
        setupClickListener()
        setupStatusBar()
    }

    private fun setupStatusBar() {
        val window = this.window ?: return // Safety check in case window is null
        // Get the WindowInsetsController
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        WindowCompat.getInsetsController(
            window,
            window.decorView
        ).let { controller ->
            controller.isAppearanceLightStatusBars = true // Set status bar icons to dark
            controller.show(WindowInsetsCompat.Type.systemBars())
        }
    }

    private fun setupClickListener() {
        binding.apply {
            layoutTransactionPay.btnPay.setOnClickListener {
                mViewModel.processEvent(TransactionViewEvent.OnPay)
            }
        }
    }

    private fun setupSwipeListener() {
        binding.srlTransaction.setOnRefreshListener {
            fetchData()
            binding.srlTransaction.isRefreshing = false
        }
    }

    private fun setupToolbar() {
        binding.apply {
            appBarTransaction.tvTitle.text = getString(R.string.label_transaction)
            appBarTransaction.btnBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mViewModel.viewState.collectLatest {
                        setupPrice(it.totalPrice)
                        mPurchasedTicketAdapter.submitList(it.listTicket)
                    }
                }
                launch {
                    mViewModel.viewEffect.collectLatest {
                        observeEffect(it)
                    }
                }
            }
        }
    }

    private fun observeEffect(it: TransactionViewEffect) {
        when (it) {
            is TransactionViewEffect.OnError -> {
                showLoading(false)
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
            TransactionViewEffect.OnLoading -> {
                showLoading(true)
            }
            is TransactionViewEffect.OnSuccess -> {
                showLoading(false)
                navigateToHome()
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupAdapter() {
        binding.apply {
            rvTicket.layoutManager = LinearLayoutManager(
                this@TransactionActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            rvTicket.adapter = mPurchasedTicketAdapter
        }
    }

    private fun setupPrice(totalPrice: Int) {
        binding.apply {
            layoutTransactionPay.tvTotalPrice.text = totalPrice.toString().withCurrencyFormat()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.cpiTransaction.visibility = if (isLoading) View.VISIBLE else View.GONE
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
        mViewModel.processEvent(TransactionViewEvent.SetTicketList(data?.listTicket ?: emptyList()))
        mViewModel.processEvent(TransactionViewEvent.SetTotalPrice(data?.totalPrice ?: 0))
    }

    private fun navigateToHome() {
        val intent = Intent(
            this,
            HomeActivity::class.java
        )
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    companion object {
        const val EXTRA_CHECKOUT = "extra_checkout"
    }
}