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
    /**
     * Retrieves an Admin document from the 'admins' collection by its ID.
     * @param id The unique identifier of the Admin.
     * @return The [Admin] object if found, or null otherwise.
     */
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

    /**
     * Adds a new Admin document to the 'admins' collection.
     * If the ID is empty, a new unique document ID will be generated.
     * @param admin The [Admin] object to insert.
     * @return True if the operation was successful, false otherwise.
     */
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

    /**
     * Authenticates an admin by querying the collection for a matching username and password.
     * @param username The username of the Admin.
     * @param password The raw password of the Admin.
     * @return The authenticated [Admin] object if credentials match, or null otherwise.
     */
    suspend fun loginAdmin(username: String, password: String): Admin? {
        return try {
            val querySnapshot = adminCollection
                .whereEqualTo("username", username)
                .whereEqualTo("password", password)
                .get()
                .await()
            if (!querySnapshot.isEmpty) {
                querySnapshot.documents.first().toObject(Admin::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Updates an existing Admin document in the 'admins' collection.
     * @param admin The [Admin] object containing updated fields.
     * @return True if the update was successful, false otherwise.
     */
    suspend fun updateAdmin(admin: Admin): Boolean {
        return try {
            adminCollection.document(admin.id).set(admin).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Mahasiswa Operations
    /**
     * Retrieves a Mahasiswa document from the 'mahasiswa' collection by their NIM.
     * @param nim The unique student identifier.
     * @return The [Mahasiswa] object if found, or null otherwise.
     */
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

    /**
     * Authenticates a Mahasiswa by checking if the NIM and password match an existing record.
     * @param nim The student's NIM.
     * @param password The student's hashed or raw password (depending on implementation).
     * @return True if credentials are valid, false otherwise.
     */
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

    /**
     * Adds a new Mahasiswa to the database, using their NIM as the document ID.
     * @param mahasiswa The [Mahasiswa] object to insert.
     * @return True if the operation was successful, false otherwise.
     */
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

    /**
     * Retrieves all violation categories (Kategori Pelanggaran) from Firestore.
     * @return A list of [KategoriPelanggaran] objects, or an empty list if an error occurs.
     */
    suspend fun getKategoriList(): List<KategoriPelanggaran> {
        return try {
            val snapshot = kategoriCollection.get().await()
            snapshot.toObjects(KategoriPelanggaran::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * Creates a new violation category in the 'kategori_pelanggaran' collection.
     * @param kategori The [KategoriPelanggaran] object to insert.
     * @return True if the creation was successful, false otherwise.
     */
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

    /**
     * Deletes a specific violation category from Firestore by its ID.
     * @param id The unique identifier of the category to delete.
     * @return True if deletion was successful, false otherwise.
     */
    suspend fun deleteKategori(id: String): Boolean {
        return try {
            kategoriCollection.document(id).delete().await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Records a new violation (Pelanggaran) for a Mahasiswa.
     * This operation runs in a Firestore Transaction to ensure the student's total points are safely updated alongside the violation insertion.
     * @param pelanggaran The [Pelanggaran] object to record.
     * @return True if the transaction succeeds, false otherwise.
     */
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

    /**
     * Records a new pending violation for a Mahasiswa scanned via QR.
     * This record is not finalized until an Admin verifies it.
     * @param nimMahasiswa The NIM of the student.
     * @param jenisQr The type of QR scanned (e.g., "QR_KETERLAMBATAN", "QR_UMUM").
     * @return The newly created [Pelanggaran] object, or null on error.
     */
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

    /**
     * Verifies a pending violation, finalizing its category, points, and severity level.
     * It updates the violation status to "Verified" and adds the points to the Mahasiswa's total.
     * @param pelanggaranId The ID of the pending violation.
     * @param nimMahasiswa The student's NIM.
     * @param kategoriId The finalized category ID.
     * @param kategoriName The finalized category name.
     * @param tingkat The severity level (e.g., "Ringan").
     * @param poin The point penalty to apply.
     * @return True if verification and point updates succeeded, false otherwise.
     */
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

    /**
     * Provides a real-time stream (Flow) of all violations currently marked as "Pending".
     * Used by the Admin notification screen.
     * @return A Flow emitting lists of pending [Pelanggaran].
     */
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

    /**
     * Provides a real-time stream (Flow) of all violations across all students.
     * Used for system-wide statistics and dashboards.
     * @return A Flow emitting lists of all [Pelanggaran].
     */
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

    /**
     * Gets the total count of Mahasiswa registered in the system.
     * Uses Firestore's optimized Aggregate query to avoid downloading all documents.
     * @return The total count, or 0 if an error occurs.
     */
    suspend fun getTotalMahasiswaCount(): Long {
        return try {
            val snapshot = mahasiswaCollection.count().get(AggregateSource.SERVER).await()
            snapshot.count
        } catch (e: Exception) {
            e.printStackTrace()
            0L
        }
    }

    /**
     * Provides a real-time stream of all Mahasiswa, ordered by NIM.
     * @return A Flow emitting lists of [Mahasiswa].
     */
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

    /**
     * Provides a real-time stream of violations committed by a specific student.
     * @param nim The student's NIM.
     * @return A Flow emitting lists of [Pelanggaran] tied to the student, sorted by latest date.
     */
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
    
    /**
     * Verifies if the provided NIM and Date of Birth match an existing student record.
     * Used in the forgot password flow for Mahasiswa.
     * @param nim The student's NIM.
     * @param tanggalLahir The student's Date of Birth (format: DD-MM-YYYY).
     * @return True if a matching record is found, false otherwise.
     */
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

    /**
     * Verifies if the provided Username and Email match an existing admin record.
     * Used in the forgot password flow for Admins.
     * @param username The Admin's username.
     * @param email The Admin's registered email.
     * @return True if a matching record is found, false otherwise.
     */
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

    /**
     * Resets a Mahasiswa's password.
     * @param nim The student's NIM.
     * @param newPasswordHash The new hashed password to be saved.
     * @return True if the password was successfully updated, false otherwise.
     */
    suspend fun resetMahasiswaPassword(nim: String, newPasswordHash: String): Boolean {
        return try {
            mahasiswaCollection.document(nim).update("password", newPasswordHash).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Resets an Admin's password.
     * @param username The Admin's username used to locate the document.
     * @param newPasswordHash The new raw or hashed password to be saved.
     * @return True if the password was successfully updated, false otherwise.
     */
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
