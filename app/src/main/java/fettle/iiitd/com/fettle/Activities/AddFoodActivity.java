package fettle.iiitd.com.fettle.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import fettle.iiitd.com.fettle.Classes.Dish;
import fettle.iiitd.com.fettle.Classes.Menu;
import fettle.iiitd.com.fettle.Classes.User;
import fettle.iiitd.com.fettle.Classes.Utils;
import fettle.iiitd.com.fettle.R;

public class AddFoodActivity extends AppCompatActivity {

    private static final String TAG = "AddFoodActivity";
    CardArrayRecyclerViewAdapter rcAdapter;
    List<Dish> dishes;
    List<Dish> dishesBreakfast = new ArrayList<>();
    List<Dish> dishesLunch = new ArrayList<>();
    List<Dish> dishesDinner = new ArrayList<>();
    List<String> breakfastDishIds = Arrays.asList("xtHmufoCKq","091YLZpUeZ","shFy4OjFnU","23tITNAj4s","jUV5amPs4L","zO7JoyUv7A","ZUTc2EFVAj","QZT5gnKjde","A9DFj9ejNT"
);
    List<String> lunchDishIds = Arrays.asList("PY5Bfv6NUC","Lp9c7eq2jl","9MG5JiPZpD","PiaCpWf2Mk","ADTpNFif3w","gPu9VXjIHm","FBrFrejA0S","qco62Shv53","66DaBrT83m","RBJp219SUu"
);
    List<String> dinnerDishIds = Arrays.asList("0syjCV7wxK","6t940cX36M","OCkjFr00TA","HyXKMqXFSF","CB2BhxEuhc","QF4uQVvRC4","ewJlWDKmLx","gPu9VXjIHm","FBrFrejA0S","zbwWGX516h");
    private List<String> meals = Arrays.asList(new String[]{"Breakfast", "Lunch", "Dinner"});
    private int[] drawables = new int[]{R.drawable.breakfast, R.drawable.lunch, R.drawable.dinner};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dishes = new ArrayList<>();

        initlist();

        GridLayoutManager lLayout = new GridLayoutManager(AddFoodActivity.this, 2);

        RecyclerView rView = (RecyclerView) findViewById(R.id.myList);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);

        rcAdapter = new CardArrayRecyclerViewAdapter(AddFoodActivity.this, dishes);
        rView.setAdapter(rcAdapter);
    }

    private void initlist() {
        ParseQuery<ParseObject> query;
        List<ParseObject> lPo;

        //breakfast
        query = new ParseQuery<ParseObject>("IndianDishes");
        List<String> ids = new ArrayList<>();
        ids.addAll(breakfastDishIds);
        ids.addAll(lunchDishIds);
        ids.addAll(dinnerDishIds);
        query.whereContainedIn("objectId", ids);
        lPo = new ArrayList<>();
        try {
            lPo = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (ParseObject each : lPo) {
            dishes.add(new Dish(each));
            if (breakfastDishIds.contains(each.getObjectId()))
                dishesBreakfast.add(new Dish(each));
            if (lunchDishIds.contains(each.getObjectId()))
                dishesLunch.add(new Dish(each));
            if (dinnerDishIds.contains(each.getObjectId()))
                dishesDinner.add(new Dish(each));
        }

    }

    private void createDialog(int position, int runcal, int walkcal, Bitmap standing, Bitmap workout) {
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.quant_dialog);
        dialog.setTitle("Add quantity");

        final Dish dish = dishes.get(position);

        final NumberPicker numberPicker = (NumberPicker) dialog.findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(100);
        numberPicker.setMinValue(0);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                setCalorieData(dialog, dish);
            }
        });

        final NumberPicker numberPicker1 = (NumberPicker) dialog.findViewById(R.id.numberPicker1);
        numberPicker1.setMinValue(0);
        numberPicker1.setMaxValue(1);
        numberPicker1.setDisplayedValues(new String[]{"grams", dish.getDescription()});
        numberPicker1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                setCalorieData(dialog, dish);
            }
        });

        dialog.show();

        setCalorieData(dialog, dish);

//        TextView WalkCal = (TextView) dialog.findViewById(R.id.time1);
//        TextView RunCal = (TextView) dialog.findViewById(R.id.time2);
        ImageView walkimage = (ImageView) dialog.findViewById(R.id.standing);
        ImageView workoutimage = (ImageView) dialog.findViewById(R.id.workout_image);
//        WalkCal.setText(walkcal + "Min");
//        RunCal.setText(runcal + "Min");
        if (standing != null) {
            walkimage.setImageBitmap(standing);
        }
        if (workout != null) {
            workoutimage.setImageBitmap(workout);
        }

        Button discard, add;
        TextView tvDish;
        TextView tvUnits;
        /*tvDish = (TextView) dialog.findViewById(R.id.tvDish);
        tvUnits = (TextView) dialog.findViewById(R.id.tvUnits);*/
//        if(dish.getName().startsWith("Calories"))
//            tvDish.setText(dish.getName().substring(12));
//        else
//            tvDish.setText(dish.getName());
//        tvUnits.setText(dish.getMeasure()+" "+dish.getDescription()+" = "+dish.getGram()+" grams");

        discard = (Button) dialog.findViewById(R.id.discard);
        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        add = (Button) dialog.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LandingActivity.updateData = true;
                ParseObject parseObject = new ParseObject("FoodIntake");
                parseObject.put("user", ParseUser.getCurrentUser());
                parseObject.put("quantity", numberPicker.getValue());
                parseObject.put("cal", dish.getCalories());
                parseObject.put("carb", dish.getCarbs());
                parseObject.put("measure", dish.getMeasure());
                parseObject.put("fat", dish.getFat());
                parseObject.put("fiber", dish.getFiber());
                parseObject.put("gram", dish.getGram());
                parseObject.put("image", dish.getImage());
                parseObject.put("description", numberPicker1.getValue() == 0 ? "grams" : dish.getDescription());
                parseObject.put("name", dish.getName());
                parseObject.put("protein", dish.getProtein());
                parseObject.put("meal", ((TextView) findViewById(R.id.tvMeal)).getText().toString());
                parseObject.put("CreatedAt", Calendar.getInstance().getTime());
                try {
                    parseObject.saveEventually();
                    parseObject.pin("today");
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                    e.printStackTrace();
                } finally {
                    Toast.makeText(AddFoodActivity.this, "Food item added", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }
        });
    }

    private void setCalorieData(Dialog dialog, Dish dish) {

        ImageView im = (ImageView) dialog.findViewById(R.id.imExercise1);
        im.setImageResource(Utils.getExerciseImageId(User.getExercise1(), true));

        im = (ImageView) dialog.findViewById(R.id.imExercise2);
        im.setImageResource(Utils.getExerciseImageId(User.getExercise2(), false));

        NumberPicker numberPickerQuantity = (NumberPicker) dialog.findViewById(R.id.numberPicker);
        NumberPicker numberPickerUnits = (NumberPicker) dialog.findViewById(R.id.numberPicker1);
        int description = numberPickerUnits.getValue();
        int quantity = numberPickerQuantity.getValue();

        float multiplier = 1f;
        if (description == 0) {
            try {
                multiplier = quantity / Float.parseFloat(dish.getGram());
            } catch (Exception e) {
            }
        } else {
            try {
                multiplier = quantity / Float.parseFloat(dish.getMeasure());
            } catch (Exception e) {
            }
        }

        int calories = (int) (Float.parseFloat(dish.getCalories()) * multiplier);

        TextView WalkCal = (TextView) dialog.findViewById(R.id.time1);
        TextView RunCal = (TextView) dialog.findViewById(R.id.time2);
        WalkCal.setText(Utils.getTimeForExercise(this, User.getExercise1(), calories) + "Min");
        RunCal.setText(Utils.getTimeForExercise(this, User.getExercise2(), calories) + "Min");
    }

    public void onLeftClick(View view) {
        changeMeal(-1);
    }

    public void onRightClick(View view) {
        changeMeal(1);
    }

    private void changeMeal(int delta) {
        TextView tv = (TextView) findViewById(R.id.tvMeal);
        String meal = (String) tv.getText();
        int requiredIndex = meals.indexOf(meal) + delta;
        requiredIndex += 3;
        requiredIndex %= 3;
        tv.setText(meals.get(requiredIndex));
        ImageView im = (ImageView) findViewById(R.id.imMeal);
        im.setImageResource(drawables[requiredIndex]);

        //change dishes
        dishes.clear();
        switch (requiredIndex) {
            case 0:
                dishes.addAll(dishesBreakfast);
                break;
            case 1:
                dishes.addAll(dishesLunch);
                break;
            case 2:
                dishes.addAll(dishesDinner);
                break;
        }
        rcAdapter.notifyDataSetChanged();
    }

    public void onSearchClick(View view) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("IndianDishes");
        String subString = ((EditText) findViewById(R.id.etSearch)).getText().toString().trim();
        query.whereContains("name", subString.toLowerCase());
        query.setLimit(30);
        List<ParseObject> lPo = new ArrayList<>();
        try {
            lPo = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        dishes.clear();
        for (ParseObject each : lPo) {
            dishes.add(new Dish(each));
        }
        rcAdapter.notifyDataSetChanged();
    }

    public void addNewItem(View view) {
        startActivity(new Intent(this, NewItem.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public class CardArrayRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> //adapter for recyclerview to load all listvenue
    {

        private List<Dish> foods;
        private LayoutInflater inflater = null;

        public CardArrayRecyclerViewAdapter(Context context, List<Dish> listDishes) {
            this.foods = listDishes;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CardListViewHolder(inflater.inflate(R.layout.addfood_item_cell, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            if (foods.get(position).getName().startsWith("Calories"))
                ((CardListViewHolder) holder).tvName.setText(Utils.toTitleCase(foods.get(position).getName().substring(12)));
            else
                ((CardListViewHolder) holder).tvName.setText(Utils.toTitleCase(foods.get(position).getName()));


            ((CardListViewHolder) holder).linearLayout.setOnClickListener(new View.OnClickListener() //open venuepage of particular foods
            {
                @Override
                public void onClick(View v) {
                    createDialog(position, 30, 50, null, null);
                    //listhotposition = position;
                }
            });

        }


        @Override
        public int getItemCount() {
            return foods.size();
        }

        private class CardListViewHolder extends RecyclerView.ViewHolder {
            public TextView tvName;
            public ImageView imageView;
            public View view;

            public LinearLayout linearLayout;

            public CardListViewHolder(View convertView) {
                super(convertView);
                view = convertView;
                imageView = (ImageView) convertView.findViewById(R.id.iv); //imageview of card
                tvName = (TextView) convertView.findViewById(R.id.foodName); //name of foods of card
                linearLayout = (LinearLayout) convertView.findViewById(R.id.linearLayout); //Linearlayout of whole card to open venuepage
            }
        }

    }

    private class MyCustomAdapter extends ArrayAdapter<Dish> {

        private ArrayList<Dish> dishes;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Dish> dishList) {
            super(context, textViewResourceId, dishList);
            this.dishes = dishList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.dish_info, null);

                holder = new ViewHolder();
                holder.name = (CheckBox) convertView.findViewById(R.id.cb);
                convertView.setTag(holder);

                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        Dish dish = (Dish) cb.getTag();
                        Toast.makeText(getApplicationContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();
                        dish.setSelected(cb.isChecked());
                    }
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Dish dish = dishes.get(position);
            holder.name.setText(dish.getName());
            holder.name.setChecked(dish.isSelected());
            holder.name.setTag(dish);

            return convertView;

        }

        private class ViewHolder {
            CheckBox name;
        }

    }

//    private void checkButtonClick() {
//
//
//        Button myButton = (Button) findViewById(R.id.findSelected);
//        myButton.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                StringBuffer responseText = new StringBuffer();
//                responseText.append("The following were selected...\n");
//
//                ArrayList<country> countryList = dataAdapter.countryList;
//                use for loop i=0 to < countryList.size(){
//                    Country country = countryList.get(i);
//                    if(country.isSelected()){
//                        responseText.append("\n" + country.getName());
//                    }
//                }
//
//                Toast.makeText(getApplicationContext(),
//                        responseText, Toast.LENGTH_LONG).show();
//
//            }
//        });
//
//    }


}
