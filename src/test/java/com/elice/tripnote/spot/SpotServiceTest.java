package com.elice.tripnote.spot;

import com.elice.tripnote.domain.spot.service.SpotService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpotServiceTest {
    @Autowired
    private SpotService spotService;

    @Test
    void spotServiceTest(){
        var result = spotService.search("갈비집");

        System.out.println(result);

        Assertions.assertNotNull(result);
    }
}
