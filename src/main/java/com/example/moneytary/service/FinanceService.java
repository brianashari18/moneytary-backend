package com.example.moneytary.service;

import com.example.moneytary.entity.Pemasukan;
import com.example.moneytary.entity.Pengeluaran;
import com.example.moneytary.entity.Tabungan;
import com.example.moneytary.repository.PemasukanRepository;
import com.example.moneytary.repository.PengeluaranRepository;
import com.example.moneytary.repository.TabunganRepository;
import com.example.moneytary.dto.TabunganDataResponse;
import com.example.moneytary.dto.PemasukanDataResponse;
import com.example.moneytary.dto.PengeluaranDataResponse;
import com.example.moneytary.dto.PemasukanAddRequest;
import com.example.moneytary.dto.PengeluaranAddRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FinanceService {

    private static final Logger log = LoggerFactory.getLogger(FinanceService.class);

    @Autowired
    private ValidationService validationService;

    @Autowired
    private TabunganRepository tabunganRepository;

    @Autowired
    private PemasukanRepository pemasukanRepository;

    @Autowired
    private PengeluaranRepository pengeluaranRepository;

    @Transactional
    public PemasukanDataResponse addPemasukan(PemasukanAddRequest request) {
        validationService.validate(request);

        Tabungan tabungan = tabunganRepository.findById(1).orElseGet(() -> {
            Tabungan newTabungan = new Tabungan();
            newTabungan.setJumlah(0L);
            newTabungan.setCreatedAt(LocalDateTime.now());
            newTabungan.setUpdatedAt(LocalDateTime.now());
            return tabunganRepository.save(newTabungan);
        });

        Pemasukan pemasukan = new Pemasukan();
        pemasukan.setJumlah(request.getJumlah());
        pemasukan.setCreatedAt(LocalDateTime.now());
        pemasukan.setUpdatedAt(LocalDateTime.now());
        pemasukan.setTabungan(tabungan);

        pemasukanRepository.save(pemasukan);
        updateTabungan();

        return PemasukanDataResponse.builder()
                .id(pemasukan.getId())
                .jumlah(pemasukan.getJumlah())
                .createdAt(pemasukan.getCreatedAt())
                .updatedAt(pemasukan.getUpdatedAt())
                .tabunganId(pemasukan.getTabungan().getId())
                .build();
    }

    @Transactional
    public PengeluaranDataResponse addPengeluaran(PengeluaranAddRequest request) {
        validationService.validate(request);

        Tabungan tabungan = tabunganRepository.findById(1).orElseGet(() -> {
            Tabungan newTabungan = new Tabungan();
            newTabungan.setJumlah(0L);
            newTabungan.setCreatedAt(LocalDateTime.now());
            newTabungan.setUpdatedAt(LocalDateTime.now());
            return tabunganRepository.save(newTabungan);
        });

        if (request.getJumlah() > tabungan.getJumlah()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tabungan tidak cukup");
        }

        log.info("TEST SINI");


        Pengeluaran pengeluaran = new Pengeluaran();
        pengeluaran.setJumlah(request.getJumlah());
        pengeluaran.setTanggal(request.getTanggal());
        pengeluaran.setCreatedAt(LocalDateTime.now());
        pengeluaran.setUpdatedAt(LocalDateTime.now());
        pengeluaran.setTabungan(tabungan);

        pengeluaranRepository.save(pengeluaran);
        updateTabungan();

        return PengeluaranDataResponse.builder()
                .id(pengeluaran.getId())
                .jumlah(pengeluaran.getJumlah())
                .tanggal(pengeluaran.getTanggal())
                .createdAt(pengeluaran.getCreatedAt())
                .updatedAt(pengeluaran.getUpdatedAt())
                .tabunganId(pengeluaran.getTabungan().getId())
                .build();
    }

    public List<PengeluaranDataResponse> getRiwayat() {

        List<Pengeluaran> riwayatPengeluaran = pengeluaranRepository.findAll();
        return riwayatPengeluaran.stream().map(pengeluaran -> PengeluaranDataResponse.builder()
                .id(pengeluaran.getId())
                .jumlah(pengeluaran.getJumlah())
                .tanggal(pengeluaran.getTanggal())
                .createdAt(pengeluaran.getCreatedAt())
                .updatedAt(pengeluaran.getUpdatedAt())
                .build()).toList();
    }

    public TabunganDataResponse getTabungan() {
        Tabungan tabungan = tabunganRepository.findById(1).orElseGet(() -> {
            Tabungan newTabungan = new Tabungan();
            newTabungan.setJumlah(0L);
            newTabungan.setCreatedAt(LocalDateTime.now());
            newTabungan.setUpdatedAt(LocalDateTime.now());
            return tabunganRepository.save(newTabungan);
        });

        return TabunganDataResponse.builder()
                .id(tabungan.getId())
                .jumlah(tabungan.getJumlah())
                .createdAt(tabungan.getCreatedAt())
                .updatedAt(tabungan.getUpdatedAt())
                .build();
    }

    private void updateTabungan() {
        Tabungan tabungan = tabunganRepository.findById(1).orElseGet(() -> {
            Tabungan newTabungan = new Tabungan();
            newTabungan.setJumlah(0L);
            newTabungan.setCreatedAt(LocalDateTime.now());
            newTabungan.setUpdatedAt(LocalDateTime.now());
            return tabunganRepository.save(newTabungan);
        });
        Long totalPemasukan = pemasukanRepository.findAll().stream()
                .map(Pemasukan::getJumlah)
                .reduce(0L, Long::sum);
        Long totalPengeluaran = pengeluaranRepository.findAll().stream()
                .map(Pengeluaran::getJumlah)
                .reduce(0L, Long::sum);

        tabungan.setJumlah(totalPemasukan - totalPengeluaran);
        tabungan.setUpdatedAt(LocalDateTime.now());
        tabunganRepository.save(tabungan);
    }
}
