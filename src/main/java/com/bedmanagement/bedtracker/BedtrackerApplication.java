package com.bedmanagement.bedtracker;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableScheduling
@ComponentScan(basePackages = "com.bedmanagement.bedtracker")
@PropertySource(value = "classpath:application.properties")
@EnableJpaRepositories(basePackages = "com.bedmanagement.bedtracker.io.repository")
@EnableEncryptableProperties
@SpringBootApplication
        (scanBasePackages = {"com.bedmanagement.bedtracker.messagebroker"})
@EnableDiscoveryClient
public class BedtrackerApplication {

    @Value("${cloud.aws.access-key}")
    private String accessKey;

    @Value("${cloud.aws.secret-key}")
    private String secretKey;

    @Bean
    public AmazonS3 s3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion("us-east-2").build();
    }

//    @Primary
//    @Bean
//    public LoggerContext getLoggerContext() {
//        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
//
//        // Set the log file path programmatically
//        context.putProperty("LOG_FILE_PATH", "abc");
//
//        // Log the configured log file path (optional)
//        System.out.println("Configuring log file path: {}");
//
//        // Print logback internal status (optional)
//        StatusPrinter.print(context);
//
//        return context;
//    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    public static void main(String[] args) {
        SpringApplication.run(BedtrackerApplication.class, args);
    }


}
