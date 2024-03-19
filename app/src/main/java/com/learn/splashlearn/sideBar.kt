//package com.learn.splashlearn
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Box
//import androidx.compose.ui.Alignment
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ExitToApp
//import androidx.compose.material.icons.filled.Notifications
//import androidx.compose.material.icons.filled.Star
//import androidx.compose.material3.Divider
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//
//@Composable
//fun Sidebar(onClose: () -> Unit) {
//    Column(
//        modifier = Modifier
//            .size(250.dp)
//            .fillMaxHeight()
//            .padding(16.dp)
//            .background(Color.White)
//    ) {
//        ProfileSection()
//        Divider(modifier = Modifier.padding(vertical = 16.dp))
//        MenuItem(icon = Icons.Default.Notifications, text = "Assigned Jobs")
//        MenuItem(icon = Icons.Default.Star, text = "Reviews")
//        Spacer(modifier = Modifier.weight(1f))
//        MenuItem(icon = Icons.Default.ExitToApp, text = "Logout")
//    }
//    CloseButton(onClose)
//}
//
//
//@Composable
//fun ProfileSection() {
//    Box(
//        modifier = Modifier.padding(bottom = 16.dp)
//    ) {
//        Image(
//            painter = painterResource(id = R.drawable.img_1),
//            contentDescription = "Profile Photo",
//            modifier = Modifier.size(80.dp).clip(CircleShape)
//        )
//        Column(
//            modifier = Modifier
//                .padding(start = 16.dp)
//                .align(Alignment.CenterStart)
//        ) {
//            Text(text = "John", fontWeight = FontWeight.Bold)
//            Text(text = "View Profile")
//        }
//    }
//}
//
//@Composable
//fun MenuItem(icon: ImageVector, text: String) {
//    Row(
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Image(
//            imageVector = icon,
//            contentDescription = null,
//            modifier = Modifier.size(24.dp)
//        )
//        Spacer(modifier = Modifier.width(16.dp))
//        Text(text = text)
//    }
//}
//
//@Composable
//fun CloseButton(onClose: () -> Unit) {
//    Box(
//        modifier = Modifier.padding(16.dp).clickable { onClose() }
//    ) {
//        Image(
//            painter = painterResource(id = R.drawable.baseline_close_24),
//            contentDescription = "Close",
//            modifier = Modifier.align(Alignment.TopEnd).size(24.dp).clickable { onClose() }
//        )
//    }
//}