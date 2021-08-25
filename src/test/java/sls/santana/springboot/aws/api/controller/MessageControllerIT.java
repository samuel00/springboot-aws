package sls.santana.springboot.aws.api.controller;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import sls.santana.springboot.aws.api.core.sns.config.AwsProperties;
import sls.santana.springboot.aws.api.core.sns.service.impl.MessagePublisherImpl;
import sls.santana.springboot.aws.api.dto.EventTypeDTO;
import sls.santana.springboot.aws.api.dto.MessageDTO;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SNS;

@Testcontainers
@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = { AwsConfig.class })
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class MessageControllerIT {

    @Container
    static LocalStackContainer localStack =
            new LocalStackContainer(DockerImageName.parse("localstack/localstack:0.10.0"))
                    .withServices(SNS)
                    .withEnv("DEFAULT_REGION", "us-east-1");

    MockMvc mockMvc;

    static final String TOPICO = "Paysandu";

    @Autowired
    AmazonSNSClient amazonSNS;

    @Mock
    AwsProperties awsProperties;

    @BeforeEach
    void setup() throws IOException, InterruptedException {
        MockitoAnnotations.openMocks(this);
        CreateTopicResult createTopicResult = amazonSNS.createTopic(TOPICO);
        MessageController messageController = new MessageController(new MessagePublisherImpl( this.amazonSNS, this.awsProperties));
        mockMvc = MockMvcBuilders.standaloneSetup(messageController).build();
        when(this.awsProperties.getRegion()).thenReturn("us-east-1");
        localStack.execInContainer("awslocal", "sns", "create-topic", "--name", TOPICO);
        when(this.awsProperties.getTopicArn()).thenReturn("arn:aws:sns:us-east-1:000000000000:"+ TOPICO);
    }

    @TestConfiguration
    static class AwsConfig {

        @Bean(name = "amazonSNS")
        @Profile("test")
        public AmazonSNSClient amazonSNS() throws IOException, InterruptedException {
            return  (AmazonSNSClient) AmazonSNSClient.builder()
                    .withEndpointConfiguration(localStack.getEndpointConfiguration(SNS))
                    .withClientConfiguration(
                            new ClientConfiguration()
                                    .withMaxErrorRetry(10)
                                    .withConnectionTimeout(10_1000)
                                    .withSocketTimeout(10_000)
                                    .withTcpKeepAlive(true))
                    .build();
        }

        public AWSCredentialsProvider credentialsProvider() {
            return localStack.getDefaultCredentialsProvider();
        }
    }

    @Test
    public void should_send_message_to_topic_sns() throws Exception {
        this.mockMvc.perform(post("/publish")
                .content(createJson())
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());
    }

    private String createJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(MessageDTO.builder()
                .category("Empréstimos")
                .eventType(EventTypeDTO.CONTRATO)
                .productName("Consórcio")
                .build());
    }

}