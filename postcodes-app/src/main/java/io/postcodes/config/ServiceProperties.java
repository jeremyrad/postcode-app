package io.postcodes.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "h2-service")
public class ServiceProperties {

    private Service database = new Service(false, 9002);

    private HttpConnection httpConnection = new HttpConnection(5000, 5000, 5000);

    @AllArgsConstructor
    @Data
    public class Service {

        private boolean enabled;
        private int port;

    }

    @AllArgsConstructor
    @Data
    public class HttpConnection {
        private int timeout;
        private int requestTimeout;
        private int socketTimeout;
    }
}
