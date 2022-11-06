package c8y.rss;

import com.cumulocity.microservice.autoconfigure.MicroserviceApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@MicroserviceApplication
@RestController
public class App
{
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args )
    {
        SpringApplication.run(App.class, args);
    }


    // http://localhost:8099/hey
    @RequestMapping("hey")
    public String greeting (@RequestParam(value = "name", defaultValue = "World") String you) {
        System.out.println("Hey " + you + "!");
        return "Hey " + you + "!";
    }

}