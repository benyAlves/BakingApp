package com.udacity.maluleque.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.udacity.maluleque.bakingapp.SharedPreferences;

public class RecipeViewService extends IntentService {

    public static final String ACTION_UPDATE_VIEW_RECIPE =
            "action.view_recipe";

    public RecipeViewService() {
        super("RecipeViewService");
    }

    public static void startActionUpdateViewRecipe(Context context) {
        Intent intent = new Intent(context, RecipeViewService.class);
        intent.setAction(ACTION_UPDATE_VIEW_RECIPE);
        context.startService(intent);

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_VIEW_RECIPE.equals(action)) {
                handleActionViewRecipe();
            }
        }
    }

    private void handleActionViewRecipe() {
        SharedPreferences.getInstance(this);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
        RecipeWidgetProvider.updateWidgets(this, appWidgetManager, appWidgetIds, SharedPreferences.getRecipeTitle(), SharedPreferences.getRecipeIngredients());
    }

}
