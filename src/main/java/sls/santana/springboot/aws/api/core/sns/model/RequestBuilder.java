package sls.santana.springboot.aws.api.core.sns.model;

import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;

import java.util.HashMap;
import java.util.Map;

public interface RequestBuilder {

    String CATEGORY = "Category";
    String PRODUCT_NAME = "ProductName";
    String EVENT_TYPE = "EventType";
    String DEFAULT_MESSAGE_BODY = "Please see attributes.";


    static PublishRequest build(String topicArn, Message message) {
        Map<String, MessageAttributeValue> attributes = new HashMap<>();
        attributes.put(CATEGORY, buildAttribute(message.getCategory(), "String"));
        attributes.put(PRODUCT_NAME, buildAttribute(message.getProductName(), "String"));
        attributes.put(EVENT_TYPE, buildAttribute(message.getEventType().toString(), "String"));

        PublishRequest request = new PublishRequest();
        request.setTopicArn(topicArn);
        request.setMessage(DEFAULT_MESSAGE_BODY);
        request.setMessageAttributes(attributes);

        return request;
    }

    private static MessageAttributeValue buildAttribute(String value, String dataType) {

        MessageAttributeValue messageAttributeValue = new MessageAttributeValue();


        messageAttributeValue.setDataType(dataType);
        messageAttributeValue.setStringValue(value);

        return messageAttributeValue;
    }
}
