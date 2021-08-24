package sls.santana.springboot.aws.api.core.sns.model;

import sls.santana.springboot.aws.api.dto.EventTypeDTO;

public interface Message {

    String getCategory();

    String getProductName();

    EventTypeDTO getEventType();
}
