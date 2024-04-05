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
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.material3.Surface
import androidx.compose.ui.window.Dialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*


const val REQUEST_CALL_PERMISSION_CODE = 1001 // You can use any integer value here

@Composable
fun CardReviewScreen(artisan: Artisan?, user: User?) {
    val name = user?.name ?: ""
    artisan ?: return
    val context = LocalContext.current
    var isClient by remember { mutableStateOf<Boolean?>(null) }
    var isDialogVisible by remember { mutableStateOf(false) }
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
                    JobAssignmentPopup(onDismiss = { isDialogVisible = false }, artisan)
                }
            }
        }
        Divider(color = Color.Black, thickness = 1.dp)
        Spacer(modifier = Modifier.weight(1f))
        CircularIconButton(
            icon = Icons.Default.ArrowBack,
            contentDescription = "Close button",
            onClick = { Navigation.navController.navigate("dashboard/$name") },
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




data class AssignedJob(
    val clientId: String,
    val name: String,
    val mobileNumber: String,
    val jobDescription: String,
    val date: Date = Calendar.getInstance().time
)

fun assignJob(artisan: Artisan, jobDescription: String) {
    val db = Firebase.firestore
    val auth = Firebase.auth

    // Get current user ID (client ID)
    val clientId = auth.currentUser?.uid ?: ""

    // Create AssignedJob object
    val assignedJob = AssignedJob(
        clientId = clientId,
        name = artisan.name,
        mobileNumber = artisan.phoneNumber,
        jobDescription = jobDescription
    )

    // Add to assignedJobs collection with artisan's ID as document name
    db.collection("assignedJobs")
        .document(artisan.uid) // Using artisan's ID as the document name
        .set(assignedJob)
        .addOnSuccessListener {
            // Add to pendingReview collection
            db.collection("pendingReview")
                .document(clientId)
                .set(assignedJob)
                .addOnSuccessListener {
                    // Successfully added to both collections
                    // You can show a toast or perform any UI update here
                }
                .addOnFailureListener { e ->
                    // Failed to add to pendingReview collection
                    // Handle error
                }
        }
        .addOnFailureListener { e ->
            // Failed to add to assignedJobs collection
            // Handle error
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
fun JobAssignmentPopup(onDismiss: () -> Unit, artisan: Artisan) {
    var jobDescription by remember { mutableStateOf("") }
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            color = Color.White,
            modifier = Modifier
                .width(300.dp)
                .height(250.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Describe the job to be assigned")
                BasicTextField(
                    value = jobDescription,
                    onValueChange = { jobDescription = it },
                    singleLine = false,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = { onDismiss() }) {
                        Text(text = "Dismiss")
                    }
                    Button(onClick = { assignJob(artisan = artisan, jobDescription) }) {
                        Text(text = "Submit")
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





