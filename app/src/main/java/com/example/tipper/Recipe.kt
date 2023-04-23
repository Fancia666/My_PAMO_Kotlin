package com.example.tipper

class Recipe(@JvmField var recipeName: String) {
    var recipeText: String
    var Image: String

    init {
        recipeText = recipeName
        Image = recipeName
    }
}