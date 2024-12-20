package com.example.moneytary.repository;

import com.example.moneytary.entity.Pengeluaran;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PengeluaranRepository extends JpaRepository<Pengeluaran, Integer> {
}
