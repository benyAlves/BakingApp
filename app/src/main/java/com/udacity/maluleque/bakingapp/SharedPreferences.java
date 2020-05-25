package com.udacity.maluleque.bakingapp;

import android.content.Context;

public class SharedPreferences {

    public static final String RECIPE_TITLE = "recipe-title";
    public static final String RECIPE_INGREDIENTS = "recipe-ingredients";
    /**
     * PREFS_NAME is a file name which generates inside data folder of application
     */
    private static final String PREFS_NAME = "shared-pref-baking";
    private static android.content.SharedPreferences sp;
    private static android.content.SharedPreferences.Editor editor = null;

    private static SharedPreferences instance = null;

    public static SharedPreferences getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferences();
        }
        sp = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return instance;
    }

    public static void addLastViewRecipe(String recipeTitle, String recipeIngredients) {
        editor = sp.edit();
        editor.putString(RECIPE_TITLE, recipeTitle);
        editor.putString(RECIPE_INGREDIENTS, recipeIngredients);
        editor.apply();
    }


    public static String getRecipeTitle() {
        return sp.getString(RECIPE_TITLE, "No Recipe Selected");
    }

    public static String getRecipeIngredients() {
        return sp.getString(RECIPE_INGREDIENTS, "No Recipe Selected");
    }

    public static void clearData() {
        editor = sp.edit();
        editor.clear();
        editor.apply();
    }
}
