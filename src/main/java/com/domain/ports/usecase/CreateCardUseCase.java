package com.domain.ports.usecase;

import com.domain.dto.Card;

public interface CreateCardUseCase {

    Card execute(String cardNumber);

}
