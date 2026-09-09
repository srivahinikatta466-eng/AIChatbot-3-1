package sri.example.aichatbot

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var messageInput: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var newChatButton: TextView
    private lateinit var adapter: ChatAdapter

    private val messages = mutableListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView)
        messageInput = findViewById(R.id.messageInput)
        sendButton = findViewById(R.id.sendButton)
        newChatButton = findViewById(R.id.newChatButton)

        // System bar handling
        val rootLayout =
            findViewById<View>(R.id.rootLayout)

        ViewCompat.setOnApplyWindowInsetsListener(
            rootLayout
        ) { view, insets ->

            val systemBars =
                insets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
                )

            view.setPadding(
                0,
                systemBars.top,
                0,
                systemBars.bottom
            )

            insets
        }

        // RecyclerView
        adapter = ChatAdapter(messages)

        recyclerView.layoutManager =
            LinearLayoutManager(this)

        recyclerView.adapter = adapter

        // Welcome message
        addBotMessage(
            "Hello! 👋 I'm your AI Assistant. How can I help you?"
        )

        // Send button
        sendButton.setOnClickListener {
            sendMessage()
        }

        // New chat
        newChatButton.setOnClickListener {
            startNewChat()
        }

        // Keyboard send
        messageInput.setOnEditorActionListener {
                _, actionId, event ->

            if (
                actionId == EditorInfo.IME_ACTION_SEND ||
                (
                        event != null &&
                                event.keyCode ==
                                KeyEvent.KEYCODE_ENTER &&
                                event.action ==
                                KeyEvent.ACTION_DOWN
                        )
            ) {

                sendMessage()
                true

            } else {
                false
            }
        }
    }

    private fun sendMessage() {

        val userText =
            messageInput.text
                .toString()
                .trim()

        if (userText.isEmpty()) {
            return
        }

        addUserMessage(userText)

        messageInput.text.clear()

        val response =
            generateResponse(userText)

        addBotMessage(response)
    }

    private fun addUserMessage(text: String) {

        messages.add(
            Message(
                text = text,
                isUser = true,
                time = getCurrentTime()
            )
        )

        adapter.notifyItemInserted(
            messages.size - 1
        )

        scrollToBottom()
    }

    private fun addBotMessage(text: String) {

        messages.add(
            Message(
                text = text,
                isUser = false,
                time = getCurrentTime()
            )
        )

        adapter.notifyItemInserted(
            messages.size - 1
        )

        scrollToBottom()
    }

    private fun getCurrentTime(): String {

        val formatter =
            SimpleDateFormat(
                "hh:mm a",
                Locale.getDefault()
            )

        return formatter.format(Date())
    }

    private fun scrollToBottom() {

        if (messages.isNotEmpty()) {

            recyclerView.post {

                recyclerView.smoothScrollToPosition(
                    messages.size - 1
                )
            }
        }
    }

    private fun startNewChat() {

        messages.clear()

        adapter.notifyDataSetChanged()

        addBotMessage(
            "Hello! 👋 I'm your AI Assistant. How can I help you?"
        )
    }

    private fun generateResponse(
        input: String
    ): String {

        val message =
            input.lowercase().trim()

        return when {

            message == "hi" ||
                    message == "hello" ||
                    message.contains("hey") ->

                "Hello! 😊 Nice to meet you. How can I help you?"

            message.contains("how are you") ->

                "I'm doing great! 🤖 Thanks for asking. What would you like to know?"

            message.contains("your name") ||
                    message.contains("who are you") ->

                "I'm your AI Assistant. I'm here to answer your questions and help you."

            message.contains("what are you doing") ->

                "I'm right here chatting with you! 😊 Ask me anything."

            message.contains("help") ->

                "Of course! You can ask me questions, learn concepts, or simply have a conversation with me."

            message.contains("thank") ->

                "You're very welcome! 😊"

            message.contains("good morning") ->

                "Good morning! ☀️ I hope you're having a great day."

            message.contains("good night") ->

                "Good night! 🌙 Have a peaceful night and take care."

            message.contains("bye") ->

                "Goodbye! 👋 It was nice chatting with you."

            message.contains("what is ai") ->

                "AI, or Artificial Intelligence, is technology that enables computers to perform tasks that normally require human intelligence."

            message.contains("what is machine learning") ->

                "Machine Learning is a branch of AI where computers learn patterns from data and use those patterns to make predictions or decisions."

            else ->

                "That's interesting! 🤔 You said: \"$input\"\n\nI'm currently running in demo mode. Gemini AI can be connected later for intelligent responses."
        }
    }
}