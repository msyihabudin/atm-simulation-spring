package com.syhb.project.repositories;

import com.syhb.project.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Customer findCustomerById(int id);

}
