package com.learn.splashlearn


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun Splash(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp, 70.dp)
    ){

        Column {
            Text(text = "QUICK", fontWeight = FontWeight.Bold, fontSize = 40.sp)
            Text(text = "FIX", fontWeight = FontWeight.Bold, fontSize = 40.sp)
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(
                space = 15.dp,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            OutlinedButton(onClick = { navController.navigate("Main_screen") },
                modifier = Modifier.size(150.dp, 40.dp)
                ) {
                Text("Login", color = Color.Black)
            }
            OutlinedButton(onClick = { navController.navigate("Main_screen") },
                modifier = Modifier.size(150.dp, 40.dp)) {
                Text("Register", color = Color.Black)
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
private fun SplashPrev() {

}