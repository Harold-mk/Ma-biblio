package com.haroldmokam.ma_biblio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class MaBiblioApplication {

    public static void main(String[] args) {
        SpringApplication.run(MaBiblioApplication.class, args);
    }

}
