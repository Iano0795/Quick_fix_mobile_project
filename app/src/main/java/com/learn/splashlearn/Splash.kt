package com.learn.splashlearn


import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.learn.splashlearn.login.clisInternetConnected

@Composable
fun Splash() {
    val navController = Navigation.navController
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()

    ){
        Image(
            painter = painterResource(id = R.drawable.splash), contentDescription = null,
            modifier = Modifier
                .matchParentSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFADD8E6).copy(alpha = 0.8f))
        )
        {
            Column(
                modifier = Modifier.padding(start = 40.dp, top = 70.dp)
            ){
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
                OutlinedButton(onClick = {
                    if(clisInternetConnected(context)){
                        Navigation.navController.navigate("loginAs_screen")
                    }else{
                        Toast.makeText(
                            context,
                            "Please check your internet connection and try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } },
                    modifier = Modifier.size(250.dp, 60.dp)) {
                    Text("Login", color = Color.Black, fontSize = 20.sp)
                }
                OutlinedButton(onClick = { navController.navigate("regAs_screen") },
                    modifier = Modifier.size(250.dp, 60.dp)) {
                    Text("Register", color = Color.Black, fontSize = 20.sp)
                }
            }
        }

    }
}
@Preview(showBackground = true)
@Composable
private fun SplashPrev() {

}