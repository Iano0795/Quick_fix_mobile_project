package com.learn.splashlearn.mainContent

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.learn.splashlearn.Navigation
import com.learn.splashlearn.Review
import com.learn.splashlearn.User
import java.util.Date

@Composable
fun Reviews(user: User?) {
    var loading by remember { mutableStateOf(false) }
    val name = user?.name ?: ""
    var reviews by remember { mutableStateOf<List<Review>>(emptyList()) }
    val auth = Firebase.auth
    val id = auth.currentUser?.uid ?: ""
    val context = LocalContext.current
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
                text = "Your Reviews",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
        LaunchedEffect(Unit) {
            loading = true
            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("reviews")
                .get()
                .addOnSuccessListener { documents ->
                    val newReviews = documents.map { document ->
                        val clientName = document.getString("clientName") ?: ""
                        val artisanId = document.getString("artisanId") ?: ""
                        val timestamp = document.getTimestamp("date")
                        val date = timestamp?.toDate() ?: Date()
                        val content = document.getString("content")?.toString() ?: ""
                        val job  = document.getString("job") ?: ""
                        Review("", artisanId, clientName, date, content, job)
                    }
                    reviews = newReviews.filter { it.artisanId == id }
                    loading = false
                }
                .addOnFailureListener { exception ->
                    // Handle failure
                    Toast.makeText(context, exception.message, Toast.LENGTH_LONG).show()
                    loading = false
                }
        }
        if (loading) {
            // Show loading indicator
            CircularProgressIndicator(
                color = Color.Blue,
                modifier = Modifier
                    .align(
                        Alignment.Center
                    )
            )
        } else {
            // Show LazyColumn with data
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(reviews) { review ->
                    CardReview(review)
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