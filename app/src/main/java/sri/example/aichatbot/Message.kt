package sri.example.aichatbot

data class Message(
    val text: String,
    val isUser: Boolean,
    val time: String
)