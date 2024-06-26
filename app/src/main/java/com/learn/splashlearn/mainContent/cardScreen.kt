package com.learn.splashlearn.mainContent

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.learn.splashlearn.Artisan
import com.learn.splashlearn.R
import android.Manifest
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.learn.splashlearn.Navigation
import com.learn.splashlearn.User
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.material3.Surface
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.learn.splashlearn.AssignedJob
import com.learn.splashlearn.Review
import java.text.SimpleDateFormat
import java.time.format.TextStyle
import java.util.*


const val REQUEST_CALL_PERMISSION_CODE = 1001 // You can use any integer value here

@Composable
fun CardReviewScreen(artisan: Artisan?, user: User?) {
    val db = Firebase.firestore
    var reviews by remember { mutableStateOf<List<Review>>(emptyList()) }
    artisan ?: return
    val auth = Firebase.auth
    val id = auth.currentUser?.uid ?: ""
    val context = LocalContext.current
    var isClient by remember { mutableStateOf<Boolean?>(null) }
    var isDialogVisible by remember { mutableStateOf(false) }
    val currentUserId = auth.currentUser?.uid ?: ""
    var currentUserName by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }


    // Check if the current user is an artisan
    db.collection("artisans").document(currentUserId)
        .get()
        .addOnSuccessListener { document ->
            if (document.exists()) {
                // User is an artisan, retrieve the name from the artisans collection
                val artisanName = document.getString("name") ?: ""
                currentUserName = artisanName
            } else {
                // User is not an artisan, retrieve the name from the clients collection
                db.collection("clients").document(currentUserId)
                    .get()
                    .addOnSuccessListener { clientDocument ->
                        val clientName = clientDocument.getString("name") ?: ""
                        currentUserName = clientName
                    }
                    .addOnFailureListener { e ->
                        // Handle failure to retrieve client's name
                        Log.e(TAG, "Failed to retrieve client's name: ${e.message}")
                    }
            }
        }
        .addOnFailureListener { e ->
            // Handle failure to retrieve artisan's name
            Log.e(TAG, "Failed to retrieve artisan's name: ${e.message}")
        }
    fun showDialog() {
        isDialogVisible = true
    }
    fun onclick() {
        showDialog()
    }
    LaunchedEffect(Unit) {
        isClient = determineUserRole() == "Client"
    }
    Column(modifier = Modifier.fillMaxSize()) {
        // Display the artisan card at the top
        ArtisanCardForReview(artisan = artisan)
        if(isClient ?: false){
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Call button
                Button(
                    onClick = {
                        handleCallAction(context, artisan.phoneNumber)
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .width(150.dp)
                        .height(40.dp)
                        .coloredShadow(
                            color = Color.Black,
                            alpha = 0.3f,
                            borderRadius = 15.dp,
                            shadowRadius = 8.dp,
                            offsetY = 5.dp,
                            offsetX = 5.dp
                        )
                ) {
                    Text(text = "Call ${artisan.name}", color = Color.White)
                }

                // Call button
                Button(
                    onClick = {
                        onclick()
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .width(150.dp)
                        .height(40.dp)
                        .coloredShadow(
                            color = Color.Black,
                            alpha = 0.3f,
                            borderRadius = 15.dp,
                            shadowRadius = 8.dp,
                            offsetY = 5.dp,
                            offsetX = 5.dp
                        )
                ) {
                    Text(text = "Assign Job", color = Color.White)
                }
                if (isDialogVisible) {
                    JobAssignmentPopup(onDismiss = { isDialogVisible = false }, artisan, user)
                }
            }
        }
        Divider(color = Color.Black, thickness = 1.dp)
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
                        val job = document.getString("job") ?: ""
                        Review("", artisanId, clientName, date, content, job)
                    }
                    reviews = newReviews.filter { it.artisanId == artisan.uid }
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
                        Alignment.CenterHorizontally
                    )
            )
        } else {
            // Show LazyColumn with data
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .padding(15.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(reviews) { review ->
                    CardReview(review)
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        CircularIconButton(
            icon = Icons.Default.ArrowBack,
            contentDescription = "Close button",
            onClick = { Navigation.navController.navigate("dashboard/${currentUserName}")
            },
            modifier = Modifier
                .coloredShadow(
                    color = Color.Black,
                    alpha = 0.3f, borderRadius = 20.dp,
                    shadowRadius = 8.dp,
                    offsetX = 6.dp,
                    offsetY = 4.dp
                )
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 20.dp)
        )
    }

}




@Composable
fun CardReview(review: Review) {
    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(review.date)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(8.dp)
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
                    text = review.clientName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = formattedDate,
                    fontSize = 14.sp

                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Job done:",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = review.job,
                modifier = Modifier.padding(8.dp),
                fontSize = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Review from client:",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = review.content,
                modifier = Modifier.padding(8.dp),
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}


fun assignJob(user: User?, artisan: Artisan, jobDescription: String, onComplete: (Boolean, String?) -> Unit) {
    val db = Firebase.firestore
    val auth = Firebase.auth

    // Get current user ID (client ID)
    val clientId = auth.currentUser?.uid ?: ""

    // Retrieve client's name from Firestore using client ID
    db.collection("clients").document(clientId)
        .get()
        .addOnSuccessListener { clientDocument ->
            val clientName = clientDocument.getString("name") ?: ""

            // Create AssignedJob object with client's name
            val assignedJob = AssignedJob(
                clientId = clientId,
                artisanId  = artisan.uid,
                clientName = clientName,
                artisanName = artisan.name,
                mobileNumber = artisan.phoneNumber,
                jobDescription = jobDescription,
                jobId = ""
            )

            // Add to assignedJobs collection with artisan's ID as document name
            db.collection("assignedJobs")
                .document("${artisan.name}_assigned_job_by_${clientName}")
                .set(assignedJob)
                .addOnSuccessListener {
                    // Add to pendingReview collection
                    db.collection("pendingReview")
                        .document("${clientName}_assigned_job_${artisan.name} ")
                        .set(assignedJob)
                        .addOnSuccessListener {
                            // Successfully added to both collections
                            // You can show a toast or perform any UI update here
                            onComplete(true, null)
                        }
                        .addOnFailureListener { e ->
                            // Failed to add to pendingReview collection
                            // Handle error
                            onComplete(false, e.message)
                        }
                }
                .addOnFailureListener { e ->
                    // Failed to add to assignedJobs collection
                    // Handle error
                    onComplete(false, e.message)
                }
        }
        .addOnFailureListener { e ->
            // Failed to retrieve client's name
            // Handle error
            onComplete(false, e.message)
        }
}
@Composable
fun ArtisanCardForReview(artisan: Artisan, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .fillMaxWidth()
            .height(110.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.BottomStart)
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.hannahnelson), // Placeholder image
                contentDescription = null,
                modifier = Modifier
                    .coloredShadow(
                        color = Color.Black,
                        alpha = 0.8f,
                        borderRadius = 30.dp,
                        shadowRadius = 8.dp,
                        offsetY = 6.dp,
                        offsetX = 3.dp
                    )
                    .clip(CircleShape)
                    .width(60.dp)
                    .height(60.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .coloredShadow(
                        color = Color.Black,
                        alpha = 0.8f,
                        shadowRadius = 8.dp,
                        offsetY = 6.dp,
                        offsetX = 3.dp
                    )
                    .clip(RoundedCornerShape(10.dp))
                    .width(550.dp)
                    .height(60.dp)
                    .background(Color.White)
                    .padding(10.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = artisan.name,
                        fontWeight = FontWeight.Bold
                    )
                    Text(artisan.phoneNumber)
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = artisan.workName)

                    Text(artisan.numberOfReviews)
                }
            }
        }
    }
}
@Composable
fun JobAssignmentPopup(onDismiss: () -> Unit, artisan: Artisan, user: User?) {
    var jobDescription by remember { mutableStateOf("") }
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
                Text(text = "Describe the job to be assigned")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = jobDescription,
                    onValueChange = { jobDescription = it },
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
                        loading = true
                        assignJob(user, artisan, jobDescription){
                            success ,errorMessage->
                            if(success){
                                loading = false
                                Toast.makeText(context, "Successfully assigned job to ${artisan.name}", Toast.LENGTH_LONG).show()
                            }else{
                                loading = false
                                Toast.makeText(context, "Failed to assign due to $errorMessage", Toast.LENGTH_LONG).show()
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
fun handleCallAction(context: Context, phoneNumber: String?) {

    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        phoneNumber?.let { number ->
            val callIntent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$number")
            }
            context.startActivity(callIntent)
        }
    } else {
        // Permission not granted, request it
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.CALL_PHONE),
            REQUEST_CALL_PERMISSION_CODE
        )
        phoneNumber?.let { number ->
            val callIntent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$number")
            }
            context.startActivity(callIntent)
        }
    }
}





