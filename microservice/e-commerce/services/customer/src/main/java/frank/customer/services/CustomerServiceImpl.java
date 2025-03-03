package frank.customer.services;

import frank.customer.exceptions.CustomerException;
import frank.customer.interfaces.CustomerService;
import frank.customer.models.Customer;
import frank.customer.models.CustomerRequest;
import frank.customer.models.CustomerResponse;
import frank.customer.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public String createCustomer(CustomerRequest customerRequest) {
        Customer savedCustomer = customerRepository.save(
                customerMapper.toCustomer(customerRequest)
        );
        return savedCustomer.getId();
    }

    @Override
    public void updateCustomer(CustomerRequest customerRequest) {
        Customer theCustomer = customerRepository.findById(customerRequest.id())
                .orElseThrow(() -> new CustomerException("Customer Not Found"));

        mergeCustomer(theCustomer, customerRequest);

        customerRepository.save(theCustomer);

    }

    @Override
    public List<CustomerResponse> findAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::fromCustomer)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean existsByTheId(String customerId) {
        return customerRepository.existsById(customerId);
        // or can use find by id and is present (true) : just like the video
    }

    @Override
    public CustomerResponse findByTheId(String customerId) {
        return customerRepository.findById(customerId)
                .map(customerMapper::fromCustomer)
                .orElseThrow(() ->new CustomerException("Customer not Found"));
    }

    @Override
    public void deleteByTheId(String customerId) {
        customerRepository.deleteById(customerId);
    }

    private void mergeCustomer(Customer theCustomer, CustomerRequest customerRequest) {

        Optional.ofNullable(customerRequest.name())
                .filter(StringUtils::isNotBlank)
                .ifPresent(theCustomer::setName);

        Optional.ofNullable(customerRequest.email())
                .filter(StringUtils::isNotBlank)
                .ifPresent(theCustomer::setEmail);

        Optional.ofNullable(customerRequest.address())
                .ifPresent(theCustomer::setAddress);

    }
}
