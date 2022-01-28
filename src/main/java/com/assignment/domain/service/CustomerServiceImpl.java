package com.assignment.domain.service;

import com.assignment.application.dto.CustomerDto;
import com.assignment.domain.model.Customer;
import com.assignment.domain.repository.CustomerRepository;
import java.util.Optional;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

  private final CustomerRepository customerRepository;
  private final InMemoryUserDetailsManager inMemoryUserDetailsManager;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Optional<Customer> getCustomerById(long customerId) {
    return customerRepository.findById(customerId);
  }

  @Override
  public Customer saveCustomer(CustomerDto customerDTO) {
    Customer customerToSave = Customer.builder()
        .name(customerDTO.getName())
        .age(customerDTO.getAge())
        .emailAddress(customerDTO.getEmailAddress())
        .password(passwordEncoder.encode(customerDTO.getPassword()))
        .build();

    customerRepository.save(customerToSave);
    inMemoryUserDetailsManager.createUser(User.withUsername(customerDTO.getEmailAddress())
        .password(passwordEncoder.encode(customerDTO.getPassword()))
        .roles("USER").build());

    return customerToSave;
  }

  @Override
  public void updateCustomer(long customerId, CustomerDto customerDTO) {
    Customer customerToUpdate = customerRepository.getById(customerId);
    customerToUpdate.setName(customerDTO.getName());
    customerToUpdate.setAge(customerDTO.getAge());
    customerToUpdate.setEmailAddress(customerDTO.getEmailAddress());
    customerToUpdate.setPassword(customerDTO.getPassword());

    inMemoryUserDetailsManager.updateUser(User.withUsername(customerDTO.getEmailAddress())
        .password(passwordEncoder.encode(customerDTO.getPassword()))
        .roles("USER")
        .build());
    customerRepository.save(customerToUpdate);
  }

  @Override
  public void deleteCustomer(long customerId) {
    Customer customer = customerRepository.getById(customerId);
    customerRepository.delete(customer);
    inMemoryUserDetailsManager.deleteUser(customer.getEmailAddress());
  }

  @Override
  public boolean isEmailValid(String emailAddress) {
    String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    return Pattern.compile(regexPattern)
        .matcher(emailAddress)
        .matches();
  }

  @Override
  public boolean customerExists(String emailAddress) {
    return customerRepository.findByEmailAddress(emailAddress).isPresent();
  }
}