package com.ahrokholska.gifs.data.network.models

import com.google.gson.annotations.SerializedName

data class Meta(
    val msg: String,
    val status: Int,
    @SerializedName("response_id")
    val responseId: String
)