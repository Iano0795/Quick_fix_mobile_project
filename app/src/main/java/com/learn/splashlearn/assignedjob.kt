package com.learn.splashlearn

import java.util.Calendar
import java.util.Date

data class AssignedJob(
    val clientId: String,
    val artisanId: String,
    val clientName: String,
    val artisanName: String,
    val mobileNumber: String,
    val jobDescription: String,
    val date: Date = Calendar.getInstance().time,
    val jobId: String
)
