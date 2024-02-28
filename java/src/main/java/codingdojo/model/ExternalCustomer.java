package codingdojo.model;

import lombok.Data;

import java.util.List;

@Data
public class ExternalCustomer {
    private Address address;
    private String name;
    private String preferredStore;
    //Using wrapper class to differentiate between null (non-applicable bonus points) and 0 (no remaining bonus points)
    private Integer bonusPointsBalance;
    private List<ShoppingList> shoppingLists;
    private String externalId;
    private String companyNumber;

    public boolean isCompany() {
        return companyNumber != null;
    }

}
