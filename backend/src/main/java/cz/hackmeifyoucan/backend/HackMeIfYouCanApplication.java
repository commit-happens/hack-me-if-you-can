/* Hlavní vstupní bod aplikace */

package cz.hackmeifyoucan.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HackMeIfYouCanApplication {

    public static void main(String[] args) {
        SpringApplication.run(HackMeIfYouCanApplication.class, args);
    }

}
