package sls.santana.springboot.aws.api.core.sns.service.impl;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import sls.santana.springboot.aws.api.core.sns.config.AwsProperties;
import sls.santana.springboot.aws.api.core.sns.model.Message;
import sls.santana.springboot.aws.api.core.sns.model.SnsResponse;
import sls.santana.springboot.aws.api.core.sns.model.SnsResponseImpl;
import sls.santana.springboot.aws.api.core.sns.service.MessagePublisher;
import sls.santana.springboot.aws.api.dto.EventTypeDTO;
import sls.santana.springboot.aws.api.dto.MessageDTO;

@Service
public class MessagePublisherImpl implements MessagePublisher {

    private final static Logger LOG = LoggerFactory.getLogger(MessagePublisherImpl.class);

    private final AmazonSNSClient snsClient;

    private final AwsProperties awsProperties;

    @Autowired
    public MessagePublisherImpl(@Qualifier("snsClient") AmazonSNSClient snsClient, AwsProperties awsProperties) {
        this.snsClient = snsClient;
        this.awsProperties = awsProperties;
    }

    @Override
    public String publish(Message message) throws JsonProcessingException {
        PublishRequest publishRequest = new PublishRequest(awsProperties.getTopicArn(),createJson(message), "Notification: Encontro Tech Consorcio");
        PublishResult response = snsClient.publish(publishRequest);
        return "Mensagem enviada com sucesso sucesso";
    }

    private String createJson(Message message) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(MessageDTO.builder()
                .category(message.getCategory())
                .eventType(message.getEventType())
                .productName(message.getProductName())
                .build());
    }

    private void rethrow(Integer statusCode, String detailedMessage) {
        SnsResponse response = new SnsResponseImpl(statusCode, detailedMessage, null);
        throw new RuntimeException(response.toString());
    }

}
