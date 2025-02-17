package frank.ecommerceapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EcommerceappApplication implements CommandLineRunner {

    @Value("${GOOGLE_CLIENT_ID}")
    private String googleClientId;

    @Value("${GOOGLE_CLIENT_SECRET}")
    private String googleClientSecret;


    @Value("${GITHUB_CLIENT_ID}")
    private String githubClientId;

    @Value("${GITHUB_CLIENT_SECRET}")
    private String githubClientSecret;




    public static void main(String[] args) {
        SpringApplication.run(EcommerceappApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {

        System.out.println("google clientId: " + googleClientId);
        System.out.println("google clientSecret: " + googleClientSecret);

        System.out.println("github clientId: " + googleClientId);
        System.out.println("github clientSecret: " + googleClientSecret);

    }
}
