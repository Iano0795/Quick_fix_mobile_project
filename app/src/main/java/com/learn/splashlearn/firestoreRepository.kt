package com.learn.splashlearn

// FirestoreRepository.kt

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

suspend fun getArtisanDetailsFromFirestore(artisanName: String?): Artisan? {
    if (artisanName.isNullOrEmpty()) {
        Log.d("Firestore", "Artisan UID is null or empty.")
        return null
    }
    Log.d("Firestore", "Fetching details for artisan with UID: $artisanName")
    return try {
        val querySnapshot = FirebaseFirestore.getInstance().collection("artisans")
            .whereEqualTo("name", artisanName)
            .get()
            .await()

        if (!querySnapshot.isEmpty) {
            val documentSnapshot = querySnapshot.documents.first()
            val uid = documentSnapshot.id
            val name = documentSnapshot.getString("name") ?: ""
            val phoneNumber = documentSnapshot.getString("mobileNumber") ?: ""
            val workName = documentSnapshot.getString("Work") ?: ""
            val numberOfReviews = documentSnapshot.getString("numberOfReviews") ?: ""
            Log.d("Firestore", "Artisan details retrieved successfully.")
            Artisan(uid, name, phoneNumber, workName, numberOfReviews)
        } else {
            Log.d("Firestore", "Artisan document does not exist.")
            null
        }
    } catch (e: Exception) {
        // Handle any errors here, e.g., log or show error message
        Log.e("Firestore", "Error fetching artisan details: ${e.message}")
        null
    }
}



