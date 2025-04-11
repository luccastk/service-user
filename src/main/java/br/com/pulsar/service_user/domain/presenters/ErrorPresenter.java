package br.com.pulsar.service_user.domain.presenters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorPresenter {
    private int code;
    private String message;
}
