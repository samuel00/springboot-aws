package sls.santana.springboot.aws.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;
import sls.santana.springboot.aws.api.core.sns.service.MessagePublisher;
import sls.santana.springboot.aws.api.dto.MessageDTO;
import sls.santana.springboot.aws.api.dto.ResponseDTO;

import java.time.LocalDateTime;

@RestController
public class MessageController {

    private final MessagePublisher messagePublisher;

    @Autowired
    public MessageController(MessagePublisher messagePublisher) {
        this.messagePublisher = messagePublisher;
    }

    @PostMapping(value = "/publish")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> publishMessage(@RequestBody MessageDTO messageDTO) throws JsonProcessingException {
        String message = messagePublisher.publish(messageDTO);
        return new ResponseEntity<>(ResponseDTO.builder()
                    .message(message)
                    .data(LocalDateTime.now().toString())
                    .build(), HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    private String handleException(RuntimeException e) {
        return e.getMessage();
    }
}
