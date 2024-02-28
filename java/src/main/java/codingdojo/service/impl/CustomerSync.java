package codingdojo.service.impl;

import codingdojo.model.*;
import codingdojo.repository.CustomerDataAccess;
import codingdojo.repository.CustomerDataLayer;
import codingdojo.service.ISyncStrategy;

import java.util.List;

public class CustomerSync {

    private final CustomerDataAccess customerDataAccess;
    private ISyncStrategy syncStrategy;

    public CustomerSync(CustomerDataLayer customerDataLayer) {
        this(new CustomerDataAccess(customerDataLayer));
    }

    public CustomerSync(CustomerDataAccess db) {
        this.customerDataAccess = db;
    }

    public boolean syncWithDataLayer(ExternalCustomer externalCustomer) {

        ISyncStrategy strategy;
        if (externalCustomer.isCompany()) {
            strategy = new CompanyCustomerSyncStrategy(customerDataAccess);
        } else {
            strategy = new PersonCustomerSyncStrategy(customerDataAccess);
        }

        CustomerMatches customerMatches = strategy.load(externalCustomer);
        Customer customer = prepareCustomer(externalCustomer, customerMatches);

        boolean created = false;
        if (customer.getInternalId() == null) {
            customer = createCustomer(customer);
            created = true;
        } else {
            updateCustomer(customer);
        }
        updateCustomerDetails(externalCustomer, customerMatches, customer);

        return created;
    }

    private void updateCustomerDetails(ExternalCustomer externalCustomer, CustomerMatches customerMatches, Customer customer) {
        updateContactInfo(externalCustomer, customer);
        updateRelations(externalCustomer, customer);
        updatePreferredStore(externalCustomer, customer);
        updateDuplicates(externalCustomer, customerMatches);
    }

    private Customer prepareCustomer(ExternalCustomer externalCustomer, CustomerMatches customerMatches) {
        Customer customer = customerMatches.getCustomer();
        if (customer == null) {
            customer = new Customer();
            customer.setExternalId(externalCustomer.getExternalId());
            customer.setMasterExternalId(externalCustomer.getExternalId());
        }
        customer.setName(externalCustomer.getName());
        if (externalCustomer.isCompany()) {
            customer.setCompanyNumber(externalCustomer.getCompanyNumber());
            customer.setCustomerType(CustomerType.COMPANY);
        } else {
            customer.setCustomerType(CustomerType.PERSON);
            if (externalCustomer.getBonusPointsBalance() != null && !externalCustomer.getBonusPointsBalance().equals(customer.getBonusPointsBalance())) {
                customer.setBonusPointsBalance(externalCustomer.getBonusPointsBalance());
            }
        }
        return customer;
    }

    private void updateDuplicates(ExternalCustomer externalCustomer, CustomerMatches customerMatches) {
        if (customerMatches.hasDuplicates()) {
            for (Customer duplicate : customerMatches.getDuplicates()) {
                updateDuplicate(externalCustomer, duplicate);
            }
        }
    }

    private void updateRelations(ExternalCustomer externalCustomer, Customer customer) {
        List<ShoppingList> consumerShoppingLists = externalCustomer.getShoppingLists();
        for (ShoppingList consumerShoppingList : consumerShoppingLists) {
            this.customerDataAccess.updateShoppingList(customer, consumerShoppingList);
        }
    }

    private Customer updateCustomer(Customer customer) {
        return this.customerDataAccess.updateCustomerRecord(customer);
    }

    private void updateDuplicate(ExternalCustomer externalCustomer, Customer duplicate) {
        duplicate.setName(externalCustomer.getName());
        if (duplicate.getInternalId() == null) {
            createCustomer(duplicate);
        } else {
            updateCustomer(duplicate);
        }
    }

    private void updatePreferredStore(ExternalCustomer externalCustomer, Customer customer) {
        customer.setPreferredStore(externalCustomer.getPreferredStore());
    }

    private Customer createCustomer(Customer customer) {
        return this.customerDataAccess.createCustomerRecord(customer);
    }

    private void updateContactInfo(ExternalCustomer externalCustomer, Customer customer) {
        customer.setAddress(externalCustomer.getAddress());
    }
}
