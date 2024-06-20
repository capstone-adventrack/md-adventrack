package com.example.adventrack.features.home

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.hardware.display.DisplayManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adventrack.R
import com.example.adventrack.databinding.FragmentHomeBinding
import com.example.adventrack.domain.model.LocationModel
import com.example.adventrack.features.home.adapter.PlaceAdapter
import com.example.adventrack.utils.getScreenWidth
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val mViewModel by viewModels<HomeViewModel>()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val mPlaceAdapter : PlaceAdapter by lazy {
        PlaceAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        binding = FragmentHomeBinding.inflate(
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
        setupCarousel()
        setupAdapter()
        getMyLastLocation()
    }

    private fun setupAdapter() {
        val screenWidth = getScreenWidth()
        val screenDensity = resources.displayMetrics.density
        val itemWidth = (170 * screenDensity).toInt()

        binding.apply {
            rvNearbyPlaces.layoutManager = GridLayoutManager(
                root.context,
                Integer.max(
                    2,
                    screenWidth / itemWidth
                )
            )
            mPlaceAdapter.setOnItemClickListener(object : PlaceAdapter.OnItemClickListener {
                override fun onItemClick(id: String) {
                    Toast.makeText(
                        requireContext(),
                        "Place ID: $id",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

            rvNearbyPlaces.adapter = mPlaceAdapter
        }

    }

    private fun setupCarousel() {
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mViewModel.viewState.collectLatest {
                        setupLocationData(it.locationModel)
                        mPlaceAdapter.submitList(it.places)
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

    private fun setupLocationData(locationModel: LocationModel?) {
        if (locationModel != null) {
            binding.apply {
                Log.d(
                    "HomeFragment",
                    "setupLocationData: $locationModel"
                )
                appBarHome.btnLocation.text = getString(
                    R.string.format_location,
                    locationModel.name,
                    locationModel.country
                )
            }
        }
    }

    private fun observeEffect(effect: HomeViewEffect) {
        when (effect) {
            is HomeViewEffect.OnLoading -> {
                showLoading(true)
                // showError(false)
            }

            is HomeViewEffect.OnSuccess -> {
                showLoading(false)
            }

            is HomeViewEffect.OnError -> {
                showLoading(false)
                Log.e(
                    "HomeFragment",
                    "observeEffect: ${effect.message}"
                )
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.cpiHome.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLastLocation()
                }

                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLastLocation()
                }

                else -> {
                    // No location access granted.
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    mViewModel.processEvent(HomeViewEvent.GetLocation(location))
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
}