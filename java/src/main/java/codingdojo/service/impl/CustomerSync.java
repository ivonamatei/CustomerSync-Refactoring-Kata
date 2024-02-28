package codingdojo.service.impl;

import codingdojo.model.Customer;
import codingdojo.model.CustomerMatches;
import codingdojo.model.ExternalCustomer;
import codingdojo.repository.ICustomerRepository;
import codingdojo.service.ISyncStrategy;

public class CustomerSync {

    private final CustomerService customerService;

    public CustomerSync(ICustomerRepository customerRepository) {
        this(new CustomerService(customerRepository));
    }

    public CustomerSync(CustomerService customerService) {
        this.customerService = customerService;
    }

    public boolean syncWithDataLayer(ExternalCustomer externalCustomer) {
        ISyncStrategy strategy = externalCustomer.isCompany() ? new CompanyCustomerSyncStrategy(customerService) : new PersonCustomerSyncStrategy(customerService);
        CustomerMatches customerMatches = strategy.load(externalCustomer);

        Customer customer = customerService.prepareCustomer(externalCustomer, customerMatches);

        boolean created = false;
        if (customer.getInternalId() == null) {
            customer = customerService.createCustomerRecord(customer);
            created = true;
        } else {
            customerService.updateCustomerRecord(customer);
        }
        updateCustomerDetails(externalCustomer, customerMatches, customer);
        return created;
    }

    private void updateCustomerDetails(ExternalCustomer externalCustomer, CustomerMatches customerMatches, Customer customer) {
        customerService.updateRelations(externalCustomer, customer);
        customerService.updateDuplicates(externalCustomer, customerMatches);
    }

}
