package com.learn.splashlearn.mainContent

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.learn.splashlearn.Artisan
import com.learn.splashlearn.AssignedJob
import com.learn.splashlearn.Navigation
import com.learn.splashlearn.R
import com.learn.splashlearn.User
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@Composable
fun AssignedJobs(user: User?) {
    val db = Firebase.firestore
    val auth = Firebase.auth
    val name = user?.name ?: ""
    var jobs by remember { mutableStateOf<List<AssignedJob>>(emptyList()) }
    val context = LocalContext.current
    var loading by remember { mutableStateOf(false) }
    val artisanId = auth.currentUser?.uid ?: ""
    LaunchedEffect(Unit) {
        loading = true
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("assignedJobs")
            .whereEqualTo("artisanId", artisanId)
            .get()
            .addOnSuccessListener { documents ->
                val newJobs = documents.map { document ->
                    val artId = document.getString("artisanId")
                    val clientName = document.getString("clientName") ?: ""
                    val timestamp = document.getTimestamp("date")
                    val date = timestamp?.toDate() ?: Date()
                    val jobDescription = document.getString("jobDescription") ?: ""
                    AssignedJob("", artisanId, clientName, name, "", jobDescription, date, "")
                }
                jobs = newJobs
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
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {

            Text(
                text = "Assigned Jobs",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 20.dp)
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
                        AssignedJobCard(job)
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
fun CircularIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(30.dp)
            .background(color = Color.LightGray, shape = CircleShape)
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .size(15.dp)
                .align(Alignment.Center)// Adjust the size of the icon as needed
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription
            )
        }
    }
}

@Composable
fun AssignedJobCard(job: AssignedJob) {
    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(job.date)
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
                    text = job.clientName,
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
                    text = job.jobDescription,
                    modifier = Modifier.padding(8.dp),
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
        }
    }
}
