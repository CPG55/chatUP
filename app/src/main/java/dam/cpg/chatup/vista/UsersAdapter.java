package dam.cpg.chatup.vista;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import dam.cpg.chatup.R;
import dam.cpg.chatup.modelo.User;

/**
 * @author Carlos Pérez on 13/06/18.
 */
public class UsersAdapter extends RecyclerView.Adapter<UserHolder> {

    private List<User> usersList = new ArrayList<>();
    private Context context;

//    public UsersAdapter(Context context, List<User> userList) {
//        this.userList = userList;
//        this.context = context;
//    }

    public UsersAdapter(Context context) {
        this.context = context;
    }


    public void addUser(User user) {
        usersList.add(user);
        notifyItemInserted(usersList.size());
    }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ViewHolder, int)
     */
    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, null);

        return new UserHolder(layoutView);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Override {@link #onBindViewHolder(ViewHolder, int, List)} instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        holder._userName.setText(usersList.get(position).getName());
        //holder._userProfilePicture.setImageResource(usersList.get(position).getProfilePictureURL());
//        holder.itemView.setOnClickListener(new View.OnClickListener(usersList.get(position).getName()) {
//            public View.OnClickListener(String userId)  {
//
//            }
//
//            @Override
//            public void onClick(View v) {
//                usersList.get(position).getProfilePictureURL();
//            }
//        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return usersList.size();
    }

}
