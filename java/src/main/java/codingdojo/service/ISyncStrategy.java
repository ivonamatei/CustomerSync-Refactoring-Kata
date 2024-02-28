package codingdojo.service;

import codingdojo.model.CustomerMatches;
import codingdojo.model.ExternalCustomer;

public interface ISyncStrategy {
    CustomerMatches load(ExternalCustomer externalCustomer);
}
