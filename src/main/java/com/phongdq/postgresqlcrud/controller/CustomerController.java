package com.phongdq.postgresqlcrud.controller;

import com.phongdq.postgresqlcrud.dto.CustomerDTO;
import com.phongdq.postgresqlcrud.exception.CustomerNotFoundException;
import com.phongdq.postgresqlcrud.model.Customer;
import com.phongdq.postgresqlcrud.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    CustomerRepository repository;

    @GetMapping("/bulkcreate")
    public String bulkCreate() {
        repository.save(new Customer("Phong", "Duong"));

        repository.saveAll(Arrays.asList(
                new Customer("Victoria", "Sheppard"),
                new Customer("Esha", "Prince"),
                new Customer("Malaika", "Mclean")));
        return "Customers is created";
    }

    @GetMapping
    public List<CustomerDTO> findAll() {
        List<Customer> customers = repository.findAll();
        List<CustomerDTO> customerDTOS = new ArrayList<>();
        for (Customer c : customers) {
            customerDTOS.add(new CustomerDTO(c.getFirstName(), c.getLastName()));
        }
        return customerDTOS;
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody CustomerDTO customerDTO) {
        Customer customer = repository.save(new Customer(customerDTO.getFirstName(), customerDTO.getLastName()));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(customer.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public CustomerDTO getById(@PathVariable long id) {
        Optional<Customer> customer = repository.findById(id);
        if (!customer.isPresent()) {
            throw new CustomerNotFoundException("not found id-" + id);
        }
        Customer c = customer.get();
        return new CustomerDTO(c.getFirstName(), c.getLastName());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> update(@PathVariable long id, @RequestBody CustomerDTO param) {
        return repository.findById(id).map(customer -> {
            customer.setFirstName(param.getFirstName());
            customer.setLastName(param.getLastName());
            Customer updated = repository.save(customer);
            return ResponseEntity.ok().body(new CustomerDTO(updated.getFirstName(), updated.getLastName()));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        return repository.findById(id).map(customer -> {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public List<CustomerDTO> getByFirstName(@RequestParam(value = "firstName") String firstName) {
        List<Customer> customers = repository.findByFirstName(firstName);
        List<CustomerDTO> customerDTOS = new ArrayList<>();
        for (Customer c : customers) {
            customerDTOS.add(new CustomerDTO(c.getFirstName(), c.getLastName()));
        }
        return customerDTOS;
    }
}
