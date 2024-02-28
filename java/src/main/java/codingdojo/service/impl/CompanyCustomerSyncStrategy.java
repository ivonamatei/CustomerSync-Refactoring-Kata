package codingdojo.service.impl;

import codingdojo.exception.ConflictException;
import codingdojo.model.Customer;
import codingdojo.model.CustomerMatches;
import codingdojo.model.CustomerType;
import codingdojo.model.ExternalCustomer;
import codingdojo.repository.CustomerDataAccess;
import codingdojo.service.ISyncStrategy;

public class CompanyCustomerSyncStrategy implements ISyncStrategy {
    private final CustomerDataAccess customerDataAccess;

    public CompanyCustomerSyncStrategy(CustomerDataAccess customerDataAccess) {
        this.customerDataAccess = customerDataAccess;
    }

    @Override
    public CustomerMatches load(ExternalCustomer externalCustomer) {

        final String externalId = externalCustomer.getExternalId();
        final String companyNumber = externalCustomer.getCompanyNumber();

        CustomerMatches customerMatches = customerDataAccess.loadCompanyCustomer(externalId, companyNumber);

        if (customerMatches.getCustomer() != null && !CustomerType.COMPANY.equals(customerMatches.getCustomer().getCustomerType())) {
            throw new ConflictException("Existing customer for externalCustomer " + externalId + " already exists and is not a company");
        }

        if ("ExternalId".equals(customerMatches.getMatchTerm())) {
            String customerCompanyNumber = customerMatches.getCustomer().getCompanyNumber();
            if (!companyNumber.equals(customerCompanyNumber)) {
                customerMatches.getCustomer().setMasterExternalId(null);
                customerMatches.addDuplicate(customerMatches.getCustomer());
                customerMatches.setCustomer(null);
                customerMatches.setMatchTerm(null);
            }
        } else if ("CompanyNumber".equals(customerMatches.getMatchTerm())) {
            String customerExternalId = customerMatches.getCustomer().getExternalId();
            if (customerExternalId != null && !externalId.equals(customerExternalId)) {
                throw new ConflictException("Existing customer for externalCustomer " + companyNumber + " doesn't match external id " + externalId + " instead found " + customerExternalId);
            }
            Customer customer = customerMatches.getCustomer();
            customer.setExternalId(externalId);
            customer.setMasterExternalId(externalId);
            customerMatches.addDuplicate(customer);
        }
        return customerMatches;
    }
}
