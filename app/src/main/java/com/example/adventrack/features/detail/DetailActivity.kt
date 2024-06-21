package com.example.adventrack.features.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adventrack.R
import com.example.adventrack.databinding.ActivityDetailBinding
import com.example.adventrack.domain.model.CheckoutModel
import com.example.adventrack.domain.model.EntryTicketModel
import com.example.adventrack.domain.model.PlaceModel
import com.example.adventrack.features.detail.adapter.DetailPlaceAdapter
import com.example.adventrack.features.detail.adapter.EntryTicketAdapter
import com.example.adventrack.features.home.HomeViewEvent
import com.example.adventrack.features.transaction.TransactionActivity
import com.example.adventrack.utils.withCurrencyFormat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.HeroCarouselStrategy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityDetailBinding

    private lateinit var mMap: GoogleMap

    private val placeId by lazy {
        intent.getStringExtra(EXTRA_PLACE_ID) ?: ""
    }

    private val mViewModel by viewModels<DetailViewModel>()

    private val mDetailPlaceAdapter: DetailPlaceAdapter by lazy {
        DetailPlaceAdapter()
    }
    private val mEntryTicketAdapter: EntryTicketAdapter by lazy {
        EntryTicketAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
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

        setupToolbar()
        setupObservers()
        getData()
        setupAdapter()
        setupClickListeners()
        setupSwipeListener()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setupClickListeners() {
        binding.apply {
            layoutCheckout.btnCheckout.setOnClickListener {
                val listTicket = mViewModel.viewState.value.entryTicketList.filter { it.quantity > 0 }
                if (listTicket.isEmpty()) {
                    Toast.makeText(
                        this@DetailActivity,
                        getString(R.string.label_please_add_item_to_checkout),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                navigateToTransaction(
                    CheckoutModel(
                        mViewModel.viewState.value.entryTicketList,
                        mViewModel.viewState.value.totalPrice
                    )
                )
            }
        }
    }

    private fun setupEntryTicket(entryTicketList: List<EntryTicketModel>) {
        if (entryTicketList.isEmpty()) {
            if (mViewModel.viewState.value.placeModel == null) {
                return
            }
            val entryTicket = listOf(
                EntryTicketModel(
                    getString(
                        R.string.format_title_ticket_adult,
                        mViewModel.viewState.value.placeModel?.name.orEmpty()
                    ),
                    getString(
                        R.string.format_desc_ticket,
                        mViewModel.viewState.value.placeModel?.name
                    ),
                    getString(
                        R.string.format_price_IDR,
                        mViewModel.viewState.value.placeModel?.adultPrice
                    ),
                    0,
                    "Adult",
                    mViewModel.viewState.value.placeModel?.address.orEmpty(),
                    System.currentTimeMillis().toString()
                ),
                EntryTicketModel(
                    getString(
                        R.string.format_title_ticket_child,
                        mViewModel.viewState.value.placeModel?.name.orEmpty()
                    ),
                    getString(
                        R.string.format_desc_ticket,
                        mViewModel.viewState.value.placeModel?.name
                    ),
                    getString(
                        R.string.format_price_IDR,
                        mViewModel.viewState.value.placeModel?.childPrice
                    ),
                    0,
                    "Child",
                    mViewModel.viewState.value.placeModel?.address.orEmpty(),
                    System.currentTimeMillis().toString()
                )
            )
            mViewModel.processEvent(DetailViewEvent.GetEntryTicketList(entryTicket))
        } else {
            mViewModel.processEvent(DetailViewEvent.GetEntryTicketList(entryTicketList))
            mEntryTicketAdapter.submitList(mViewModel.viewState.value.entryTicketList)
        }
    }

    private fun setupAdapter() {
        binding.apply {
            rvBanner.layoutManager = CarouselLayoutManager(HeroCarouselStrategy())
            rvBanner.adapter = mDetailPlaceAdapter

            rvEntryTicket.layoutManager = LinearLayoutManager(
                this@DetailActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            mEntryTicketAdapter.setOnItemClickListener(object :
                EntryTicketAdapter.OnItemClickListener {
                override fun onAddItemClick(type: String) {
                    mViewModel.processEvent(DetailViewEvent.OnAddEntryTicket(type))
                }

                override fun onMinusItemClick(type: String) {
                    mViewModel.processEvent(DetailViewEvent.OnMinusEntryTicket(type))
                }
            })
            rvEntryTicket.adapter = mEntryTicketAdapter
        }
    }

    private fun getData() {
        mViewModel.processEvent(DetailViewEvent.GetPlaceById(placeId))
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mViewModel.viewState.collectLatest {
                        setupData(it.placeModel)
                        mDetailPlaceAdapter.submitList(it.placeModel?.imageUrl)
                        setupEntryTicket(it.entryTicketList)
                        setupPrice(it.totalPrice)
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
            srlDetail.setOnRefreshListener {
                mViewModel.processEvent(DetailViewEvent.OnRefresh)
                srlDetail.isRefreshing = false
            }
        }
    }

    private fun setupPrice(totalPrice: Int) {
        binding.apply {
            layoutCheckout.tvTotalPrice.text = totalPrice.toString().withCurrencyFormat()
        }
    }

    private fun setupData(placeModel: PlaceModel?) {
        binding.apply {
            appBarDetail.tvTitle.text = placeModel?.name
            tvDescPlace.text = placeModel?.description
            tvTitlePlace.text = placeModel?.name
            tvRating.text = placeModel?.rating.toString()
            tvTime.text = getString(
                R.string.format_time,
                placeModel?.openTime,
                placeModel?.closeTime
            )
            showEmptyTicket(placeModel?.activityTicket.isNullOrEmpty())
        }
    }

    private fun observeEffect(it: DetailViewEffect) {
        when (it) {
            is DetailViewEffect.OnLoading -> {
                showLoading(true)
            }

            is DetailViewEffect.OnSuccess -> {
                showLoading(false)
            }

            is DetailViewEffect.OnError -> {
                showLoading(false)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.cpiDetail.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showEmptyTicket(isEmpty: Boolean) {
        binding.apply {
            layoutEmptyTicket.root.visibility = if (isEmpty) View.VISIBLE else View.GONE
            rvActivityTicket.visibility = if (isEmpty) View.GONE else View.VISIBLE
            if (isEmpty) {
                val params = tvTitleLocation.layoutParams as ConstraintLayout.LayoutParams
                params.topToBottom = layoutEmptyTicket.root.id
                tvTitleLocation.layoutParams = params
            }
        }
    }

    private fun setupToolbar() {
        binding.apply {
            appBarDetail.btnBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mViewModel.viewState.collectLatest { viewState ->
                    val placeModel = viewState.placeModel
                    if (placeModel != null) {
                        // Place model is available, add the marker
                        val location = com.google.android.gms.maps.model.LatLng(
                            placeModel.latitude ?: 0.0,
                            placeModel.longitude ?: 0.0
                        )

                        mMap.addMarker(
                            MarkerOptions()
                                .position(location)
                                .title(placeModel.name)
                        )

                        mMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                location,
                                15f
                            )
                        )
                    }
                }
            }
        }
    }

    private fun navigateToTransaction(checkoutModel: CheckoutModel) {
        val intent = Intent(
            this,
            TransactionActivity::class.java
        )
        intent.putExtra(
            TransactionActivity.EXTRA_CHECKOUT,
            checkoutModel
        )
        startActivity(intent)
    }

    companion object {
        const val EXTRA_PLACE_ID = "place_id"
    }

}