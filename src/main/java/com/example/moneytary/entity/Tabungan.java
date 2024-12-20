package com.example.moneytary.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tabungan")
public class Tabungan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Long jumlah;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "tabungan", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Pemasukan> pemasukan;

    @OneToMany(mappedBy = "tabungan", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Pengeluaran> pengeluaran;

}
