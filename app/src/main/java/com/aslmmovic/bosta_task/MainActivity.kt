package  com.aslmmovic.bosta_task

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels // or appropriate delegate for Fragments
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aslmmovic.bosta_task.R
import com.aslmmovic.bosta_task.presenation.ui.CitiesAdapter
import com.aslmmovic.bosta_task.presenation.ui.CitiesViewModel
import com.aslmmovic.bosta_task.presenation.ui.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: CitiesViewModel by viewModels() // Or createViewModel() for Fragments
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView
    private lateinit var cityAreaEditText: EditText
    private lateinit var citiesAdapter: CitiesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        setupRecyclerView()
        observeUiState()

        // Initial data fetch (replace with your actual countryId)
       viewModel.fetchCities("60e4482c7cb7d4bc4849c4d5")

//        // Set up text change listener for filtering (if needed)
//        cityAreaEditText.setOnKeyListener { _, _, _ ->
//            filterCities()
//            false // Return false to allow default key processing
//        }
    }

    private fun initializeViews() {
        recyclerView = findViewById(R.id.citiesRecyclerView)
        progressBar = findViewById(R.id.progressBar)
        errorTextView = findViewById(R.id.errorTextView)
        cityAreaEditText = findViewById(R.id.cityAreaEditText)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        citiesAdapter = CitiesAdapter()
        recyclerView.adapter = citiesAdapter
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            viewModel.uiState.observe(this@MainActivity) { state ->
                when (state) {
                    is UiState.Loading -> {
                        progressBar.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                        errorTextView.visibility = View.GONE
                    }

                    is UiState.Success -> {
                        progressBar.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        errorTextView.visibility = View.GONE
                        citiesAdapter.submitList(state.data)
                    }

                    is UiState.Error -> {
                        progressBar.visibility = View.GONE
                        recyclerView.visibility = View.GONE
                        errorTextView.visibility = View.VISIBLE
                        errorTextView.text = state.message
                    }
                }
            }
        }
    }

//    private fun filterCities() {
//        val query = cityAreaEditText.text.toString().trim()
//        if (query.isNotEmpty()) {
//            // Implement your filtering logic here based on the query
//            // For example, filter the list from the ViewModel and submit it to the adapter
//            val filteredList = viewModel.uiState.value.let { state ->
//                if (state is UiState.Success) {
//                    state.data.filter { city ->
//                        city.cityName.contains(query, ignoreCase = true)
//                    }.map { city ->
//                        CityWithDistricts(city, city.districts)
//                    }
//                } else {
//                    emptyList()
//                }
//            }
//            citiesAdapter.submitList(filteredList)
//        } else {
//            // If the query is empty, submit the original list
//            viewModel.uiState.value.let { state ->
//                if (state is UiState.Success) {
//                    val originalList = state.data.map { city ->
//                        CityWithDistricts(city, city.districts)
//                    }
//                    citiesAdapter.submitList(originalList)
//                }
//            }
//        }
//    }
}