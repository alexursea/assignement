package com.assignment.domain.service;

import com.assignment.domain.model.Customer;
import com.assignment.application.dto.CustomerDto;
import java.util.Optional;

public interface CustomerService {

  Optional<Customer> getCustomerById(long customerId);
  Customer saveCustomer(CustomerDto customer);
  void updateCustomer(long customerId, CustomerDto customer);
  void deleteCustomer(long customerId);
  boolean isEmailValid(String email);
  boolean customerExists(String customerId);

}
