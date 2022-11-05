package ru.popov.loanrestapi.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LimitServiceImpl implements LimitService {

    private final ConcurrentHashMap<String, Pair> cache = new ConcurrentHashMap<>();

    @Value("${limit.period}")
    private int period;

    @Value("${limit.total}")
    private int total;

    @Override
    public boolean isLimit(String locale) {
        boolean result;
        do {
            Pair pair = this.cache.get(locale);
            Pair temp = pair;
            if (pair == null) {
                pair = new Pair(System.currentTimeMillis(), 1);
                temp = pair;
            } else {
                if ((System.currentTimeMillis() - pair.start)/1000 > total) {
                    pair = new Pair(System.currentTimeMillis(), 1);
                } else {
                    pair = new Pair(pair.start, pair.count + 1);
                }
            }
            result = cache.putIfAbsent(locale, pair) == null || cache.replace(locale, temp, pair);
        } while (!result);
        return cache.get(locale).count > total;
    }

    private final class Pair {
        private final long start;
        private final int count;

        public Pair(long start, int count) {
            this.start = start;
            this.count = count;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            if (start != pair.start) return false;
            return count == pair.count;
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, count);
        }
    }
}
