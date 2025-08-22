package com.healthlinkteam.healthlink.repository;

import com.healthlinkteam.healthlink.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransactionRepository extends JpaRepository<Transaction, Long> { }