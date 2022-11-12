package com.dongyang.mysolelife.board

import java.io.Serializable

data class BoardModel (
    val title : String?,
    val content : String?,
    val time : String?,
    val uid : String?,


    ) : Serializable
