package com.example.spirala1.Models

import com.example.spirala1.Models.Links
import com.example.spirala1.Models.SinglePlantResponseModel
import com.google.gson.annotations.SerializedName

data class TrefleSingleSearch(
    @SerializedName("data")
    val data: SinglePlantResponseModel,

    @SerializedName("links")
    val links: Links?
)