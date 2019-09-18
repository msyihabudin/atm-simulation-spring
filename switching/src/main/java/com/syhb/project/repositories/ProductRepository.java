package com.syhb.project.repositories;

import com.syhb.project.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Product findByReferencenumber(String referenceNumber);
    boolean existsByReferencenumber(String referenceNumber);

}
