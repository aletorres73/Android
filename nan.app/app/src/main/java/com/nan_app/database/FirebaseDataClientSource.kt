package com.nan_app.database

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.nan_app.entities.Clients
import kotlinx.coroutines.tasks.await


class FirebaseDataClientSource: ClientSource {

    val db = Firebase.firestore
    val storage = Firebase.storage

    private val collectionName: String = "clients"
    private val collection  = db.collection(collectionName)

    var clientFb: Clients = Clients()
    var currentClient: Clients = Clients()
    var clientListFB = mutableListOf<Clients>()
    var deleteImageName = ""


    override suspend fun loadClientById(id: Int): Boolean {
        val querySnapshot = collection
            .whereEqualTo("id", id)
            .get()
            .await()
        return !querySnapshot.isEmpty
    }

    override suspend fun loadClientByName() {
    }

    override suspend fun loadClientByLastName() {
    }

    override suspend fun loadAllClients() {
        val clientList = mutableListOf<Clients>()
        val querySnapshot = collection
            .get()
            .await()

        if (!querySnapshot.isEmpty) {
            for (document in querySnapshot.documents) {
                val client = document.toObject<Clients>()
                if (client != null) {
                    clientList.add(client)
                } else {
                    throw IllegalStateException("No document found")
                }
                clientListFB = clientList.sortedBy { it.id }.toMutableList()
            }
        } else {
            clientListFB = clientList
        }
    }

    override suspend fun deleteClient(id: Int) {
        try {
            val querySnapshot = collection
                .whereEqualTo("id", id)
                .get()
                .await()
            val documentRef = querySnapshot
                .first()
                .reference
                .delete()
                .await()

        } catch (e: Exception) {
            throw IllegalStateException("Failed to remove product into Firestore: ${e.message}")
        }
    }

    override suspend fun insertClient(newClient: Clients): Boolean {

        val newDocument = hashMapOf(
            "id"            to newClient.id,
            "name"          to newClient.Name,
            "lastName"      to newClient.LastName,
            "birthday"      to newClient.Birthday,
            "email"         to newClient.Email,
            "phone"         to newClient.Phone,
            "state"         to newClient.State,
            "payDay"        to newClient.PayDay,
            "finishDay"      to newClient.FinishDay,
            "amountClass"   to newClient.AmountClass,
            "imageName"     to newClient.ImageName,
            "imageUri"      to newClient.ImageUri )
        return try{
            collection.document()
                .set(newDocument)
            true
        }catch (e: Exception) {
            Log.e("Insert Client","Failed to insert product into Firestore: ${e.message}")
            false
        }
    }

    override suspend fun updateClientById(id: Int, field: String, value: String, reference: String)
    {
        collection
            .document(reference)
            .update(
                mapOf( field to value),
            )
            .await()
    }

    override suspend fun getClientReference(id: Int):String {
        try {
            val querySnapshot = collection
                .whereEqualTo("id", id)
                .get()
                .await()
                .first()
                .reference
            return querySnapshot.id
        } catch (e: Exception) {
            Log.e("TAG", "Fail to get client's reference: ${e.message}")
            throw e
        }
    }

    override suspend fun loadImageUri(uri: Uri): String {
        return if(uri.toString() != "") {
            val storageRef = storage.reference
            val fileRef = storageRef.child("images/${uri.lastPathSegment}")
            fileRef.putFile(uri).await() // Esperar a que se complete la carga
            val downloadUrl =
                fileRef.downloadUrl.await() // Esperar a que se obtenga la URL de descarga
            downloadUrl.toString()
        } else
            ""
    }

    override suspend fun deleteImage(path: String): Boolean {
        return try{
            storage.reference
                .child("images/${path}")
                .delete()
                .await()
            true
        }catch (e: Exception){
            Log.e("Delete Image", "Error al eliminar imagen: ${e.message}")
            false
        }
    }
}