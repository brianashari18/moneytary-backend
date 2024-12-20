package com.example.moneytary.repository;

import com.example.moneytary.entity.Pemasukan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PemasukanRepository extends JpaRepository<Pemasukan, Integer> {

}
