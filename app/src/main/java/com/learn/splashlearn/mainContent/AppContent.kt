package com.learn.splashlearn.mainContent

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
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
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.firestore.FirebaseFirestore
import com.learn.splashlearn.Navigation
import com.learn.splashlearn.Navigation.navController
import com.learn.splashlearn.R
import com.learn.splashlearn.User
import com.learn.splashlearn.Artisan
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.time.format.TextStyle


@Composable
fun MainContent(user: User?) {
    val name = user?.name ?: ""
    val navController = Navigation.navController
    var loading by remember { mutableStateOf(false) }
    var artisans by remember { mutableStateOf<List<Artisan>>(emptyList()) }
    var sidebarVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var selectedFilter by remember { mutableStateOf<String?>(null) }


    val filteredArtisans = if (selectedFilter != null) {
        artisans.filter { it.workName == selectedFilter }
    } else {
        artisans
    }
    var isClient by remember { mutableStateOf<Boolean?>(null) }
    LaunchedEffect(Unit) {
        isClient = determineUserRole() == "Client"
    }
    Box() {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = "Background",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(horizontal = 17.dp, vertical = 17.dp)
            ) {
                IconButton(
                    onClick = { sidebarVisible = !sidebarVisible },
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_menu_24),
                        contentDescription = null,
                        modifier = Modifier.matchParentSize()
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier.align(Alignment.BottomStart)
                ) {
                    Text(text = "Hi", fontWeight = FontWeight.Bold, fontSize = 30.sp)
                    Text(text = "${user?.name}!", fontSize = 30.sp)
                }
            }
            Divider(color = Color.Black, thickness = 1.dp)
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp, 4.dp)
            ) {
                FilterButton(
                    text = "All",
                    isSelected = selectedFilter == null,
                    onClick = { selectedFilter = null }
                )
                FilterButton(
                    text = "Plumber",
                    isSelected = selectedFilter == "Plumber",
                    onClick = { selectedFilter = "Plumber" }
                )
                FilterButton(
                    text = "Tailor",
                    isSelected = selectedFilter == "Tailor",
                    onClick = { selectedFilter = "Tailor" }
                )
                FilterButton(
                    text = "Carpenter",
                    isSelected = selectedFilter == "Carpenter",
                    onClick = { selectedFilter = "Carpenter" }
                )
            }
            Divider(color = Color.Black, thickness = 1.dp)
            LaunchedEffect(Unit) {
                loading = true
                val firestore = FirebaseFirestore.getInstance()
                firestore.collection("artisans")
                    .get()
                    .addOnSuccessListener { documents ->
                        val newArtisans = documents.map { document ->
                            val uid = document.id
                            val name = document.getString("name") ?: ""
                            val phoneNumber = document.getString("mobileNumber") ?: ""
                            val workName = document.getString("Work") ?: ""
                            val numberOfReviews = document.getString("numberOfReviews")?.toString() ?: ""
                            Artisan(uid, name, phoneNumber, workName, numberOfReviews)
                        }
                        artisans = newArtisans.filter { it.name != name }
                        loading = false
                    }
                    .addOnFailureListener { exception ->
                        // Handle failure
                        Toast.makeText(context, exception.message, Toast.LENGTH_LONG).show()
                        loading = false
                    }
            }
            // Function to navigate to the review screen
            fun navigateToReviewScreen(navController: NavController, artisan: Artisan) {
                navController.navigate("review_screen/${artisan.name}")
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
                        .height(1000.dp)
                        .padding(15.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(filteredArtisans) { artisan ->
                        ArtisanCard(artisan = artisan) {
                            // Handle click on the artisan card
                            navigateToReviewScreen(navController, artisan)
                        }
                    }
                }
            }
        }
    }
    if(sidebarVisible && isClient != null){
        Sidebar(navController = navController, name = name, isClient = isClient ?: false, onClose = { sidebarVisible = false })
    }

}


@Composable
fun FilterButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) {
        Color.Blue // Change the background color if selected
    } else {
        Color.LightGray
    }

    Box(
        modifier = Modifier
            .border(1.dp, Color.Black, shape = RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .background(backgroundColor, shape = RoundedCornerShape(10.dp))
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else Color.Black,
            textAlign = TextAlign.Center,
        )
    }
}


@Composable
fun ArtisanCard(artisan: Artisan, modifier: Modifier = Modifier, onArtisanClicked: (Artisan) -> Unit) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .fillMaxWidth()
            .height(85.dp)
            .clickable { onArtisanClicked(artisan) }
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
fun Sidebar(navController: NavController, name: String?, isClient: Boolean, onClose: () -> Unit) {
    Box (
        modifier = Modifier
            .fillMaxHeight()
            .width(250.dp)
    ){
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(250.dp)
                .background(Color.White)
        ) {
            ProfileSection(name = name, isClient = isClient ?: false)
            Divider(color = Color.LightGray, thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))
            MenuItemClickable(icon = Icons.Default.Notifications, text = if (isClient) "Jobs given" else "Assigned Jobs") {
                if (isClient) navController.navigate("jobsgiven_screen/$name") else navController.navigate("assigned_screen/$name")
            }
            MenuItemClickable(icon = Icons.Default.Star, text = if (isClient) "Review" else "Reviews") {
                if (isClient) navController.navigate("reviewjobs_screen/$name") else navController.navigate("yourreviews_screen/$name")
            }
            Spacer(modifier = Modifier.weight(1f))
            Divider(color = Color.LightGray, thickness = 1.dp)
            MenuItemClickable(icon = Icons.Default.ExitToApp, text = "Logout") {
                logout()
            }
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(8.dp)
                .align(Alignment.CenterEnd)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, Color.Black),
                        startX = 16f,
                        endX = 0f
                    )
                )
        )
    }
    CloseButton(onClose)
}
fun logout() {
    val firebaseauth = FirebaseAuth.getInstance()
    firebaseauth.signOut()
    val authStateListener = AuthStateListener{
        if(it.currentUser == null){
            Log.d(TAG, "logout: success")
            navController.navigate("loginAs_screen")
        }
    }
    firebaseauth.addAuthStateListener(authStateListener)
}
@Composable
fun ProfileSection(name: String?, isClient: Boolean) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.img_1),
            contentDescription = "Profile Photo",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .align(Alignment.CenterHorizontally)
                .clickable {  if (isClient) navController.navigate("profile/$name") else navController.navigate("artisanProfile/$name") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "${name}", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Text(text = "View Profile", textAlign = TextAlign.Center, color = Color.Gray)
    }
}
@Composable
fun MenuItem(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text)
    }
}
@Composable
fun MenuItemClickable(icon: ImageVector, text: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text)
    }
}
@Composable
fun CloseButton(onClose: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth( fraction = 0.7f)
    ) {
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.End)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.Gray
            )
        }
    }
}



suspend fun determineUserRole(): String {
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    if (uid.isNullOrEmpty()) {
        // Handle case where user is not logged in or UID is null
        return "Unknown"
    }

    val firestore = FirebaseFirestore.getInstance()

    // Check if the UID exists in the artisans collection
    val artisanDoc = firestore.collection("artisans").document(uid).get().await()
    if (artisanDoc.exists()) {
        return "Artisan"
    }

    // Check if the UID exists in the clients collection
    val clientDoc = firestore.collection("clients").document(uid).get().await()
    if (clientDoc.exists()) {
        return "Client"
    }

    // If the UID does not exist in either collection, return "Unknown"
    return "Unknown"
}






fun Modifier.coloredShadow(
    color: Color,
    alpha: Float = 0.2f,
    borderRadius: Dp = 0.dp,
    shadowRadius: Dp = 20.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp
) = composed {
    val shadowColor = color.copy(alpha = alpha).toArgb()
    val transparent = color.copy(alpha= 0f).toArgb()
    this.drawBehind {
        this.drawIntoCanvas {
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            frameworkPaint.color = transparent
            frameworkPaint.setShadowLayer(
                shadowRadius.toPx(),
                offsetX.toPx(),
                offsetY.toPx(),
                shadowColor
            )
            it.drawRoundRect(
                0f,
                0f,
                this.size.width,
                this.size.height,
                borderRadius.toPx(),
                borderRadius.toPx(),
                paint
            )
        }
    }
}

