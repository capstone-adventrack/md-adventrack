package com.example.adventrack.features.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.adventrack.databinding.FragmentProfileBinding
import com.example.adventrack.domain.model.UserModel
import com.example.adventrack.features.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val mViewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentProfileBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpObserver()
        setupSwipeListener()
        setUpButtonListener()
    }

    private fun setUpObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mViewModel.viewState.collectLatest {
                        setupUserData(it.user)
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

    private fun setupSwipeListener() {
        binding.apply {
            srlProfile.setOnRefreshListener {
                mViewModel.processEvent(ProfileViewEvent.OnRefresh)
                srlProfile.isRefreshing = false
            }
        }
    }

    private fun setUpButtonListener() {
        binding.apply {
            btnLogout.setOnClickListener {
                mViewModel.processEvent(ProfileViewEvent.Logout)
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }

    private fun observeEffect(effect: ProfileViewEffect) {
        when (effect) {
            is ProfileViewEffect.OnLoading -> {
                showLoading(true)
            }
            is ProfileViewEffect.OnSuccess -> {
                showLoading(false)
            }
            is ProfileViewEffect.OnError -> {
                showLoading(false)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.cpiProfile.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupUserData(userModel: UserModel?) {
        binding.apply {
            tvTitleName.text = userModel?.name
            tvSubtitleEmail.text = userModel?.email
            Glide.with(requireContext())
                .load(userModel?.imageUrl)
                .into(sivProfile)
        }
    }
}