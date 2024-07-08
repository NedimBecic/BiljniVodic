package com.example.spirala1.Models

import com.google.gson.annotations.SerializedName

data class FlowerSearchResponse(
    @SerializedName("data")
    val data : List<PlantResponseModel>,

    @SerializedName("links")
    val links: Links
)