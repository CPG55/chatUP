package dam.cpg.chatup.vista;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dam.cpg.chatup.R;
import dam.cpg.chatup.modelo.Group;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Carlos Pérez on 13/06/18.
 */
class GroupHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.group_user_name) TextView _userName;
    @BindView(R.id.group_user_profile_picture) CircleImageView _userProfilePicture;
    //@BindView(R.id.user_last_message) TextView _userLastMessage;

    GroupHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(itemView);

    }

    void bind(Group group) {//
        _userName.setText(group.getOppositeUser().getName());
        //_userProfilePicture.setImageURI();
    }

}