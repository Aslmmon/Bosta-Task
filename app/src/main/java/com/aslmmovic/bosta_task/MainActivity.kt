package com.aslmmovic.bosta_task

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aslmmovic.bosta_task.data.model.City
import com.aslmmovic.bosta_task.presenation.ui.CitiesAdapter
import com.aslmmovic.bosta_task.presenation.ui.CitiesViewModel
import com.aslmmovic.bosta_task.presenation.ui.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * The main activity of the application, responsible for displaying a list of cities and districts.
 *
 * This activity uses Hilt for dependency injection, retrieves data from a [CitiesViewModel],
 * and displays it in a [RecyclerView] using a [CitiesAdapter].
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: CitiesViewModel by viewModels() // Or createViewModel() for Fragments

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView
    private lateinit var cityAreaEditText: EditText
    private lateinit var citiesAdapter: CitiesAdapter

    /**
     * Called when the activity is first created. Initializes the UI and starts data loading.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down
     * then this Bundle contains the data it most recently supplied in
     * [onSaveInstanceState]. Note: This value may be null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeViews()
        setupRecyclerView()
        observeUiState()
        observeErrorMessage() // Observe the new errorMessage flow

        viewModel.fetchCities("60e4482c7cb7d4bc4849c4d5")
    }

    /**
     * Initializes the views used in the activity.
     */
    private fun initializeViews() {
        recyclerView = findViewById(R.id.citiesRecyclerView)
        progressBar = findViewById(R.id.progressBar)
        errorTextView = findViewById(R.id.errorTextView)
        cityAreaEditText = findViewById(R.id.cityAreaEditText)
    }

    /**
     * Sets up the RecyclerView with a layout manager and adapter.
     */
    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        citiesAdapter = CitiesAdapter()
        recyclerView.adapter = citiesAdapter
    }

    /**
     * Observes the [UiState] from the [CitiesViewModel] and updates the UI accordingly.
     */
    private fun observeUiState() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is UiState.Loading -> handleLoadingState()
                    is UiState.Success -> handleSuccessState(state)
                    is UiState.Error -> handleErrorState()
                }
            }
        }
    }

    /**
     * Handles the [UiState.Loading] state by showing the loading indicator
     * and hiding the RecyclerView and error message.
     */
    private fun handleLoadingState() {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        errorTextView.visibility = View.GONE
    }

    /**
     * Handles the [UiState.Success] state by hiding the loading indicator
     * and error message, and displaying the list of cities in the RecyclerView.
     *
     * @param state The [UiState.Success] state containing the data.
     */
    private fun handleSuccessState(state: UiState.Success<List<City>>) {
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        errorTextView.visibility = View.GONE
        citiesAdapter.submitList(state.data)
    }

    /**
     * Handles the [UiState.Error] state by hiding the loading indicator
     * and RecyclerView, and displaying the error message.
     *
     * @param state The [UiState.Error] state containing the error message.
     */
    private fun handleErrorState() {
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.GONE
        errorTextView.visibility = View.VISIBLE
    }

    private fun observeErrorMessage() {
        lifecycleScope.launch {
            viewModel.errorMessage.collectLatest { message ->
                if (message != null) {
                    errorTextView.visibility = View.VISIBLE
                    errorTextView.text = message
                } else {
                    errorTextView.visibility = View.GONE
                }
            }
        }
    }

}