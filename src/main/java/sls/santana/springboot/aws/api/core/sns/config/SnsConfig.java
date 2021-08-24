package sls.santana.springboot.aws.api.core.sns.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.net.URI;

@Configuration
public class SnsConfig {

    @Autowired
    private AwsProperties awsProperties;

    public static final String SECRET_KEY = "asdf";
    public static final String ACCESS_KEY = "asdf";

    @Bean(name = "snsClient")
    @Primary
    public AmazonSNSClient snsClient() {
        return (AmazonSNSClient) AmazonSNSClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4566", this.awsProperties.getRegion()))
                .build();
    }
}
