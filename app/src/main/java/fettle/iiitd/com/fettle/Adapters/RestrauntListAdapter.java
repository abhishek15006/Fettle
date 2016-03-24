package fettle.iiitd.com.fettle.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fettle.iiitd.com.fettle.Classes.Menu;
import fettle.iiitd.com.fettle.Classes.Restraunt;
import fettle.iiitd.com.fettle.R;

/**
 * Created by danishgoel on 24/03/16.
 */
public class RestrauntListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Restraunt> messages;
    private LayoutInflater inflater = null;

    public RestrauntListAdapter(Context context, ArrayList<Restraunt> messages) {
        this.messages = messages;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserListViewHolder(inflater.inflate(R.layout.restraunt_list_card, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

//        ((UserListViewHolder) holder).tvName.setText(messages.get(position).getString("name"));
//        ((UserListViewHolder) holder).tvRole.setVisibility(View.INVISIBLE);
//        ((UserListViewHolder) holder).tvLocation.setVisibility(View.INVISIBLE);
//        if (messages.get(position).getString("position") != null)
//            ((UserListViewHolder) holder).tvRole.setText(messages.get(position).getString("position"));
//        if (messages.get(position).getParseObject("constituency") != null)
//            ((UserListViewHolder) holder).tvLocation.setText(messages.get(position).getParseObject("constituency").getString("name"));
//        if (!usersInstalled.contains(messages.get(position).getObjectId())) {
//            ((UserListViewHolder) holder).tvName.setAlpha((float) .4);
//        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    private static class UserListViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvRole;
        public TextView tvLocation;
        public ImageView ivAskerPicture;
        public View view;

        public UserListViewHolder(View itemView) {
            super(itemView);
            view = itemView;
//            tvName = (TextView) itemView.findViewById(R.id.tvName);
//            tvRole = (TextView) itemView.findViewById(R.id.tvRole);
//            tvLocation = (TextView) itemView.findViewById(R.id.tvLocation);
//            ivAskerPicture = (ImageView) itemView.findViewById(R.id.askerPicture);
        }
    }
}

