package fettle.iiitd.com.fettle.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import fettle.iiitd.com.fettle.Activities.RestrauntList;
import fettle.iiitd.com.fettle.Classes.Utils;
import fettle.iiitd.com.fettle.R;


public class FoodCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static LayoutInflater inflater = null;
    int i = 0;
    private Activity activity;
    private List<Utils.FoodCategory> listCategories;

    public FoodCategoryAdapter(Activity a, List<Utils.FoodCategory> list) {
        activity = a;
        inflater = LayoutInflater.from(a.getApplicationContext());
        this.listCategories = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.list_item_course_stats_overview, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        ((ViewHolder) viewHolder).course.setText(listCategories.get(position).name);
        ((ViewHolder) viewHolder).backgroundImage.setImageResource(getCategoryImage(listCategories.get(position).name));
        ((ViewHolder) viewHolder).view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, RestrauntList.class);
                intent.putExtra("category", listCategories.get(position).name);
                activity.startActivity(intent);
            }
        });
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listCategories.size();
    }

    int getCategoryImage(String category) {
        if (category.equalsIgnoreCase("bakery")) {
            return R.drawable.bakery;
        } else if (category.equalsIgnoreCase("beverages")) {
            return R.drawable.beverages;

        } else if (category.equalsIgnoreCase("breads")) {
            return R.drawable.breads;

        } else if (category.equalsIgnoreCase("breakfast")) {
            return R.drawable.breakfast;

        } else if (category.equalsIgnoreCase("burger")) {
            return R.drawable.burger;

        } else if (category.equalsIgnoreCase("burritos")) {
            return R.drawable.burritos;

        } else if (category.equalsIgnoreCase("chicken")) {
            return R.drawable.chicken;

        } else if (category.equalsIgnoreCase("desserts")) {
            return R.drawable.desserts;

        } else if (category.equalsIgnoreCase("diment")) {
            return R.drawable.diment;

        } else if (category.equalsIgnoreCase("donuts")) {
            return R.drawable.donuts;

        } else if (category.equalsIgnoreCase("enchiladas")) {
            return R.drawable.enchiladas;

        } else if (category.equalsIgnoreCase("fazitas")) {
            return R.drawable.enchiladas;

        } else if (category.equalsIgnoreCase("fries")) {
            return R.drawable.fries;

        } else if (category.equalsIgnoreCase("frozen_yogurt")) {
            return R.drawable.frozen_yogurt;

        } else if (category.equalsIgnoreCase("icecream")) {
            return R.drawable.icecream;

        } else if (category.equalsIgnoreCase("kids_menu")) {
            return R.drawable.kids_menu;

        } else if (category.equalsIgnoreCase("lunch")) {
            return R.drawable.lunch;

        } else if (category.equalsIgnoreCase("meal_components")) {
            return R.drawable.meal_components;

        } else if (category.equalsIgnoreCase("menu_items")) {
            return R.drawable.menu_items;

        } else if (category.equalsIgnoreCase("pancakes")) {
            return R.drawable.pancakes;

        } else if (category.equalsIgnoreCase("pasta")) {
            return R.drawable.pasta;

        } else if (category.equalsIgnoreCase("pizza")) {
            return R.drawable.pizza;

        } else if (category.equalsIgnoreCase("quesadillas")) {
            return R.drawable.quesadillas;

        } else if (category.equalsIgnoreCase("salad")) {
            return R.drawable.salad;

        } else if (category.equalsIgnoreCase("salad_dressing")) {
            return R.drawable.salad_dressing;

        } else if (category.equalsIgnoreCase("sandwich_components")) {
            return R.drawable.sandwich_components;

        } else if (category.equalsIgnoreCase("sauces")) {
            return R.drawable.sauces;

        } else if (category.equalsIgnoreCase("seniors_menu")) {
            return R.drawable.seniors_menu;

        } else if (category.equalsIgnoreCase("sides")) {
            return R.drawable.sides;

        } else if (category.equalsIgnoreCase("soups")) {
            return R.drawable.soups;

        } else if (category.equalsIgnoreCase("sushi")) {
            return R.drawable.sushi;

        } else if (category.equalsIgnoreCase("tacos")) {
            return R.drawable.tacos;

        } else if (category.equalsIgnoreCase("taquitos")) {
            return R.drawable.taquitos;

        } else if (category.equalsIgnoreCase("toppings")) {
            return R.drawable.toppings;

        }
        return R.drawable.toppings;


    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView course;
        public LinearLayout back;
        public ImageView backgroundImage;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            course = (TextView) itemView.findViewById(R.id.firstLine);
            back = (LinearLayout) itemView.findViewById(R.id.mainback);
            backgroundImage = (ImageView) itemView.findViewById(R.id.backgroundImage);
        }
    }
}