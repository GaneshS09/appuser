package com.eagle.util;

import java.util.Optional;
import java.util.function.Function;

public class IdGenerator<T> {
    private final Function<T, String> idExtractor;
    private final Function<String, Long> idParser;
    private final Function<Long, String> idFormatter;

    public IdGenerator(Function<T, String> idExtractor,
                       Function<String, Long> idParser,
                       Function<Long, String> idFormatter) {
        this.idExtractor = idExtractor;
        this.idParser = idParser;
        this.idFormatter = idFormatter;
    }

    public String generateId(String prefix, Optional<T> lastEntity){
        long lastId = lastEntity.map( entity -> {
            String fullId = idExtractor.apply(entity);
            System.out.println("ganesh fullID "+fullId);
            return idParser.apply(fullId);
        }).orElse(0l);

        return prefix + idFormatter.apply(lastId + 1);
    }
}
