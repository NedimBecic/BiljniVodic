package com.example.spirala1.Models

import com.google.gson.annotations.SerializedName

data class SinglePlantResponseModel(

    @SerializedName("id")
val id: Int,

    @SerializedName("slug")
val slug: String,

    @SerializedName("common_name")
val commonName: String,

    @SerializedName("scientific_name")
val scientificName: String,

    @SerializedName("family")
val family: Family,

    @SerializedName("image_url")
val imageUrl: String?,

    @SerializedName("main_species")
val mainSpecies: MainSpecies?,

    @SerializedName("links")
val links: Links?
)

data class Family(
    @SerializedName("name")
    val name: String
)






