package codingdojo.model;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class ShoppingList {
    private final List<String> products;
    public ShoppingList(String... products) {
        this.products = Arrays.asList(products);
    }

}
