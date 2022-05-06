package edu.ntnu.idatt2106.boco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class BocoApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(BocoApplication.class, args);
    }
}
