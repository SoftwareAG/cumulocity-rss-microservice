package c8y.rss;

import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.RestController;

import com.cumulocity.microservice.autoconfigure.MicroserviceApplication;

@MicroserviceApplication
@RestController
public class App
{
    public static void main( String[] args ) {
        SpringApplication.run(App.class, args);
    }

}