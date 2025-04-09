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

class CitiesAdapter :
    RecyclerView.Adapter<CitiesAdapter.CityViewHolder>() {

    private var citiesWithDistricts: List<City> = emptyList()

    // 1. DiffUtil Callback
    private val diffCallback = object : DiffUtil.ItemCallback<City>() {
        override fun areItemsTheSame(
            oldItem: City,
            newItem: City
        ): Boolean {
            return oldItem.cityId == newItem.cityId
        }

        override fun areContentsTheSame(
            oldItem: City,
            newItem: City
        ): Boolean {
            return oldItem == newItem // Adjust this if needed for more granular checks
        }
    }

    // 2. AsyncListDiffer
    private val differ = AsyncListDiffer(this, diffCallback)

    init {
        setHasStableIds(true) // Important for proper item updates with DiffUtil
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.city_item, parent, false)
        return CityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val cityWithDistricts = differ.currentList[position]
        holder.bind(cityWithDistricts)
    }

    override fun getItemCount() = differ.currentList.size

    override fun getItemId(position: Int): Long {
        return differ.currentList[position].cityId.hashCode().toLong() // Use a stable ID
    }

    // 3. SubmitList
    fun submitList(newList: List<City>) {
        differ.submitList(newList)
    }

    inner class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cityNameTextView: TextView = itemView.findViewById(R.id.cityNameTextView)
        private val expandCollapseIcon: ImageView = itemView.findViewById(R.id.expandCollapseIcon)
        private val districtsRecyclerView: RecyclerView =
            itemView.findViewById(R.id.districtsRecyclerView)
        private val cityHeader: LinearLayout = itemView.findViewById(R.id.cityHeader)
        private lateinit var districtsAdapter: DistrictsAdapter

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

    // 4. Nested Adapter for Districts
    class DistrictsAdapter(private val districts: List<District>) :
        RecyclerView.Adapter<DistrictsAdapter.DistrictViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DistrictViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.district_item, parent, false)
            return DistrictViewHolder(view)
        }

        override fun onBindViewHolder(holder: DistrictViewHolder, position: Int) {
            val district = districts[position]
            holder.bind(district)
        }

        override fun getItemCount() = districts.size

        inner class DistrictViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val districtNameTextView: TextView =
                itemView.findViewById(R.id.districtNameTextView)

            fun bind(district: District) {
                districtNameTextView.text = district.districtName
            }
        }
    }
}
