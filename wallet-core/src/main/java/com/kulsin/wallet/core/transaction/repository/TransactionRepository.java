package com.kulsin.wallet.core.transaction.repository;

import com.kulsin.wallet.core.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByPlayerId(Long playerId);

}
