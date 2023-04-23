package com.example.tipper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentRecipesBinding
import java.io.IOException

class Recipes : Fragment() {
    // wywołane przy tworzeniu
    private var binding: FragmentRecipesBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecipesBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listViewRecipes = binding!!.listViewRecipes
        val arrayOfRecipes = ArrayList<Recipe>()
        val adapter = context?.let { RecipesAdapter(it, arrayOfRecipes, resources) }
        listViewRecipes.adapter = adapter
        val recipesNamesList: MutableList<String> = ArrayList()
        try {
            val assetsContent = requireContext().assets.list("")
            for (assetsItem in assetsContent!!) {
                if (assetsItem.contains(".txt")) {
                    val recipeName = assetsItem.substring(0, assetsItem.indexOf(".txt"))
                    if (!recipesNamesList.contains(recipeName)) {
                        recipesNamesList.add(recipeName)
                    }
                }
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        for (i in recipesNamesList.indices) {
            val newRecipe = Recipe(recipesNamesList[i])
            adapter?.add(newRecipe)
        }
        adapter?.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}