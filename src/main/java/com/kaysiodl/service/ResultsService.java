package com.kaysiodl.service;

import com.kaysiodl.database.Result;
import com.kaysiodl.database.ResultsRepository;
import com.kaysiodl.database.User;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Stateless
public class ResultsService {
    @Inject
    private ResultsRepository resultsRepository;

    public Result save(double x, double y, double r, User user) {
        Long start = System.nanoTime();
        boolean hit = checkHit(x, y, r);
        Long end = System.nanoTime();

        Result result = Result.builder()
                .x(x)
                .y(y)
                .r(r)
                .hit(hit)
                .currentTime(
                        String.valueOf(LocalTime.now(ZoneId.of("Europe/Moscow"))
                                .withNano(0))
                )
                .executionTime(String.format("%.3f ms", (end - start) / 1_000_000.0))
                .user(user)
                .build();

        resultsRepository.add(result);
        return result;
    }

    public List<Result> findByUser(User user) {
        return resultsRepository.findByUser(user);
    }

    public boolean checkHit(double x, double y, double r) {
        return ((x * x + y * y <= (r * r)) && x >= 0 && y <= 0) || // sector
                (x >= 0 && x <= r/2 && y <= r && y >= 0) || //square
                ((y <= x/2 + r/2) && x <= 0 && y >= 0); //triangle
    }
}
