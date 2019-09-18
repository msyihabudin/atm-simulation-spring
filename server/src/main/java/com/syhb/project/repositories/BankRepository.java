package com.syhb.project.repositories;

import com.syhb.project.models.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends JpaRepository<Bank, Integer> {

    Bank findById(int id);
    Bank findByName(String name);

}
