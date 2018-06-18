package dam.cpg.chatup.vista;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import dam.cpg.chatup.R;
import dam.cpg.chatup.modelo.Message;
import dam.cpg.chatup.util.DateParsing;

/**
 * @author Carlos Pérez on 17/06/18.
 */
public class MessagesAdapter extends RecyclerView.Adapter<MessageHolder> {

    private List<Message> messageList = new ArrayList<>();
    private Context context;

    public MessagesAdapter(Context context) {
        this.context = context;
    }

    public void addMensaje(Message newMessage){
        messageList.add(newMessage);
        notifyItemInserted(messageList.size());
    }

    @Override
    public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View messageItemView = LayoutInflater.from(context).inflate(R.layout.message_item,parent,false);

        return new MessageHolder(messageItemView);
    }

    @Override
    public void onBindViewHolder(MessageHolder holder, int position) {

        holder._userName.setText(messageList.get(position).getSenderName());
        holder._messageText.setText(messageList.get(position).getText());

//        // Aquí se transforma el formato del timeStamp de UTC a localtime.
//        String messageSentTimeParsedToLocalTime = DateParsing.convertUTCToLocalTime(messageList.get(position).getTimeStamp());
//        // Conversión a "visualizacion amistosa".
//        String friendlyParsedTime = DateParsing.convertDateToFriendly(messageSentTimeParsedToLocalTime , context);
//
//        // Y despues se hace el bind al interface.
//        holder._timeStamp.setText(friendlyParsedTime);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

}
