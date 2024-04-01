package com.learn.splashlearn.mainContent

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.learn.splashlearn.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.learn.splashlearn.Navigation.navController
import com.learn.splashlearn.User


@Composable
fun ProfileScreen(user: User?) {
    val email = remember { mutableStateOf(TextFieldValue()) }
    val name = user?.name ?: ""
    val mobileNo = remember { mutableStateOf(TextFieldValue()) }
    val password = remember { mutableStateOf(TextFieldValue()) }
    val showDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current
    var pdfUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val uri: Uri? = data?.data
            if (uri != null) {
                pdfUri = uri
            } else {
                Toast.makeText(context, "Failed to retrieve PDF", Toast.LENGTH_SHORT).show()
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f)
                .padding(20.dp)
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ){
                Icon(
                    painter = painterResource(id = R.drawable.baseline_close_24),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .clickable { navController.navigate("dashboard/$name") }
                )
                Image(
                    painter = painterResource(id = R.drawable.img_1),
                    contentDescription = "Profile Photo",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 16.dp)
                ) {
                    Card(
                        modifier = Modifier.size(40.dp),
                        border = BorderStroke(2.dp, Color.White),
                        shape = CircleShape
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.camera),
                            contentDescription = "Camera Icon",
                            modifier = Modifier
                                .size(24.dp)
                                .padding(8.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "$name",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .weight(0.6f),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "Email:")

            OutlinedTextField(
                value = email.value,
                onValueChange = {email.value = it},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(15.dp)),
                placeholder = { Text(text = "Email") }
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Phonenumber:")

            OutlinedTextField(
                value = mobileNo.value,
                onValueChange = {mobileNo.value = it},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(15.dp)),
                placeholder = { Text(text = "Phonenumber") }
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Password")

            OutlinedTextField(
                value = password.value,
                onValueChange = {password.value = it},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(15.dp)),
                placeholder = { Text(text = "Password") }
            )
            Spacer(modifier = Modifier.height(15.dp))
            Button(
                onClick = { showDialog.value = true },
                modifier = Modifier
                    .padding(16.dp)
                    .height(50.dp)
                    .width(200.dp)
                    .padding(horizontal = 16.dp),
                shape = RectangleShape
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(painter = painterResource(id = R.drawable.baseline_upload_24), contentDescription = "Add Icon")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Upload")
                }
            }

            // Popup Dialog
            if (showDialog.value) {
                Dialog(onDismissRequest = { showDialog.value = false }) {
                    Surface(shape = RectangleShape) {
                        Column(
                            modifier = Modifier
                                .padding(5.dp)
                                .size(400.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            IconButton(
                                onClick = { showDialog.value = false },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Icon(painter = painterResource(id = R.drawable.baseline_close_24), contentDescription = "Close")
                            }

                            Button(
                                onClick = { launcher.launch(createFilePickerIntent()) },
                                modifier = Modifier.padding(16.dp).size(250.dp, 40.dp)
                            ) {
                                Text(text = "Choose to Upload")
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = {  },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Add Icon"
                    )
                }

                Button(
                    onClick = { logout() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Logout")
                }
            }
            LaunchedEffect(pdfUri) {
                if (pdfUri != null) {
                    uploadPdfToFirebase(context, pdfUri!!)
                }
            }
        }
    }
}
// Function to create an intent for the file picker
private fun createFilePickerIntent(): Intent {
    return Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "application/pdf" // Filter only PDF files
    }
}

// Function to upload PDF file to Firebase Storage
private fun uploadPdfToFirebase(context: Context, pdfUri: Uri) {
    val storageRef = Firebase.storage.reference
    val fileName = "example.pdf" // You can define your own file name logic here
    val fileRef = storageRef.child("pdfs/$fileName")


    fileRef.putFile(pdfUri)
        .addOnSuccessListener { _ ->
            Toast.makeText(context, "PDF uploaded successfully", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener { exception ->
            Toast.makeText(context, "Failed to upload PDF: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
}