package com.example.tipper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.myapplication.databinding.FragmentRecipesBinding;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class Recipes extends Fragment {

    // wywołane przy tworzeniu
    private FragmentRecipesBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentRecipesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listViewRecipes = binding.listViewRecipes;
        ArrayList<Recipe> arrayOfRecipes = new ArrayList<Recipe>();

        RecipesAdapter adapter = new RecipesAdapter(getContext(), arrayOfRecipes, getResources());
        listViewRecipes.setAdapter(adapter);

        List<String> recipesNamesList = new ArrayList<String>();
        try {
            String[] assetsContent = getContext().getAssets().list("");
            for (String assetsItem : assetsContent) {
                System.out.println(assetsItem);
                if(assetsItem.contains(".txt")){
                    String recipeName = assetsItem.substring(0,assetsItem.indexOf(".txt"));
                    if(!recipesNamesList.contains(recipeName)){
                        recipesNamesList.add(recipeName);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0 ; i < recipesNamesList.size(); i++){
            Recipe newRecipe = new Recipe(recipesNamesList.get(i));
            adapter.add(newRecipe);
        }

        adapter.notifyDataSetChanged();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}