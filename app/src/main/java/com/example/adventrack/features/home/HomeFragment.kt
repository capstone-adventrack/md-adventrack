package com.example.adventrack.features.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.adventrack.R
import com.example.adventrack.databinding.FragmentHomeBinding
import com.example.adventrack.domain.model.LocationModel
import com.example.adventrack.domain.model.UserModel
import com.example.adventrack.features.detail.DetailActivity
import com.example.adventrack.features.home.adapter.HighRatedPlaceAdapter
import com.example.adventrack.features.home.adapter.NearbyPlaceAdapter
import com.example.adventrack.utils.getScreenWidth
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.FullScreenCarouselStrategy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val mViewModel by viewModels<HomeViewModel>()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val mNearbyPlaceAdapter : NearbyPlaceAdapter by lazy {
        NearbyPlaceAdapter()
    }

    private val mHighRatedPlaceAdapter : HighRatedPlaceAdapter by lazy {
        HighRatedPlaceAdapter()
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
        setupAdapter()
        getMyLastLocation()
        setupSwipeListener()
        setupStatusBar()
    }
    private fun setupStatusBar() {
        val window = activity?.window ?: return // Safety check in case window is null
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
        // Get the WindowInsetsController
        WindowCompat.getInsetsController(
            window,
            window.decorView
        ).let { controller ->
            controller.isAppearanceLightStatusBars = true // Set status bar icons to dark
            controller.show(WindowInsetsCompat.Type.systemBars())
        }
    }
    private fun setupSwipeListener() {
        binding.apply {
            srlHome.setOnRefreshListener {
                mViewModel.processEvent(HomeViewEvent.OnRefresh)
                srlHome.isRefreshing = false
            }
        }
    }

    private fun setupAdapter() {
        val screenWidth = getScreenWidth()
        val screenDensity = resources.displayMetrics.density
        val itemWidth = (170 * screenDensity).toInt()

        binding.apply {
            rvCarousel.layoutManager = CarouselLayoutManager(FullScreenCarouselStrategy())
            rvNearbyPlaces.layoutManager = GridLayoutManager(
                root.context,
                Integer.max(
                    2,
                    screenWidth / itemWidth
                )
            )

            mHighRatedPlaceAdapter.setOnItemClickListener(object : HighRatedPlaceAdapter.OnItemClickListener {
                override fun onItemClick(id: String) {
                    navigateToDetail(id)
                }
            })
            mNearbyPlaceAdapter.setOnItemClickListener(object : NearbyPlaceAdapter.OnItemClickListener {
                override fun onItemClick(id: String) {
                    navigateToDetail(id)
                }
            })

            rvCarousel.adapter = mHighRatedPlaceAdapter
            rvNearbyPlaces.adapter = mNearbyPlaceAdapter
        }

    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mViewModel.viewState.collectLatest {
                        setupLocationData(it.locationModel)
                        setupNearbyPlaces(it.locationModel?.name)
                        setupUserData(it.user)
                        mHighRatedPlaceAdapter.submitList(it.places)
                        mNearbyPlaceAdapter.submitList(it.nearbyPlaces)
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

    private fun setupUserData(userModel: UserModel?) {
        binding.apply {
            appBarHome.tvUserName.text = userModel?.name
            Glide.with(this@HomeFragment)
                .load(userModel?.imageUrl)
                .circleCrop()
                .into(appBarHome.sivProfile)
        }
    }

    private fun setupNearbyPlaces(city: String?) {
        if (city != null) {
            mViewModel.processEvent(HomeViewEvent.GetNearbyPlacesByCity(city))
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
            is HomeViewEffect.OnError -> {
                showLoading(false)
            }
            HomeViewEffect.OnLoading -> {
                showLoading(true)
            }
            is HomeViewEffect.OnSuccess -> {
                showLoading(false)
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

    private fun navigateToDetail(id: String) {
        Intent(
            requireContext(),
            DetailActivity::class.java
        ).apply {
            putExtra(
                DetailActivity.EXTRA_PLACE_ID,
                id
            )
        }.also {
            startActivity(it)
        }
    }
}