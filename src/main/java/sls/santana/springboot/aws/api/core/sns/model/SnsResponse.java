package sls.santana.springboot.aws.api.core.sns.model;

public interface SnsResponse {

    Integer getStatusCode() ;

    String getMessage() ;

    String getPublishedMessageId() ;
}
