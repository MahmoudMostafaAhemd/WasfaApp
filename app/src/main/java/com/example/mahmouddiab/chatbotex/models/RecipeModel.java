package com.example.mahmouddiab.chatbotex.models;

/**
 * Created by mahmoud.diab on 5/13/2018.
 */

public class RecipeModel {
    private String imgUrl;
    private String recipeTitle;
    private String recipe;
    private String recipeContent;
    private boolean isSuperMama;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public String getRecipeContent() {
        return recipeContent;
    }

    public void setRecipeContent(String recipeContent) {
        this.recipeContent = recipeContent;
    }

    public boolean isSuperMama() {
        return isSuperMama;
    }

    public void setSuperMama(boolean superMama) {
        isSuperMama = superMama;
    }
}
