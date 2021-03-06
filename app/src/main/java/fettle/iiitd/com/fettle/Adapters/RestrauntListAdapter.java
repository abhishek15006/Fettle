package fettle.iiitd.com.fettle.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import fettle.iiitd.com.fettle.Activities.RestrauntMenuList;
import fettle.iiitd.com.fettle.Classes.Restraunt;
import fettle.iiitd.com.fettle.R;

/**
 * Created by danishgoel on 24/03/16.
 */
public class RestrauntListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    Context context;
    private List<Restraunt> messages;
    private LayoutInflater inflater = null;
    private String category;

    public RestrauntListAdapter(Context context, String category, List<Restraunt> messages) {
        this.messages = messages;
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.category = category;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RestrauntViewHolder(inflater.inflate(R.layout.restraunt_list_card, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        ((RestrauntViewHolder) holder).card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, RestrauntMenuList.class);
                myIntent.putExtra("restaurant", messages.get(position).getName());
                myIntent.putExtra("category", category);
                context.startActivity(myIntent);
            }
        });

        ((RestrauntViewHolder) holder).tvRestrauntName.setText(messages.get(position).getName());
        ((RestrauntViewHolder) holder).tvFoodType.setText(category);
        ((RestrauntViewHolder) holder).tvMinOrder.setText("");
        ((RestrauntViewHolder) holder).ivRestrauntIcon.setBackgroundResource(R.drawable.subway_icon);
        ((RestrauntViewHolder) holder).rating.setRating(3);



    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    @Override
    public Filter getFilter() {
        return new UserFilter(this, messages);
    }

    private static class UserFilter extends Filter {

        private final RestrauntListAdapter adapter;

        private final List<Restraunt> originalList;

        private final List<Restraunt> filteredList;

        private UserFilter(RestrauntListAdapter adapter, List<Restraunt> originalList) {
            super();
            this.adapter = adapter;
            this.originalList = new LinkedList<>(originalList);
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();

            if (constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();

                for (final Restraunt user : originalList) {
                    if (user.getName().contains(filterPattern)) {
                        filteredList.add(user);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.messages.clear();
            adapter.messages.addAll((ArrayList<Restraunt>) results.values);
            adapter.notifyDataSetChanged();
        }
    }

    private static class RestrauntViewHolder extends RecyclerView.ViewHolder {
        public TextView tvRestrauntName;
        public TextView tvFoodType;
        public TextView tvMinOrder;
        public ImageView ivRestrauntIcon;
        public RatingBar rating;
        public CardView card;
        public View view;

        public RestrauntViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            card = (CardView) itemView.findViewById(R.id.restrauntCard);
            tvRestrauntName = (TextView) itemView.findViewById(R.id.restraunt_name);
            tvFoodType = (TextView) itemView.findViewById(R.id.food_type);
            tvMinOrder = (TextView) itemView.findViewById(R.id.minOrder);
            ivRestrauntIcon = (ImageView) itemView.findViewById(R.id.restraunt_icon);
            rating = (RatingBar) itemView.findViewById(R.id.ratingBar);
        }
    }
}

