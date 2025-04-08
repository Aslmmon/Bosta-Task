package com.aslmmovic.bosta_task

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.aslmmovic.bosta_task.data.model.DistrictWithCity
import com.aslmmovic.bosta_task.presenation.ui.CitiesAdapter
import com.aslmmovic.bosta_task.presenation.ui.CitiesViewModel
import com.aslmmovic.bosta_task.presenation.ui.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * MainActivity is an AppCompatActivity that displays a list of cities.
 * It uses Hilt for dependency injection and ViewModel to manage UI state.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: CitiesViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView

    /**
     * Called when the activity is first created.
     * Initializes views, observes UI state, and fetches cities.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        initializeViews()
        observeUiState()
        viewModel.fetchCities("60e4482c7cb7d4bc4849c4d5")
    }

    /**
     * Initializes the RecyclerView, ProgressBar, and Error TextView.
     */
    private fun initializeViews() {
        recyclerView = findViewById(R.id.citiesRecyclerView)
        progressBar = findViewById(R.id.progressBar)
        errorTextView = findViewById(R.id.errorTextView)
    }

    /**
     * Observes the UI state from the ViewModel and updates the UI accordingly.
     * Uses lifecycleScope to collect the Flow and handle different UiState cases.
     */
    private fun observeUiState() {
        lifecycleScope.launch {
            viewModel.uiState.observe(this@MainActivity) { state ->
                when (state) {
                    is UiState.Loading -> handleLoadingState()
                    is UiState.Success -> handleSuccessState(state)
                    is UiState.Error -> handleErrorState(state)
                }
            }
        }
    }

    /**
     * Handles the loading state by showing the ProgressBar and hiding the RecyclerView and Error TextView.
     */
    private fun handleLoadingState() {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        errorTextView.visibility = View.GONE
    }

    /**
     * Handles the success state by hiding the ProgressBar, showing the RecyclerView, and setting the adapter.
     *
     * @param state The UiState.Success containing the list of cities.
     */
    private fun handleSuccessState(state: UiState.Success<List<DistrictWithCity>>) {
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        errorTextView.visibility = View.GONE
        recyclerView.adapter = CitiesAdapter(state.data)
    }

    /**
     * Handles the error state by hiding the ProgressBar, showing the Error TextView, and displaying a Toast message.
     *
     * @param state The UiState.Error containing the error message.
     */
    private fun handleErrorState(state: UiState.Error) {
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.GONE
        errorTextView.visibility = View.VISIBLE
        errorTextView.text = state.message
        Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
    }
}