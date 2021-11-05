package by.shnitko.client.ui.main

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import by.shnitko.client.R
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import android.view.View
import android.widget.ListView
import androidx.annotation.RequiresApi
import by.shnitko.client.NoteActivity
import by.shnitko.client.data.UserCache
import by.shnitko.client.data.model.LoggedInUser
import by.shnitko.client.data.model.NoteDto
import by.shnitko.client.data.model.NotesDto
import by.shnitko.client.data.util.Encryptor
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking


class MainActivity : AppCompatActivity() {

    private val httpClient: HttpClient = HttpClient(CIO) {
        install(JsonFeature)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val user: LoggedInUser = UserCache.loggedUser
        val notes: NotesDto = runBlocking {
            httpClient.get("http://10.0.2.2:9040/notepad/${user.id}/all") {
                accept(ContentType.Application.Json)
            }
        }
        println(notes)

        val keys = Encryptor.decryptKeys(notes.keys, user.privateKey)
        val initVector = Encryptor.decryptInitVector(notes.initVector, user.privateKey)
        val decryptedNotes = ArrayList<NoteDto>()
        val decryptedTitles = ArrayList<String>()
        for(note in notes.notes) {
            val decryptedText = Encryptor.decryptTextSerpent(note.text, keys, initVector)
            val decryptedTitle = Encryptor.decryptTextSerpent(note.title, keys, initVector)
            decryptedNotes.add(NoteDto(note.id, decryptedTitle, decryptedText))
            decryptedTitles.add(decryptedTitle)
        }

        val adapter: ArrayAdapter<*> = ArrayAdapter(
            this,
            R.layout.activity_listview, decryptedTitles.toTypedArray()
        )
        val listView: ListView = findViewById<View>(R.id.notes_list_view) as ListView
        listView.setOnItemClickListener { parent, view, position, id ->
            val element = adapter.getItem(position) // The item that was clicked
            if (element != null) {
                for(note in decryptedNotes) {
                    if(element.equals(note.title)) {
                        UserCache.selectedNote = note.text
                    }
                }
                val intent = Intent(this, NoteActivity::class.java)
                startActivity(intent)
            }

        }
        listView.setAdapter(adapter)
    }
}