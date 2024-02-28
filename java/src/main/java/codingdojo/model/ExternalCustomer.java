package codingdojo.model;

import lombok.Data;

import java.util.List;

@Data
public class ExternalCustomer {
    private Address address;
    private String name;
    private String preferredStore;
    //using wrapper class in order to distinct from null (not applicable bonus points) and 0 ( no bonus points left)
    private Integer bonusPointsBalance;
    private List<ShoppingList> shoppingLists;
    private String externalId;
    private String companyNumber;

    public boolean isCompany() {
        return companyNumber != null;
    }

}
