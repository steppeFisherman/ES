package com.example.es.utils

import android.content.Context
import android.net.Uri
import com.example.es.R
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.qualifiers.ApplicationContext

interface SavePhotoStorage {

    fun save(uri: Uri?, id: String)

    class Base(@ApplicationContext val context: Context) : SavePhotoStorage {

        override fun save(uri: Uri?, id: String) {
            if (uri != null) {
                val fileReference: StorageReference = REF_STORAGE.child(id)
                fileReference.putFile(uri).addOnSuccessListener { uploadTask ->
                    uploadTask.metadata?.reference?.downloadUrl?.addOnCompleteListener { task ->
                        val image = task.result!!.toString()
                        REF_DATABASE_ROOT.child(NODE_USERS).child(id)
                            .child(CHILD_PHOTO).setValue(image)
                            .addOnSuccessListener {
                                showToast(context, context.getString(R.string.photo_saved))
                            }
                            .addOnFailureListener {
                                showToast(context, context.getString(R.string.couldnt_save_photo))
                            }
                    }
                }
                    .addOnFailureListener {
                        showToast(context, context.getString(R.string.couldnt_save_photo))
                    }
            }
        }
    }
}