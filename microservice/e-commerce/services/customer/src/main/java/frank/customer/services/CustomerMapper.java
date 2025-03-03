package frank.customer.services;

import frank.customer.models.Customer;
import frank.customer.models.CustomerRequest;
import frank.customer.models.CustomerResponse;
import org.springframework.stereotype.Service;

@Service
public class CustomerMapper {

    public Customer toCustomer(CustomerRequest customerRequest){

        if (customerRequest == null){
            return null;
        }

        return Customer.builder()
                .name(customerRequest.name())
                .email(customerRequest.email())
                .address(customerRequest.address())
                .build();
    }

    public CustomerResponse fromCustomer(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getAddress()
        );
    }
}