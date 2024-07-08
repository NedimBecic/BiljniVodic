package com.example.spirala1.Models

import com.google.gson.annotations.SerializedName

data class PlantResponseModel(
    @SerializedName("id")
    val id: Int,

    @SerializedName("slug")
    val slug: String,

    @SerializedName("common_name")
    val commonName: String,

    @SerializedName("scientific_name")
    val scientificName: String,

    @SerializedName("family")
    val family: String,

    @SerializedName("image_url")
    val imageUrl: String?,

    @SerializedName("main_species")
    val mainSpecies: MainSpecies?,

    @SerializedName("links")
    val links: Links?
)

data class MainSpecies(
    @SerializedName("edible")
    val edible: Boolean?,


    @SerializedName("growth")
    val growth: Growth,

    @SerializedName("specifications")
    val specfications: Specifications
)

data class Specifications(

    @SerializedName("toxicity")
    val toxicity: String,
)
data class Growth(
    @SerializedName("soil_texture")
    val soilTexture: List<String>,

    @SerializedName("light")
    val light: Int,

    @SerializedName("atmospheric_humidity")
    val atmosphericHumidity: Int
)

data class Links(
    @SerializedName("self")
    val self: String?,

    @SerializedName("first")
    val first: String?,

    @SerializedName("last")
    val last: String?,

    @SerializedName("next")
    val next: String?
)
