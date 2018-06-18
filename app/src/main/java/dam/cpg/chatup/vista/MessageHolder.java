package dam.cpg.chatup.vista;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dam.cpg.chatup.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Carlos Pérez on 17/06/18.
 */
public class MessageHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.message_sender_name) TextView _userName;
    @BindView(R.id.message_timestamp)   TextView _timeStamp;
    @BindView(R.id.message_text)        TextView _messageText;
    @BindView(R.id.message_profile_picture) CircleImageView _profilePicture;

    MessageHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);

    }

}