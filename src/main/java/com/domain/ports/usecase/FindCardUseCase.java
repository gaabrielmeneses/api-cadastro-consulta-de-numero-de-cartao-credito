package com.domain.ports.usecase;

import com.domain.dto.Card;

public interface FindCardUseCase {

    Card execute(String cardNumber);

}
