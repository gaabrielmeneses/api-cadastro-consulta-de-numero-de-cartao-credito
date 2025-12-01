package com.domain.ports.usecase;

import java.io.InputStream;

public interface BatchCreateCardsUseCase {
    int execute(InputStream inputStream);
}
