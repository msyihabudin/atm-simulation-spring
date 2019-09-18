package com.syhb.project.repositories;

import com.syhb.project.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    Transaction findByAccountnumber(String accountNumber);

}
