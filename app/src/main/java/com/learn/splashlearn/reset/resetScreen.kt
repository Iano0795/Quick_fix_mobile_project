package com.learn.splashlearn.reset

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.learn.splashlearn.Navigation
import com.learn.splashlearn.Navigation.navController
import kotlinx.coroutines.tasks.await

@Composable
fun ResetPasswordScreen() {
    val navController = Navigation.navController
    var email by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Reset")
                }
                append(" Password")
            },
            fontSize = 40.sp,
            textAlign = TextAlign.Left,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Please enter your email",
            fontSize = 20.sp,
            textAlign = TextAlign.Left,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(56.dp),
            placeholder = { Text("Your Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            )
        )

        Button(
            onClick = { 
                      FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                          .addOnCompleteListener{task ->
                              if(task.isSuccessful){
                                  Toast.makeText(
                                      context,
                                      "Email sent successfully!, Check your email",
                                      Toast.LENGTH_LONG
                                  ).show()
                              }else{
                                  Toast.makeText(
                                      context,
                                      task.exception?.message,
                                      Toast.LENGTH_LONG
                                  ).show()
                              }
                          }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Reset Password")
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                Navigation.navController.navigate("loginAs_screen")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Back To Login")
        }
    }
}





