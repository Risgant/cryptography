package by.shnitko.client

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import by.shnitko.client.data.UserCache

class NoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        val textView: TextView = findViewById<View>(R.id.textView) as TextView
        textView.text = UserCache.selectedNote
    }
}