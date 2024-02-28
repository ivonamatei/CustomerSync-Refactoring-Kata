package codingdojo.service;

import codingdojo.model.Customer;
import codingdojo.model.CustomerMatches;
import codingdojo.model.ExternalCustomer;
import codingdojo.model.ShoppingList;

public interface ICustomerService {
    CustomerMatches loadCompanyCustomer(String externalId, String companyNumber);

    CustomerMatches loadPersonCustomer(String externalId);

    Customer createCustomerRecord(Customer customer);

    Customer updateCustomerRecord(Customer customer);

    void updateShoppingList(Customer customer, ShoppingList consumerShoppingList);

    void updateDuplicates(ExternalCustomer externalCustomer, CustomerMatches customerMatches);

    void updateRelations(ExternalCustomer externalCustomer, Customer customer);

    Customer prepareCustomer(ExternalCustomer externalCustomer, CustomerMatches customerMatches);

}
