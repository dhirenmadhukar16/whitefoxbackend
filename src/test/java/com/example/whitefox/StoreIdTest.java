package com.example.whitefox;

import com.example.whitefox.store.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StoreIdTest {

    @Autowired
    private StoreRepository storeRepository;

    @Test
    public void printStoreId() {
        storeRepository.findAll().forEach(store -> {
            System.out.println("====== THE_STORE_ID_IS: " + store.getId() + " ======");
        });
    }
}
