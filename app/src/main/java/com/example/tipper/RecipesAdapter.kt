package com.example.tipper

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.myapplication.R
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class RecipesAdapter(context: Context, recipes: List<Recipe>, res: Resources?) :
    ArrayAdapter<Recipe?>(context, 0, recipes) {
    var res: Resources? = null
    var assets: AssetManager? = null

    init {
        this.res = res
        assets = context.assets
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val recipe = getItem(position)
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false)
        }
        val tvName = convertView!!.findViewById<View>(R.id.tvRecipeName) as TextView
        val tvText = convertView.findViewById<View>(R.id.tvRecipeText) as TextView
        val imgRecipe = convertView.findViewById<View>(R.id.recipeImageView) as ImageView
        tvName.text = recipe!!.recipeName
        try {
            val ims = assets!!.open(recipe.recipeName + ".jpg")
            val d = Drawable.createFromStream(ims, null)
            imgRecipe.setImageDrawable(d)
            ims.close()
        } catch (ignored: IOException) {
        }
        var builtRecipeText = ""
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(
                InputStreamReader(assets!!.open(recipe.recipeName + ".txt"))
            )

            // czytanie linijka po linijce
            var mLine: String
            while (reader.readLine().also { mLine = it } != null) {
                builtRecipeText += """
                    $mLine
                    
                    """.trimIndent()
            }
        } catch (_: IOException) {
        } finally {
            if (reader != null) {
                try {
                    reader.close()
                } catch (_: IOException) {
                }
            }
        }
        tvText.text = builtRecipeText
        return convertView
    }
}