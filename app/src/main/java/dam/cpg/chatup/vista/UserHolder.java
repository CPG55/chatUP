package dam.cpg.chatup.vista;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dam.cpg.chatup.R;
import dam.cpg.chatup.modelo.User;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Carlos Pérez on 13/06/18.
 */
class UserHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.user_name) TextView _userName;
    @BindView(R.id.user_profile_picture) ImageView _userProfilePicture;
    //@BindView(R.id.user_last_message) TextView _userLastMessage;

    UserHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);


//        itemView.setOnClickListener(new View.OnClickListener() {
//            private String id;
//
//            /**
//             * Called when a view has been clicked.
//             *
//             * @param v The view that was clicked.
//             */
//            @Override
//            public void onClick(View v) {
//
//            }
//        });



    }

//    void bind(User user) {
//        _userName.setText(user.getName());
//        //_userProfilePicture.setImageURI();
//    }

}