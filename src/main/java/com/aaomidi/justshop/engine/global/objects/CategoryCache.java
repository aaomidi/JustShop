package com.aaomidi.justshop.engine.global.objects;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class CategoryCache implements Iterable<JSCategory> {
    private List<JSCategory> categories = new ArrayList<>();
    private HashMap<Integer, JSCategory> categoryIDs = new HashMap<>();

    public JSCategory getCategory(int i) {
        return categories.get(i);
    }

    public JSCategory getCategoryByID(Integer i) {
        return categoryIDs.get(i);
    }

    @Override
    public Iterator<JSCategory> iterator() {
        return categories.iterator();
    }

    public void add(JSCategory jsCategory) {
        if (categories.contains(jsCategory)) {
            return;
        }
        if (jsCategory.getId() == -1 || jsCategory.getIcon() == null || jsCategory.getName() == null || jsCategory.getLore() == null) {
            throw new Error("Unrecoverable error occurred when saving the category with the ID: " + jsCategory.getId());
        }
        categories.add(jsCategory);
        categoryIDs.put(jsCategory.getId(), jsCategory);
    }

    public List<JSCategory> getCategories() {
        return categories;
    }
}
