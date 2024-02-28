package codingdojo.service.impl;

import codingdojo.model.*;
import codingdojo.repository.ICustomerRepository;
import codingdojo.service.ICustomerService;

import java.util.List;

public class CustomerService implements ICustomerService {

    private final ICustomerRepository customerRepository;

    public CustomerService(ICustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerMatches loadCompanyCustomer(String externalId, String companyNumber) {
        CustomerMatches matches = new CustomerMatches();
        Customer matchByExternalId = this.customerRepository.findByExternalId(externalId);
        if (matchByExternalId != null) {
            matches.setCustomer(matchByExternalId);
            matches.setMatchTerm("ExternalId");
            Customer matchByMasterId = this.customerRepository.findByMasterExternalId(externalId);
            if (matchByMasterId != null) {
                matches.addDuplicate(matchByMasterId);
            }
        } else {
            Customer matchByCompanyNumber = this.customerRepository.findByCompanyNumber(companyNumber);
            if (matchByCompanyNumber != null) {
                matches.setCustomer(matchByCompanyNumber);
                matches.setMatchTerm("CompanyNumber");
            }
        }
        return matches;
    }

    @Override
    public CustomerMatches loadPersonCustomer(String externalId) {
        CustomerMatches matches = new CustomerMatches();
        Customer matchByPersonalNumber = this.customerRepository.findByExternalId(externalId);
        matches.setCustomer(matchByPersonalNumber);
        if (matchByPersonalNumber != null) {
            matches.setMatchTerm("ExternalId");
        }
        return matches;
    }

    @Override
    public Customer createCustomerRecord(Customer customer) {
        return customerRepository.createCustomerRecord(customer);
    }

    @Override
    public Customer updateCustomerRecord(Customer customer) {
        return customerRepository.updateCustomerRecord(customer);
    }

    @Override
    public void updateShoppingList(Customer customer, ShoppingList consumerShoppingList) {
        customer.addShoppingList(consumerShoppingList);
        customerRepository.updateShoppingList(consumerShoppingList);
        customerRepository.updateCustomerRecord(customer);
    }

    @Override
    public void updateDuplicates(ExternalCustomer externalCustomer, CustomerMatches customerMatches) {
        if (customerMatches.hasDuplicates()) {
            for (Customer duplicate : customerMatches.getDuplicates()) {
                duplicate.setName(externalCustomer.getName());
                if (duplicate.getInternalId() == null) {
                    customerRepository.createCustomerRecord(duplicate);
                } else {
                    customerRepository.updateCustomerRecord(duplicate);
                }
            }
        }
    }

    @Override
    public void updateRelations(ExternalCustomer externalCustomer, Customer customer) {
        List<ShoppingList> consumerShoppingLists = externalCustomer.getShoppingLists();
        for (ShoppingList consumerShoppingList : consumerShoppingLists) {
            updateShoppingList(customer, consumerShoppingList);
        }
    }

    @Override
    public Customer prepareCustomer(ExternalCustomer externalCustomer, CustomerMatches customerMatches) {
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
        customer.setAddress(externalCustomer.getAddress());
        customer.setPreferredStore(externalCustomer.getPreferredStore());
        return customer;
    }
}
