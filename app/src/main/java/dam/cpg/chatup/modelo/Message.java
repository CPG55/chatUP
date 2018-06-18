package dam.cpg.chatup.modelo;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Clase que representa un mensaje de chat.
 *
 * @author Carlos Pérez on 17/06/18.
 */
@IgnoreExtraProperties
public class Message {

    private String senderUID;
    private String senderName;
    private String timeStamp;
    private String text;
    private String messageTypeURL;

    public Message() {

    }

    public Message(String senderUID, String senderName, String timeStamp, String text) {
        this.senderUID = senderUID;
        this.senderName = senderName;
        this.timeStamp = timeStamp;
        this.text = text;
    }

    public Message(String senderUID, String senderName, String timeStamp, String text, String messageTypeURL) {
        this.senderUID = senderUID;
        this.senderName = senderName;
        this.timeStamp = timeStamp;
        this.text = text;
        this.messageTypeURL = messageTypeURL;
    }

    public String getSenderUID() {

        return senderUID;
    }

    public String getSenderName() {

        return senderName;
    }

    public String getTimeStamp() {

        return timeStamp;
    }

    public String getText() {

        return text;
    }

    public String getMessageTypeURL() {

        return messageTypeURL;
    }

    public void setSenderUID(String senderUID) {

        this.senderUID = senderUID;
    }

    public void setSenderName(String senderName) {

        this.senderName = senderName;
    }

    public void setTimeStamp(String timeStamp) {

        this.timeStamp = timeStamp;
    }

    public void setText(String text) {

        this.text = text;
    }

    public void setMessageTypeURL(String messageTypeURL) {

        this.messageTypeURL = messageTypeURL;
    }
}
