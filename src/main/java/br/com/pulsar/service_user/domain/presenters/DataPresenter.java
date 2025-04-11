package br.com.pulsar.service_user.domain.presenters;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class DataPresenter<T> {

    private Integer statusCode;
    private LocalDateTime timeStamp;
    private T data;

    public DataPresenter(T data) {
        statusCode = HttpStatus.OK.value();
        this.timeStamp = LocalDateTime.now();
        this.data = data;
    }
}
