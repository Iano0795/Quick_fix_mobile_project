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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.learn.splashlearn.AssignedJob
import com.learn.splashlearn.Navigation
import com.learn.splashlearn.R
import com.learn.splashlearn.Review
import com.learn.splashlearn.User
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun JobsGiven(user: User?) {
    val context = LocalContext.current
    val db = Firebase.firestore
    var reviews by remember { mutableStateOf<List<Review>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }
    val name = user?.name ?: ""
    val auth = Firebase.auth
    val clientId = auth.currentUser?.uid ?: ""

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
                text = "Jobs Given",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            LaunchedEffect(Unit) {
                loading = true
                val firestore = FirebaseFirestore.getInstance()
                firestore.collection("reviews")
                    .get()
                    .addOnSuccessListener { documents ->
                        val newReviews = documents.map { document ->
                            val artisanName = document.getString("artisanName") ?: ""
                            val artisanId = document.getString("artisanId") ?: ""
                            val clientId = document.getString("clientId") ?: ""
                            val timestamp = document.getTimestamp("date")
                            val date = timestamp?.toDate() ?: Date()
                            val content = document.getString("content")?.toString() ?: ""
                            val job = document.getString("job") ?: ""
                            Review(clientId, artisanId, "", date, content, job)
                        }
                        reviews = newReviews.filter { it.clientId == clientId }
                        loading = false
                    }
                    .addOnFailureListener { exception ->
                        // Handle failure
                        Toast.makeText(context, exception.message, Toast.LENGTH_LONG).show()
                        loading = false
                    }
            }
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
                    itemsIndexed(reviews) { _, review ->
                        JobCard(review)
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
fun JobCard(review: Review) {
    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(review.date)
    val context = LocalContext.current
    val db = Firebase.firestore
    var artisan by remember { mutableStateOf("") }
    db.collection("assignedJobs")
        .whereEqualTo("clientId", review.clientId)
        .get()
        .addOnSuccessListener{documents ->
            if (!documents.isEmpty) {
                val document = documents.documents[0]
                val artisanName = document.getString("artisanName") ?: ""
                artisan = artisanName
                // Now you have the artisanName, you can use it as needed
            } else {
                // Handle the case where no document matches the artisanId
                Log.d(TAG, "JobCard: ${review.artisanId}")
                Toast.makeText(context, "Artisan not available", Toast.LENGTH_SHORT).show()
            }
        }
        .addOnFailureListener { exception ->
            // Handle failure
            Toast.makeText(context, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
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
                    text = formattedDate ,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 50.dp, max = 550.dp)
            ) {
                Text(
                    text = review.job,
                    modifier = Modifier.padding(8.dp),
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 50.dp, max = 550.dp)
            ) {
                Text(
                    text = review.content,
                    modifier = Modifier.padding(8.dp),
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
        }
    }
}