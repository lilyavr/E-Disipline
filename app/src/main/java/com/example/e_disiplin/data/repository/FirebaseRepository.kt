package com.example.e_disiplin.data.repository

import com.example.e_disiplin.domain.model.Admin
import com.example.e_disiplin.domain.model.Mahasiswa
import com.example.e_disiplin.domain.model.KategoriPelanggaran
import com.example.e_disiplin.domain.model.Pelanggaran
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseRepository {

    private val db = FirebaseFirestore.getInstance()
    private val adminCollection = db.collection("admins")
    private val mahasiswaCollection = db.collection("mahasiswa")
    private val kategoriCollection = db.collection("kategori_pelanggaran")
    private val pelanggaranCollection = db.collection("pelanggaran_mahasiswa")

    // Admin Operations
    suspend fun getAdmin(id: String): Admin? {
        return try {
            val document = adminCollection.document(id).get().await()
            if (document.exists()) {
                document.toObject(Admin::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun addAdmin(admin: Admin): Boolean {
        return try {
            val id = admin.id.ifEmpty { adminCollection.document().id }
            val newAdmin = admin.copy(id = id)
            adminCollection.document(id).set(newAdmin).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun loginAdmin(username: String, password: String): Boolean {
        return try {
            val querySnapshot = adminCollection
                .whereEqualTo("username", username)
                .whereEqualTo("password", password)
                .get()
                .await()
            !querySnapshot.isEmpty
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Mahasiswa Operations
    suspend fun getMahasiswa(nim: String): Mahasiswa? {
        return try {
            val document = mahasiswaCollection.document(nim).get().await()
            if (document.exists()) {
                document.toObject(Mahasiswa::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun loginMahasiswa(nim: String, password: String): Boolean {
        return try {
            val querySnapshot = mahasiswaCollection
                .whereEqualTo("nim", nim)
                .whereEqualTo("password", password)
                .get()
                .await()
            !querySnapshot.isEmpty
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun addMahasiswa(mahasiswa: Mahasiswa): Boolean {
        return try {
            // Using NIM as the document ID for Mahasiswa
            mahasiswaCollection.document(mahasiswa.nim).set(mahasiswa).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun getKategoriList(): List<KategoriPelanggaran> {
        return try {
            val snapshot = kategoriCollection.get().await()
            snapshot.toObjects(KategoriPelanggaran::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun addKategori(kategori: KategoriPelanggaran): Boolean {
        return try {
            val docRef = kategoriCollection.document()
            val newKategori = kategori.copy(id = docRef.id)
            docRef.set(newKategori).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun deleteKategori(id: String): Boolean {
        return try {
            kategoriCollection.document(id).delete().await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun addPelanggaran(pelanggaran: Pelanggaran): Boolean {
        return try {
            val pelanggaranRef = pelanggaranCollection.document()
            val mahasiswaRef = mahasiswaCollection.document(pelanggaran.nimMahasiswa)
            val newPelanggaran = pelanggaran.copy(id = pelanggaranRef.id, tanggal = System.currentTimeMillis())

            db.runTransaction { transaction ->
                val mahasiswaSnapshot = transaction.get(mahasiswaRef)
                
                // Get current totalPoin, default to 0 if not found
                val currentPoin = mahasiswaSnapshot.getLong("totalPoin") ?: 0L
                val updatedPoin = currentPoin + pelanggaran.poin

                // Update Mahasiswa
                transaction.update(mahasiswaRef, "totalPoin", updatedPoin)
                
                // Insert Pelanggaran
                transaction.set(pelanggaranRef, newPelanggaran)
            }.await()
            
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun addPendingPelanggaran(nimMahasiswa: String, jenisQr: String): Pelanggaran? {
        return try {
            val kategoriName = when (jenisQr) {
                "QR_KETERLAMBATAN" -> "Telat Masuk Kelas"
                "QR_UMUM" -> "Pelanggaran Umum"
                else -> "Pelanggaran Tidak Diketahui"
            }

            val pelanggaranRef = pelanggaranCollection.document()
            val newPelanggaran = Pelanggaran(
                id = pelanggaranRef.id,
                nimMahasiswa = nimMahasiswa,
                kategoriId = "",
                kategoriName = kategoriName,
                tingkat = "",
                poin = 0,
                status = "Pending",
                tanggal = System.currentTimeMillis()
            )

            pelanggaranRef.set(newPelanggaran).await()
            newPelanggaran
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun verifyPelanggaran(pelanggaranId: String, nimMahasiswa: String, kategoriId: String, kategoriName: String, tingkat: String, poin: Int): Boolean {
        return try {
            // Update Pelanggaran status
            pelanggaranCollection.document(pelanggaranId).update(
                mapOf(
                    "status" to "Verified",
                    "kategoriId" to kategoriId,
                    "kategoriName" to kategoriName,
                    "tingkat" to tingkat,
                    "poin" to poin
                )
            ).await()

            // Update Mahasiswa total poin
            val mahasiswaRef = mahasiswaCollection.document(nimMahasiswa)
            val mahasiswaSnapshot = mahasiswaRef.get().await()
            val currentPoin = mahasiswaSnapshot.getLong("totalPoin") ?: 0
            mahasiswaRef.update("totalPoin", currentPoin + poin).await()

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getPendingPelanggaranFlow(): Flow<List<Pelanggaran>> = callbackFlow {
        val listener = pelanggaranCollection
            .whereEqualTo("status", "Pending")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    close(e)
                    return@addSnapshotListener
                }
                
                if (snapshot != null) {
                    val list = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Pelanggaran::class.java)?.copy(id = doc.id)
                    }.sortedByDescending { it.tanggal }
                    trySend(list)
                }
            }
        
        awaitClose { listener.remove() }
    }

    fun getAllPelanggaranFlow(): Flow<List<Pelanggaran>> = callbackFlow {
        val listener = pelanggaranCollection
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    close(e)
                    return@addSnapshotListener
                }
                
                if (snapshot != null) {
                    val list = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Pelanggaran::class.java)?.copy(id = doc.id)
                    }
                    trySend(list)
                }
            }
        
        awaitClose { listener.remove() }
    }

    suspend fun getTotalMahasiswaCount(): Long {
        return try {
            val snapshot = mahasiswaCollection.count().get(AggregateSource.SERVER).await()
            snapshot.count
        } catch (e: Exception) {
            e.printStackTrace()
            0L
        }
    }

    fun getAllMahasiswaFlow(): Flow<List<Mahasiswa>> = callbackFlow {
        val listener = mahasiswaCollection
            .orderBy("nim")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    close(e)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val list = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Mahasiswa::class.java)
                    }
                    trySend(list)
                }
            }
        awaitClose { listener.remove() }
    }

    fun getPelanggaranByNimFlow(nim: String): Flow<List<Pelanggaran>> = callbackFlow {
        val listener = pelanggaranCollection
            .whereEqualTo("nimMahasiswa", nim)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    close(e)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val list = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Pelanggaran::class.java)?.copy(id = doc.id)
                    }.sortedByDescending { it.tanggal }
                    trySend(list)
                }
            }
        awaitClose { listener.remove() }
    }
    
    // --- Forgot Password Methods ---
    
    suspend fun verifyMahasiswaForReset(nim: String, tanggalLahir: String): Boolean {
        return try {
            val querySnapshot = mahasiswaCollection
                .whereEqualTo("nim", nim)
                .whereEqualTo("tanggalLahir", tanggalLahir)
                .get()
                .await()
            !querySnapshot.isEmpty
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun verifyAdminForReset(username: String, email: String): Boolean {
        return try {
            val querySnapshot = adminCollection
                .whereEqualTo("username", username)
                .whereEqualTo("email", email)
                .get()
                .await()
            !querySnapshot.isEmpty
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun resetMahasiswaPassword(nim: String, newPasswordHash: String): Boolean {
        return try {
            mahasiswaCollection.document(nim).update("password", newPasswordHash).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun resetAdminPassword(username: String, newPasswordHash: String): Boolean {
        return try {
            val querySnapshot = adminCollection
                .whereEqualTo("username", username)
                .get()
                .await()
            if (!querySnapshot.isEmpty) {
                val docId = querySnapshot.documents.first().id
                adminCollection.document(docId).update("password", newPasswordHash).await()
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
