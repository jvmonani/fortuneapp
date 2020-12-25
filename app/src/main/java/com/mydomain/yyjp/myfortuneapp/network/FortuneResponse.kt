package com.mydomain.yyjp.myfortuneapp.network

import com.google.gson.annotations.SerializedName


class FortuneResponse(
    @SerializedName("fortune") val items: List<String>
)
