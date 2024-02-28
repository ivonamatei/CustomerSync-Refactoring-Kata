package codingdojo.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CustomerMatches {
    private List<Customer> duplicates = new ArrayList<>();
    private String matchTerm;
    private Customer customer;

    public void addDuplicate(Customer duplicate) {
        duplicates.add(duplicate);
    }

    public boolean hasDuplicates() {
        return !duplicates.isEmpty();
    }
}
