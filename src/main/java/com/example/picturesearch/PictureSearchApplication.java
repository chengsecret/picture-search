package com.example.picturesearch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.picturesearch.mapper")
public class PictureSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(PictureSearchApplication.class, args);
    }

}
