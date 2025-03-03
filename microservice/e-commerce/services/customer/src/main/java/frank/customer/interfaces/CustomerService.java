package frank.customer.interfaces;

import frank.customer.models.CustomerRequest;
import frank.customer.models.CustomerResponse;

import java.util.List;

public interface CustomerService {

    String createCustomer(CustomerRequest customerRequest);

    void updateCustomer(CustomerRequest customerRequest);

    List<CustomerResponse> findAllCustomers();

    Boolean existsByTheId(String customerId);

    CustomerResponse findByTheId(String customerId);

    void deleteByTheId(String customerId);
}
