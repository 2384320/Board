package com.dongyang.mysolelife.personalize

import java.io.Serializable

data class PersonalizeModel(
    val title : String?,
    val content : String?,
    val category: String?,
    val upload_time : String?,
    val uid : String?,
    val img_url : String?
) : Serializable