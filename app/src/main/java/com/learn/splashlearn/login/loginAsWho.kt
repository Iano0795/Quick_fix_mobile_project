package com.learn.splashlearn.login

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learn.splashlearn.Navigation
import com.learn.splashlearn.R

@Composable
fun LoginAsWho() {
    val navController = Navigation.navController
    Image(
        painter = painterResource(id = R.drawable.plumber), contentDescription = null,
        modifier = Modifier
            .fillMaxSize(),
        contentScale = ContentScale.Crop
    )
    Box(
        modifier = Modifier.background(Color.White.copy(alpha = 0.8f)).fillMaxSize()
    ){

    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp, 70.dp)
    ){

        Text(text = "Login As Who?",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { navController.navigate("home_screen") }
            )
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(
                space = 15.dp,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            OutlinedButton(onClick = { navController.navigate("clientLogin_screen") },
                modifier = Modifier.size(250.dp, 60.dp)
            ) {
                Text("Client", color = Color.Black, fontSize = 20.sp)
            }
            OutlinedButton(onClick = { navController.navigate("artisanLogin_screen") },
                modifier = Modifier.size(250.dp, 60.dp)) {
                Text("Artisan", color = Color.Black, fontSize = 20.sp)
            }
        }
    }
}