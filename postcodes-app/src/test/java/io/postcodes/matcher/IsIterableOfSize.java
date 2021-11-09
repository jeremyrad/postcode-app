package io.postcodes.matcher;

import org.mockito.ArgumentMatcher;

import java.util.stream.StreamSupport;

public class IsIterableOfSize implements ArgumentMatcher<Iterable> {

    int listSize;

    public IsIterableOfSize(final int listSize) {
        this.listSize = listSize;
    }

    @Override
    public boolean matches(final Iterable list) {
        return StreamSupport.stream(list.spliterator(), false).count() == listSize;
    }
}
