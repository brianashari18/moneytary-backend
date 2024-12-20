package com.example.moneytary.controller;

import com.example.moneytary.dto.*;
import com.example.moneytary.service.FinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tabungan")
@CrossOrigin(origins = "http://localhost:5173")
public class FinanceController {

    @Autowired
    private FinanceService financeService;

    @PostMapping(
            path = "/pemasukan",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<PemasukanDataResponse> addPemasukan(@RequestBody PemasukanAddRequest request) {
        PemasukanDataResponse pemasukanDataResponse = financeService.addPemasukan(request);
        return WebResponse.<PemasukanDataResponse>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .data(pemasukanDataResponse)
                .build();
    }

    @PostMapping(
            path = "/pengeluaran",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<PengeluaranDataResponse> addPengeluaran(@RequestBody PengeluaranAddRequest request) {
        PengeluaranDataResponse pengeluaranDataResponse = financeService.addPengeluaran(request);
        return WebResponse.<PengeluaranDataResponse>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .data(pengeluaranDataResponse)
                .build();
    }

    @GetMapping(
            path = "/pengeluaran",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<PengeluaranDataResponse>> getRiwayat() {
        List<PengeluaranDataResponse> riwayatPengeluaran = financeService.getRiwayat();
        return WebResponse.<List<PengeluaranDataResponse>>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .data(riwayatPengeluaran)
                .build();
    }

    @GetMapping(
            path = "/",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TabunganDataResponse> getTabungan() {
        TabunganDataResponse tabunganDataResponse = financeService.getTabungan();
        return WebResponse.<TabunganDataResponse>builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .data(tabunganDataResponse)
                .build();
    }
}

