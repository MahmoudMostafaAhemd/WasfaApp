package com.example.mahmouddiab.chatbotex;

import com.example.mahmouddiab.chatbotex.models.RecipeModel;

import java.util.List;

import io.paperdb.Book;
import io.paperdb.Paper;

/**
 * Created by mahmoud.diab on 5/13/2018.
 */

public class DataBase {
    private static final String Map_NAME = "_basket";
    private static DataBase instance;
    private final Book book;

    public static DataBase getInstance() {
        if (instance == null) {
            synchronized (DataBase.class) {
                instance = new DataBase();
            }
        }
        return instance;
    }

    public DataBase() {
        book = Paper.book(Map_NAME);
    }

    public void addToHistory(String key, RecipeModel recipeModel) {
        book.write(key, recipeModel);
    }

    public RecipeModel getItemFromHistory(String key) {
        return book.read(key, null);
    }

    public List<String> getAllItems() {
        return book.getAllKeys();
    }
}
