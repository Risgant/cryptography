package by.shnitko.client.data.model

data class NotesDto (
    val notes: List<NoteDto>,
    val keys: List<String>,
    val initVector: String
    )