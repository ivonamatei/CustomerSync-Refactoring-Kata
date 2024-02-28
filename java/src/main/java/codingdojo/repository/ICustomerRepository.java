package codingdojo.repository;

import codingdojo.model.Customer;
import codingdojo.model.ShoppingList;

public interface ICustomerRepository {
    Customer updateCustomerRecord(Customer customer);

    Customer createCustomerRecord(Customer customer);

    void updateShoppingList(ShoppingList consumerShoppingList);

    Customer findByExternalId(String externalId);

    Customer findByMasterExternalId(String externalId);

    Customer findByCompanyNumber(String companyNumber);
}
