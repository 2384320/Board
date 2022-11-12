package com.dongyang.mysolelife.boardDaily

import java.io.Serializable

data class BoardDailyModel(
    val title : String?,
    val content : String?,
    val category: String?,
    val time : String?,
    val uid : String?,
    val img_url : String?
) : Serializable
