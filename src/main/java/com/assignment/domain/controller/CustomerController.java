package com.assignment.domain.controller;

import com.assignment.application.dto.CustomerDto;
import com.assignment.domain.model.Customer;
import com.assignment.domain.service.CustomerService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/customer")
public class CustomerController {

  private final CustomerService customerService;

  @GetMapping({"/{customerId}"})
  public ResponseEntity<Customer> getCustomer(@PathVariable("customerId") Integer customerId) {
    Optional<Customer> customerByIdOpt = customerService.getCustomerById(customerId);
    return customerByIdOpt.map(customer -> new ResponseEntity<>(customer, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
  }

  @PostMapping("/addCustomer")
  public ResponseEntity<Void> handleSave(@RequestBody CustomerDto customerDTO) {
    if (customerService.customerExists(customerDTO.getEmailAddress())) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    if (!customerService.isEmailValid(customerDTO.getEmailAddress())) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    Customer savedCustomer = customerService.saveCustomer(customerDTO);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Saved-Customer-Id: ", String.valueOf(savedCustomer.getId()));

    return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
  }

  @PutMapping("/updateCustomer")
  public ResponseEntity<Void> handleUpdate(@PathVariable("customerId") Integer customerId,
      @RequestBody CustomerDto customerDTO) {
    if (!customerService.customerExists(customerDTO.getEmailAddress())) {
      new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    customerService.updateCustomer(customerId, customerDTO);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping({"/{customerId}"})
  public ResponseEntity<String> deleteCustomer(@PathVariable("customerId") Integer customerId) {
    customerService.deleteCustomer(customerId);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
