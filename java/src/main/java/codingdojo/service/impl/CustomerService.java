package codingdojo.service.impl;

import codingdojo.repository.CustomerDataAccess;
import codingdojo.service.ICustomerService;

public class CustomerService implements ICustomerService {

    private final CustomerDataAccess customerDataAccess;

    public CustomerService(CustomerDataAccess customerDataAccess) {
        this.customerDataAccess = customerDataAccess;
    }
}
