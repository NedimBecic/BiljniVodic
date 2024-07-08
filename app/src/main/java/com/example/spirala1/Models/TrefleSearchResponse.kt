package com.example.spirala1.Models

import com.google.gson.annotations.SerializedName

data class TrefleSearchResponse(
    @SerializedName("data")
    val data: List<PlantResponseModel>?,

    @SerializedName("links")
    val links: Links?,

    @SerializedName("meta")
    val meta: Meta
)

data class Meta(
    @SerializedName("total")
    val total: Int
)
