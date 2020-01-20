package com.phongdq.postgresqlcrud.repository;

import com.phongdq.postgresqlcrud.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
  List<Customer> findByFirstName(String firstName);

  List<Customer> findAll();
}
