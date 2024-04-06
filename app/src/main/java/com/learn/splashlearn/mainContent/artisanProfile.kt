package com.learn.splashlearn.mainContent

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.learn.splashlearn.Artisan
import com.learn.splashlearn.Navigation
import com.learn.splashlearn.Navigation.navController
import com.learn.splashlearn.R
import com.learn.splashlearn.User
import com.learn.splashlearn.login.clisInternetConnected

@Composable
fun artisanProfileScreen(user: User?) {
    val email = remember { mutableStateOf(TextFieldValue()) }
    var loading by remember { mutableStateOf(false) }
    val name = user?.name ?: ""
    val mobileNo = remember { mutableStateOf(TextFieldValue()) }
    val showDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current
    var pdfUri by remember { mutableStateOf<Uri?>(null) }
    var uploading by remember { mutableStateOf(false) }
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
    var isPasswordDialogVisible by remember { mutableStateOf(false) }
    fun showPasswordDialog() {
        isPasswordDialogVisible = true
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
                        .clickable {
                            if(clisInternetConnected(context)){
                                navController.navigate("dashboard/$name")
                            }else{
                                Toast.makeText(
                                    context,
                                    "Please check your internet connection and try again.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(end = 16.dp, bottom = 16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_1),
                        contentDescription = "Profile Photo",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .align(Alignment.Center)
                    )
                    Card(
                        modifier = Modifier
                            .size(35.dp)
                            .align(Alignment.BottomEnd),
                        border = BorderStroke(2.dp, Color.White),
                        shape = CircleShape
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.camera),
                            contentDescription = "Camera Icon",
                            modifier = Modifier
                                .wrapContentSize(Alignment.Center)
                                .align(Alignment.CenterHorizontally)
                                .size(33.dp)
                                .padding(8.dp),
                            tint = Color.White
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "$name",
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
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
            Spacer(modifier = Modifier.height(15.dp))
            Button(
                onClick = { showDialog.value = true },
                modifier = Modifier
                    .padding(16.dp)
                    .height(50.dp)
                    .width(400.dp)
                    .padding(horizontal = 16.dp),
                shape = RectangleShape
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Icon(painter = painterResource(id = R.drawable.baseline_upload_24), contentDescription = "Upload icon")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Upload a certificate")
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            fun artOnclickUpdate() {
                showPasswordDialog()
            }
            Button(
                modifier = Modifier
                    .size(100.dp, 50.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .align(Alignment.CenterHorizontally),
                onClick = { artOnclickUpdate() }) {
                    Text(text = "Update")
            }
            if (isPasswordDialogVisible) {
                PasswordDialog(
                    context, name, email = email.value.text, mobileNo = mobileNo.value.text,
                    onDismiss = { isPasswordDialogVisible = false },
                )
            }
            // Popup Dialog
            if (showDialog.value) {
                Dialog(onDismissRequest = { showDialog.value = false }) {
                    Surface(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(10.dp).size(300.dp, 250.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(5.dp)
                                .fillMaxSize()
                                .background(Color.LightGray, shape = RoundedCornerShape(8.dp)),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            IconButton(
                                onClick = { showDialog.value = false },
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                            ) {
                                Icon(painter = painterResource(id = R.drawable.baseline_close_24), contentDescription = "Close")
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            OutlinedButton(
                                onClick = {
                                    uploading = true
                                    launcher.launch(createFilePickerIntent()) },
                                modifier = Modifier
                                    .padding(16.dp)
                                    .size(180.dp, 45.dp)
                            ) {
                                if (uploading) {
                                    Text(text = "Uploading...", color = Color.Black, fontSize = 15.sp)
                                } else {
                                    Text(text = "Choose to Upload", color = Color.Black, fontSize = 15.sp)
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { logout() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.width(100.dp)
                ) {
                    IconButton(
                        onClick = {  },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "logout icon"
                        )
                    }
                    Text(text = "Logout")
                }

            }

            LaunchedEffect(pdfUri) {
                if (pdfUri != null) {
                    uploadPdfToFirebase(context, pdfUri!!, user){
                        success, errorMessage ->
                        if(success){
                            uploading = false
                            Toast.makeText(context, "PDF uploaded successfully", Toast.LENGTH_SHORT).show()
                        }else{
                            uploading = false
                            Toast.makeText(context, "Failed to upload PDF: $errorMessage", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun PasswordDialog(context: Context, name: String, email: String, mobileNo: String, onDismiss: () -> Unit) {
    var password by remember { mutableStateOf(TextFieldValue()) }
    var loading by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .width(300.dp)
            ) {
                Text(text = "Enter Your Password")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            loading = true
                            artisanUpdateDetails(context, name, email = email, mobileNo = mobileNo, password.text)
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        if(loading){
                            CircularProgressIndicator()
                        }else{
                            Text(text = "Submit")
                        }
                    }
                    Button(
                        onClick = { onDismiss() },
                        modifier = Modifier
                            .padding(end = 8.dp)
                    ) {
                        Text(text = "Dismiss")
                    }
                }
            }
        }
    }
}
fun artisanUpdateDetails(context: Context, name: String, email: String, mobileNo: String, password: String) {
    val db = FirebaseFirestore.getInstance()
    val userCollection = db.collection("clients")
    val updatedUser = hashMapOf<String, Any>(
        "mobileNumber" to mobileNo
    )

    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    if (user != null) {
        val credential = EmailAuthProvider.getCredential(user.email!!, password)

        user.reauthenticate(credential)
            .addOnCompleteListener { reAuthResult ->
                if (reAuthResult.isSuccessful) {
                    // Re-authentication successful, now update email address
                    user.updateEmail(email)
                        .addOnCompleteListener { emailUpdateResult ->
                            if (emailUpdateResult.isSuccessful) {
                                // Email updated successfully, now update user details in Firestore
                                userCollection.whereEqualTo("name", name)
                                    .get()
                                    .addOnSuccessListener { documents ->
                                        if (!documents.isEmpty) {
                                            val userId = documents.documents[0].id
                                            userCollection.document(userId)
                                                .update(updatedUser)
                                                .addOnSuccessListener {
                                                    // Update successful
                                                    Toast.makeText(context, "User details updated successfully", Toast.LENGTH_SHORT).show()
                                                }
                                                .addOnFailureListener { e ->
                                                    // Update failed
                                                    Toast.makeText(context, "Failed to update user details: ${e.message}", Toast.LENGTH_SHORT).show()
                                                }
                                        } else {
                                            // User not found
                                            Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        // Query failed
                                        Toast.makeText(context, "Failed to query user: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                // Failed to update email
                                Toast.makeText(context, "Failed to update email: ${emailUpdateResult.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    // Re-authentication failed
                    Toast.makeText(context, "Re-authentication failed: ${reAuthResult.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    } else {
        // User not logged in
        Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
    }
}


private fun createFilePickerIntent(): Intent {
    return Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "application/pdf" // Filter only PDF files
    }
}

// Function to upload PDF file to Firebase Storage
private fun uploadPdfToFirebase(context: Context, pdfUri: Uri, user: User?, onComplete: (Boolean, String?) -> Unit) {
    val storageRef = Firebase.storage.reference
    val name = user?.name ?: ""
    val fileName = "${name}_${System.currentTimeMillis()}.pdf" // Add timestamp to the filename
    val fileRef = storageRef.child("certifications/$fileName") // Adjusted path to certifications folder

    // Upload the file
    fileRef.putFile(pdfUri)
        .addOnSuccessListener { _ ->
            onComplete(true, null)


        }
        .addOnFailureListener { exception ->
            onComplete(false, exception.message)


        }
}
