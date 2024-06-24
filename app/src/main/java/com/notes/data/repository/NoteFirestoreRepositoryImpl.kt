package com.notes.data.repository

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.notes.domain.model.Note
import com.notes.domain.repository.NoteCloudFirestoreRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject


/**
 * Created by Kamil Lenartowicz on 06/20/2024 @ QVC, Inc
 */
class NoteFirestoreRepositoryImpl @Inject constructor() : NoteCloudFirestoreRepository {

    private val db = Firebase.firestore
    private val TAG = NoteFirestoreRepositoryImpl::class.java.name

    override fun getAllNotes(): Flow<List<Note>> {
        return callbackFlow {
            db.collection("note")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val notes = mutableListOf<Note>()
                    for (document in querySnapshot.documents) {
                        val title = document.getString("title") ?: ""
                        val content = document.getString("content") ?: ""
                        val selectedColorIndex =
                            document.getLong("selectedColorIndex")?.toInt() ?: 0
                        val documentId = document.id
                        Note(
                            title = title,
                            content = content,
                            selectedColorIndex = selectedColorIndex,
                            documentId = documentId
                        ).let { notes.add(it) }
                    }
                    trySend(notes)
                    close()
                    Log.i(
                        TAG,
                        "Successfully fetched ${notes.size} notes from cloud firestore"
                    )
                }
                .addOnFailureListener {
                    close(it)
                    Log.e(
                        TAG,
                        "Failure when trying to get nots from cloud firestore",
                        it
                    )
                }
            awaitClose()
        }
    }

    override suspend fun insertNote(note: Note) {
        db.collection("note")
            .document(note.documentId)
            .set(note)
            .addOnSuccessListener {
                Log.i(
                    TAG,
                    "Successfully added new $note to cloud firestore"
                )
            }
            .addOnFailureListener {
                Log.e(
                    TAG,
                    "Failure when trying to add new $note to cloud firestore",
                    it
                )
            }
    }

    override suspend fun updateNote(note: Note) {
        db.collection("note")
            .document(note.documentId)
            .set(note)
            .addOnSuccessListener {
                Log.i(TAG, "Successfully updated $note in cloud firestore")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failure when trying to update $note in cloud firestore", e)
            }
    }

    override suspend fun deleteNote(note: Note) {
        db.collection("note")
            .document(note.documentId)
            .delete()
            .addOnSuccessListener {
                Log.i(TAG, "Successfully deleted $note from cloud firestore")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failure when trying to delete $note from cloud firestore", e)
            }
    }
}