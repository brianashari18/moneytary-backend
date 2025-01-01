package com.example.moneytary.controller;

import com.example.moneytary.dto.PemasukanAddRequest;
import com.example.moneytary.dto.PengeluaranAddRequest;
import com.example.moneytary.dto.WebResponse;
import com.example.moneytary.entity.Tabungan;
import com.example.moneytary.repository.PemasukanRepository;
import com.example.moneytary.repository.PengeluaranRepository;
import com.example.moneytary.repository.TabunganRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FinanceControllerTest {

    private static final Logger log = LoggerFactory.getLogger(FinanceControllerTest.class);
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TabunganRepository tabunganRepository;

    @Autowired
    private PemasukanRepository pemasukanRepository;

    @Autowired
    private PengeluaranRepository pengeluaranRepository;

    @BeforeEach
    void setUp() {
        pemasukanRepository.deleteAll();
        pengeluaranRepository.deleteAll();
    }

    @Test
    void addPemasukanSuccess() throws Exception {
        PemasukanAddRequest request = PemasukanAddRequest.builder()
                .jumlah(10000L)
                .build();

        mockMvc.perform(
                post("/api/tabungan/pemasukan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<?> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertNotNull(response.getData());

            Tabungan tabungan = tabunganRepository.findAll().getFirst();
            log.info("tabungan: {}", tabungan.getJumlah());
            assertNotNull(tabungan);
            assertEquals(10000L, tabungan.getJumlah());
        });
    }

    @Test
    void addPemasukanInvalidData() throws Exception {
        PemasukanAddRequest request = PemasukanAddRequest.builder()
                .jumlah(-10000L)
                .build();

        mockMvc.perform(
                post("/api/tabungan/pemasukan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<?> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getErrors());
            assertEquals("jumlah: Jumlah harus lebih besar dari atau sama dengan 1", response.getErrors());
        });
    }


    @Test
    void addPengeluaranSuccess() throws Exception {

        PengeluaranAddRequest request = PengeluaranAddRequest.builder()
                .jumlah(5000L)
                .tanggal(LocalDate.now())
                .build();

        mockMvc.perform(
                post("/api/tabungan/pengeluaran")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<?> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertNotNull(response.getData());

            Tabungan updatedTabungan = tabunganRepository.findAll().getFirst();
            log.info("tabungan: {}", updatedTabungan.getJumlah());
            assertNotNull(updatedTabungan);
            assertEquals(-5000L, updatedTabungan.getJumlah());
        });
    }

    @Test
    void addPengeluaranInsufficientFunds() throws Exception {
        PengeluaranAddRequest request = PengeluaranAddRequest.builder()
                .jumlah(5000L)
                .tanggal(LocalDate.now())
                .build();

        mockMvc.perform(
                post("/api/tabungan/pengeluaran")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<?> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getErrors());
            assertEquals("Tabungan tidak cukup", response.getErrors());
        });
    }

    @Test
    void addPengeluaranInvalidJumlahNegative() throws Exception {
        PengeluaranAddRequest request = PengeluaranAddRequest.builder()
                .jumlah(-5000L)
                .tanggal(LocalDate.now())
                .build();

        mockMvc.perform(
                post("/api/tabungan/pengeluaran")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<?> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getErrors());
            assertEquals("jumlah: Jumlah harus lebih besar dari atau sama dengan 1", response.getErrors());
        });
    }

    @Test
    void addPengeluaranInvalidJumlahNull() throws Exception {
        PengeluaranAddRequest request = PengeluaranAddRequest.builder()
                .jumlah(null)
                .tanggal(LocalDate.now())
                .build();

        mockMvc.perform(
                post("/api/tabungan/pengeluaran")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<?> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getErrors());
            assertEquals("jumlah: Jumlah tidak boleh null", response.getErrors());
        });
    }

    @Test
    void addPengeluaranInvalidTanggalNull() throws Exception {
        PengeluaranAddRequest request = PengeluaranAddRequest.builder()
                .jumlah(5000L)
                .tanggal(null)
                .build();

        mockMvc.perform(
                post("/api/tabungan/pengeluaran")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<?> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getErrors());
            assertEquals("tanggal: Tanggal tidak boleh null", response.getErrors());
        });
    }


    @Test
    void getTabunganSuccess() throws Exception {
        mockMvc.perform(
                get("/api/tabungan/")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<?> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertNotNull(response.getData());
        });
    }

    @Test
    void addPemasukanDataSaved() throws Exception {
        PemasukanAddRequest request = PemasukanAddRequest.builder()
                .jumlah(10000L)
                .build();

        mockMvc.perform(
                post("/api/tabungan/pemasukan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        );

        assertEquals(1, pemasukanRepository.count());
        assertEquals(10000L, pemasukanRepository.findAll().get(0).getJumlah());
    }

}
