package codingdojo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(exclude = {"address", "preferredStore", "shoppingLists", "internalId", "name", "customerType"})
public class Customer {
    private String externalId;
    private String masterExternalId;
    private Address address;
    private String preferredStore;
    private Integer bonusPointsBalance;
    private List<ShoppingList> shoppingLists = new ArrayList<>();
    private String internalId;
    private String name;
    private CustomerType customerType;
    private String companyNumber;

    public void addShoppingList(ShoppingList consumerShoppingList) {
        ArrayList<ShoppingList> newList = new ArrayList<>(this.shoppingLists);
        newList.add(consumerShoppingList);
        this.setShoppingLists(newList);
    }
}
