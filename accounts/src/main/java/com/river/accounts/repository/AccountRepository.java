package com.river.accounts.repository;

import com.river.accounts.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    Optional<Account> findByCustomerId(Integer customerId);

    @Modifying
    void deleteByCustomerId(Integer customerId);

}
