package com.example.moneytary.repository;

import com.example.moneytary.entity.Tabungan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TabunganRepository extends JpaRepository<Tabungan, Integer> {
}
