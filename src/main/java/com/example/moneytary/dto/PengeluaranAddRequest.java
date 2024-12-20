package com.example.moneytary.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PengeluaranAddRequest {

    @NotNull(message = "Jumlah tidak boleh null")
    @Min(value = 1, message = "Jumlah harus lebih besar dari atau sama dengan 1")
    private Long jumlah;

    @NotNull(message = "Tanggal tidak boleh null")
    private LocalDate tanggal;
}
