package com.learn.splashlearn.login

import android.widget.Space
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learn.splashlearn.Navigation

@Composable
fun LoginAsWho() {
    val navController = Navigation.navController
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
                modifier = Modifier.size(150.dp, 40.dp)
            ) {
                Text("Client", color = Color.Black)
            }
            OutlinedButton(onClick = { navController.navigate("artisanLogin_screen") },
                modifier = Modifier.size(150.dp, 40.dp)) {
                Text("Artisan", color = Color.Black)
            }
        }
    }
}