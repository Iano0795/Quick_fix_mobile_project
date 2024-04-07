package com.learn.splashlearn

import java.util.Calendar
import java.util.Date

data class Review(
    val clientId: String,
    val artisanId: String,
    val clientName: String,
    val date: Date = Calendar.getInstance().time,
    val content: String,
    val job: String
)

