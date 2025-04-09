package com.aslmmovic.bosta_task.presenation.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aslmmovic.bosta_task.R
import com.aslmmovic.bosta_task.data.model.City
import com.aslmmovic.bosta_task.data.model.District

/**
 * Adapter for displaying a list of cities and their associated districts in a RecyclerView.
 *
 * This adapter uses DiffUtil and AsyncListDiffer for efficient list updates and provides
 * functionality to expand/collapse the list of districts for each city.
 */
class CitiesAdapter :
    RecyclerView.Adapter<CitiesAdapter.CityViewHolder>() {

    /**
     * DiffUtil Callback to calculate the differences between old and new lists of cities.
     */
    private val diffCallback = object : DiffUtil.ItemCallback<City>() {
        /**
         * Checks if two city items are the same based on their unique identifiers.
         *
         * @param oldItem The old city item.
         * @param newItem The new city item.
         * @return True if the items are the same, false otherwise.
         */
        override fun areItemsTheSame(
            oldItem: City,
            newItem: City
        ): Boolean {
            return oldItem.cityId == newItem.cityId
        }

        /**
         * Checks if the content of two city items is the same.
         *
         * @param oldItem The old city item.
         * @param newItem The new city item.
         * @return True if the content is the same, false otherwise.
         * You might need to adjust this for more granular checks if your City class has complex properties.
         */
        override fun areContentsTheSame(
            oldItem: City,
            newItem: City
        ): Boolean {
            return oldItem == newItem
        }
    }

    /**
     * AsyncListDiffer to handle the background thread diffing and updating of the RecyclerView.
     */
    private val differ = AsyncListDiffer(this, diffCallback)

    init {
        setHasStableIds(true) // Important for proper item updates with DiffUtil
    }

    /**
     * Creates a new ViewHolder for a city item.
     *
     * @param parent The parent ViewGroup.
     * @param viewType The view type of the new View.
     * @return A new CityViewHolder instance.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.city_item, parent, false)
        return CityViewHolder(view)
    }

    /**
     * Binds the data to the ViewHolder at the specified position.
     *
     * @param holder The ViewHolder to bind the data to.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = differ.currentList[position]
        holder.bind(city)
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The size of the current list.
     */
    override fun getItemCount() = differ.currentList.size

    /**
     * Returns a stable ID for the item at the specified position.
     *
     * @param position The position of the item within the adapter's data set.
     * @return A unique and stable ID for the city item.
     */
    override fun getItemId(position: Int): Long {
        return differ.currentList[position].cityId.hashCode().toLong() // Use a stable ID
    }

    /**
     * Submits a new list of cities to the adapter.
     *
     * This method uses DiffUtil to calculate the differences between the old and new lists
     * and efficiently updates the RecyclerView.
     *
     * @param newList The new list of cities to display.
     */
    fun submitList(newList: List<City>) {
        differ.submitList(newList)
    }

    /**
     * ViewHolder for a single city item in the RecyclerView.
     *
     * This ViewHolder displays the city name and a list of districts, with the ability
     * to expand/collapse the district list.
     */
    inner class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cityNameTextView: TextView = itemView.findViewById(R.id.cityNameTextView)
        private val expandCollapseIcon: ImageView = itemView.findViewById(R.id.expandCollapseIcon)
        private val districtsRecyclerView: RecyclerView =
            itemView.findViewById(R.id.districtsRecyclerView)
        private lateinit var districtsAdapter: DistrictsAdapter

        /**
         * Binds the city data to the ViewHolder.
         *
         * @param city The city data to display.
         */
        fun bind(city: City) {
            cityNameTextView.text = city.cityName

            districtsAdapter = DistrictsAdapter(city.districts)
            districtsRecyclerView.adapter = districtsAdapter

            val isExpanded = city.isExpanded

            districtsRecyclerView.visibility = if (isExpanded) View.VISIBLE else View.GONE
            expandCollapseIcon.setImageResource(if (isExpanded) R.drawable.arrow_up else R.drawable.arrow_down)

            itemView.setOnClickListener {
                city.isExpanded = !city.isExpanded
                notifyItemChanged(adapterPosition)
            }
        }
    }

    /**
     * Adapter for displaying a list of districts within a city item.
     */
    class DistrictsAdapter(private val districts: List<District>) :
        RecyclerView.Adapter<DistrictsAdapter.DistrictViewHolder>() {

        /**
         * Creates a new ViewHolder for a district item.
         *
         * @param parent The parent ViewGroup.
         * @param viewType The view type of the new View.
         * @return A new DistrictViewHolder instance.
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DistrictViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.district_item, parent, false)
            return DistrictViewHolder(view)
        }

        /**
         * Binds the district data to the ViewHolder at the specified position.
         *
         * @param holder The ViewHolder to bind the data to.
         * @param position The position of the item within the adapter's data set.
         */
        override fun onBindViewHolder(holder: DistrictViewHolder, position: Int) {
            val district = districts[position]
            holder.bind(district)
        }

        /**
         * Returns the total number of items in the data set held by the adapter.
         *
         * @return The size of the current list.
         */
        override fun getItemCount() = districts.size

        /**
         * ViewHolder for a single district item in the RecyclerView.
         *
         * This ViewHolder displays the district name.
         */
        inner class DistrictViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val districtNameTextView: TextView =
                itemView.findViewById(R.id.districtNameTextView)

            /**
             * Binds the district data to the ViewHolder.
             *
             * @param district The district data to display.
             */
            fun bind(district: District) {
                districtNameTextView.text = district.districtName
            }
        }
    }
}