package sri.example.aichatbot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(
    private val messages: MutableList<Message>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val USER = 1
        private const val BOT = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isUser) USER else BOT
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        val layout = if (viewType == USER) {
            R.layout.item_user_message
        } else {
            R.layout.item_bot_message
        }

        val view = LayoutInflater.from(parent.context)
            .inflate(layout, parent, false)

        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {

        val message = messages[position]

        val messageHolder =
            holder as MessageViewHolder

        messageHolder.text.text = message.text
        messageHolder.time.text = message.time

        // Small animation when message appears
        messageHolder.itemView.alpha = 0f
        messageHolder.itemView.translationY = 15f

        messageHolder.itemView.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(200)
            .start()
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    class MessageViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        val text: TextView =
            view.findViewById(R.id.messageText)

        val time: TextView =
            view.findViewById(R.id.messageTime)
    }
}