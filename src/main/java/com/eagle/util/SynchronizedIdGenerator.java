package com.eagle.util;

import java.util.Optional;
import java.util.function.Function;

public class SynchronizedIdGenerator<T> extends IdGenerator<T> {
    public SynchronizedIdGenerator(Function<T, String> idExtractor,
                                   Function<String, Long> idParser,
                                   Function<Long, String> idFormatter){
        super(idExtractor,idParser,idFormatter);
    }

    public synchronized String generateId(String prefix, Optional<T> lastEntity){
        return super.generateId(prefix,lastEntity);
    }
}
