package com.syhb.project.repositories;

import com.syhb.project.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    Account findByCardnumber(String cardnumber);
    boolean existsByAccountnumber(String accountnumber);
    Account findByAccountnumber(String accountnumber);

}
