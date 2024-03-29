package com.nan_app.database

import android.net.Uri
import com.nan_app.entities.Clients
import java.lang.ref.PhantomReference

interface ClientSource {

    suspend fun loadClientById(id: Int): Boolean
    suspend fun loadClientByName()
    suspend fun loadClientByLastName()
    suspend fun loadAllClients()
    suspend fun deleteClient(id : Int)
    suspend fun insertClient(newClient: Clients): Boolean
    suspend fun updateClientById(id: Int, field : String, value: String, reference: String)
    suspend fun getClientReference(id: Int): String
    suspend fun loadImageUri (uri: Uri) :String
    suspend fun deleteImage(path: String): Boolean
}