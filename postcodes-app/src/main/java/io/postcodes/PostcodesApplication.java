package io.postcodes;

import io.postcodes.config.ServiceProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication(exclude = ElasticsearchRestClientAutoConfiguration.class, scanBasePackages = {"io.postcodes"})
@EnableConfigurationProperties({ServiceProperties.class})
public class PostcodesApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostcodesApplication.class, args);
    }

}
