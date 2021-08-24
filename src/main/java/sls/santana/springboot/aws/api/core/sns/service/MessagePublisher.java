package sls.santana.springboot.aws.api.core.sns.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import sls.santana.springboot.aws.api.core.sns.model.Message;

public interface MessagePublisher {

    String publish(Message message) throws JsonProcessingException;
}
