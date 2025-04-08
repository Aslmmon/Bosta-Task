package com.aslmmovic.bosta_task.data.model

data class ApiResponse(
    val success: Boolean,
    val message: String,
    val data: List<City>
)

data class City(
    val cityId: String,
    val cityName: String,
    val cityOtherName: String,
    val cityCode: String,
    val districts: List<District>
)

data class District(
    val zoneId: String,
    val zoneName: String,
    val zoneOtherName: String,
    val districtId: String,
    val districtName: String,
    val districtOtherName: String,
    val pickupAvailability: Boolean,
    val dropOffAvailability: Boolean,
    val coverage: String
)

data class DistrictWithCity(
    val cityId: String,
    val cityName: String,
    val cityOtherName: String,
    val cityCode: String,
    val district: District
)