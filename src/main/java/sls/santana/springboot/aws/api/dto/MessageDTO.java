package sls.santana.springboot.aws.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sls.santana.springboot.aws.api.core.sns.model.Message;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDTO implements Message {

    private String category;

    private String productName;

    private EventTypeDTO eventType;
}
