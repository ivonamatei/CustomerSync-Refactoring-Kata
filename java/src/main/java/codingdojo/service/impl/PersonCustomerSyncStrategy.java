package codingdojo.service.impl;

import codingdojo.exception.ConflictException;
import codingdojo.model.Customer;
import codingdojo.model.CustomerMatches;
import codingdojo.model.CustomerType;
import codingdojo.model.ExternalCustomer;
import codingdojo.service.ISyncStrategy;

public class PersonCustomerSyncStrategy implements ISyncStrategy {

    private final CustomerService customerService;

    public PersonCustomerSyncStrategy(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public CustomerMatches load(ExternalCustomer externalCustomer) {
        final String externalId = externalCustomer.getExternalId();

        CustomerMatches customerMatches = customerService.loadPersonCustomer(externalId);

        if (customerMatches.getCustomer() != null) {
            if (!CustomerType.PERSON.equals(customerMatches.getCustomer().getCustomerType())) {
                throw new ConflictException("Existing customer for externalCustomer " + externalId + " already exists and is not a person");
            }

            if (!"ExternalId".equals(customerMatches.getMatchTerm())) {
                Customer customer = customerMatches.getCustomer();
                customer.setExternalId(externalId);
                customer.setMasterExternalId(externalId);
            }
        }
        return customerMatches;
    }
}
