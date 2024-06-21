package com.example.adventrack.features.order

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adventrack.R
import com.example.adventrack.databinding.FragmentOrderBinding
import com.example.adventrack.features.order.adapter.OrderAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderFragment : Fragment() {
    private lateinit var binding: FragmentOrderBinding

    private val mOrderAdapter by lazy { OrderAdapter() }

    private val mViewModel by viewModels<OrderViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentOrderBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(
            view,
            savedInstanceState
        )

        setupObserver()
        setupAdapter()
    }

    private fun setupAdapter() {
        binding.apply {
            rvOrder.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            rvOrder.adapter = mOrderAdapter
        }
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mViewModel.viewState.collectLatest {
                        mOrderAdapter.submitList(it.listOrder)
                        showLoading(it.isLoading)
                        if (it.listOrder.isEmpty()) {
                            showEmptyState(true)
                        } else {
                            showEmptyState(false)
                        }
                    }
                }
                launch {
                    mViewModel.viewEffect.collectLatest {
                        Log.d("TAG", "setupObserver: $it")
                        observeEffect(it)
                    }
                }
            }
        }
    }

    private fun observeEffect(it: OrderViewEffect) {
        when (it) {
            is OrderViewEffect.OnError -> {
                showLoading(false)
                showEmptyState(true)
                Toast.makeText(
                    requireContext(),
                    it.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            OrderViewEffect.OnLoading -> {
                showLoading(true)
            }
            is OrderViewEffect.OnSuccess -> {
                Toast.makeText(
                    requireContext(),
                    it.message,
                    Toast.LENGTH_SHORT
                ).show()
                showLoading(false)
                if (mViewModel.viewState.value.listOrder.isEmpty()) {
                    showEmptyState(true)
                } else {
                    showEmptyState(false)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.cpiOrder.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showEmptyState(isEmpty: Boolean) {
        binding.layoutEmptyOrder.root.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }
}