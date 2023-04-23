package com.example.tipper;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.myapplication.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
public class RecipesAdapter extends ArrayAdapter<Recipe> {
    Resources res = null;
    AssetManager assets = null;
    public RecipesAdapter(Context context, ArrayList<Recipe> recipes, Resources res) {
        super(context, 0, recipes);
        this.res = res;
        this.assets = context.getAssets();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Recipe recipe = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_recipe, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.tvRecipeName);
        TextView tvText = (TextView) convertView.findViewById(R.id.tvRecipeText);
        ImageView imgRecipe = (ImageView) convertView.findViewById(R.id.recipeImageView);

        tvName.setText(recipe.recipeName);
        try
        {
            InputStream ims = assets.open(recipe.recipeName + ".jpg");

            Drawable d = Drawable.createFromStream(ims, null);

            imgRecipe.setImageDrawable(d);
            ims .close();
        }
        catch(IOException ignored)
        {}

        String builtRecipeText = "";

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(assets.open(recipe.recipeName + ".txt")));

            // czytanie linijka po linijce
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                builtRecipeText += mLine + "\n";
            }
        } catch (IOException e) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }

        tvText.setText(builtRecipeText);

        return convertView;
    }
}