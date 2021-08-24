package sls.santana.springboot.aws.api.core.sns.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SnsResponseImpl implements SnsResponse{

    private Integer statusCode;

    private String message;

    private String publishedMessageId;
}
