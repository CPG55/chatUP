package dam.cpg.chatup.vista;

import android.view.View;

/**
 * @author Carlos Pérez on 17/06/18.
 */
public interface RecyclerViewItemClickListener {

    void onItemClick(View view, int position);

    void onItemLongClick(View view, int position);

}