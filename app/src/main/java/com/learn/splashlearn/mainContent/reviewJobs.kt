package com.learn.splashlearn.mainContent

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.learn.splashlearn.Artisan
import com.learn.splashlearn.AssignedJob
import com.learn.splashlearn.Navigation
import com.learn.splashlearn.R
import com.learn.splashlearn.Review
import com.learn.splashlearn.User
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@Composable
fun ReviewJobs(user: User?) {
    val name = user?.name ?: ""
    val auth = Firebase.auth
    var loading by remember { mutableStateOf(false) }
    val clientId = auth.currentUser?.uid ?: ""
    val context = LocalContext.current
    var jobs by remember { mutableStateOf<List<AssignedJob>>(emptyList()) }
    val reviewedJobIds by remember { mutableStateOf(mutableSetOf<String>()) }
    LaunchedEffect(Unit) {
        loading = true
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("assignedJobs")
            .whereEqualTo("clientId", clientId)
            .get()
            .addOnSuccessListener { documents ->
                val newJobs = documents.map { document ->
                    val jobId = document.id
                    val artId = document.getString("artisanId") ?: ""
                    val clientName = document.getString("clientName") ?: ""
                    val timestamp = document.getTimestamp("date")
                    val date = timestamp?.toDate() ?: Date()
                    val jobDescription = document.getString("jobDescription") ?: ""
                    AssignedJob(clientId, artId, clientName, name, "", jobDescription, date, jobId)
                }
                jobs = newJobs.filter { !reviewedJobIds.contains(it.jobId) }
                loading = false
            }
            .addOnFailureListener { exception ->
                // Handle failure
                Toast.makeText(context, exception.message, Toast.LENGTH_LONG).show()
                loading = false
            }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {

            Text(
                text = "Review Jobs Done",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Divider(color = Color.Black, thickness = 1.dp)
            if(loading){
                CircularProgressIndicator(
                    color = Color.Blue,
                    modifier = Modifier
                        .align(
                            Alignment.CenterHorizontally
                        )
                )
            }else {
                LazyColumn(
                    modifier = Modifier
                        .padding(15.dp)
                ) {
                    itemsIndexed(jobs) { _, job ->
                        ReviewCard(job) { reviewedJobIds.add(job.jobId) }
                    }
                }
            }

        }
        CircularIconButton(
            icon = Icons.Default.ArrowBack,
            contentDescription = "Close button",
            onClick = { Navigation.navController.navigate("dashboard/$name") },
            modifier = Modifier
                .align(Alignment.TopStart)
                .coloredShadow(
                    color = Color.Black,
                    alpha = 0.3f, borderRadius = 20.dp,
                    shadowRadius = 8.dp,
                    offsetX = 6.dp,
                    offsetY = 4.dp
                )
        )

    }
}

@Composable
fun ReviewCard(job: AssignedJob, onReviewSubmitted: () -> Unit) {
    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(job.date)
    var isDialogVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val db = Firebase.firestore
    var artisan by remember { mutableStateOf("") }

    // Fetch artisan details
    db.collection("assignedJobs")
        .whereEqualTo("artisanId", job.artisanId)
        .get()
        .addOnSuccessListener{documents ->
            if (!documents.isEmpty) {
                val document = documents.documents[0]
                val artisanName = document.getString("artisanName") ?: ""
                artisan = artisanName
            } else {
                Toast.makeText(context, "Artisan not available", Toast.LENGTH_SHORT).show()
            }
        }
        .addOnFailureListener { exception ->
            Toast.makeText(context, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
        }

    fun showDialog() {
        isDialogVisible = true
    }

    fun onSubmitReview() {
        showDialog()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(),
        colors = CardDefaults.cardColors()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = artisan,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = formattedDate,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = job.jobDescription,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onSubmitReview() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = "Review", color = Color.White)
            }
            if (isDialogVisible) {
                ReviewPopup(
                    onDismiss = {
                        isDialogVisible = false
                        // Invoke the callback when review is submitted
                        onReviewSubmitted()
                    },
                    job,
                    job.clientName
                )
            }
        }
    }
}


@Composable
fun ReviewPopup(onDismiss: () -> Unit, job: AssignedJob, clientName: String) {
    var reviewDescription by remember { mutableStateOf("") }
    val context  = LocalContext.current
    var loading by remember{ mutableStateOf<Boolean>(false) }
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            color = Color.White,
            modifier = Modifier
                .width(300.dp)
                .height(250.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(8.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Review the Job")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = reviewDescription,
                    onValueChange = { reviewDescription = it },
                    singleLine = false,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black)
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = { onDismiss() }) {
                        Text(text = "Dismiss")
                    }
                    Button(onClick = {
                        Log.d(TAG, "Client Name: $clientName")
                        loading = true
                        reviewJob(job,  clientName, reviewDescription){
                                success ,errorMessage->
                            if(success){
                                loading = false
                                Toast.makeText(context, "Successfully reviewed the job", Toast.LENGTH_LONG).show()
                            }else{
                                loading = false
                                Toast.makeText(context, "Failed to review $errorMessage", Toast.LENGTH_LONG).show()
                            }
                        } }) {
                        if(loading){
                            CircularProgressIndicator(color = Color.White)
                        }else{
                            Text(text = "Submit")
                        }
                    }
                }
            }
        }
    }
}

fun reviewJob(job: AssignedJob, clientName: String, reviewDescription: String, onComplete: (Boolean, String?) -> Unit){
    val db = Firebase.firestore
    val auth = Firebase.auth
    val clientId = auth.currentUser?.uid ?: ""
    if (job.artisanId != null) {
        val review = Review(
            clientId = clientId,
            artisanId = job.artisanId!!, // Use !! operator to assert non-nullability
            clientName = clientName,
            content = reviewDescription,
            job = job.jobDescription
        )

        db.collection("reviews")
            .document("Reviewed_by_${clientName}")
            .set(review)
            .addOnSuccessListener {
                onComplete(true, null)
            }.addOnFailureListener { e ->
                // Failed to add to pendingReview collection
                // Handle error
                onComplete(false, e.message)
            }
    } else {
        onComplete(false, "Artisan ID is null")
    }
}
