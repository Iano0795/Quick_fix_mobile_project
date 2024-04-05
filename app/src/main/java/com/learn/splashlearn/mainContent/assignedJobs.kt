package com.learn.splashlearn.mainContent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learn.splashlearn.Navigation
import com.learn.splashlearn.R
import com.learn.splashlearn.User

@Composable
fun AssignedJobs(user: User?) {
    val name = user?.name ?: ""
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
                text = "Assigned Jobs",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
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