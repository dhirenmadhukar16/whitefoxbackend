package com.example.whitefox.config;

import com.couchbase.client.core.env.SecurityConfig;
import com.couchbase.client.java.env.ClusterEnvironment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;

@Configuration
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {

    @Value("${spring.couchbase.connection-string}")
    private String connectionString;

    @Value("${spring.couchbase.username}")
    private String userName;

    @Value("${spring.couchbase.password}")
    private String password;

    @Value("${spring.data.couchbase.bucket-name}")
    private String bucketName;

    @Override
    public String getConnectionString() {
        return connectionString;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getBucketName() {
        return bucketName;
    }

    @Override
    protected void configureEnvironment(ClusterEnvironment.Builder builder) {
        if (connectionString != null && connectionString.startsWith("couchbases://")) {
            builder.securityConfig(SecurityConfig.enableTls(true));
        }
    }
}
