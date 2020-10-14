package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.MealsUtil.mealsForUser;
import static ru.javawebinar.topjava.util.UsersUtil.*;
import static ru.javawebinar.topjava.util.Util.isBetweenHalfOpen;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        mealsForUser.forEach(meal -> save(meal, USER_ID));
        mealsForAdmin.forEach(meal -> save(meal, ADMIN_ID));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save meal {}", meal);
        Map<Integer, Meal> meals = repository.computeIfAbsent(userId, b -> (new ConcurrentHashMap<Integer, Meal>()));
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            meals.put(meal.getId(), meal);
            return meal;
        }
        return meals.computeIfPresent(meal.getId(), (id, oldMeal) -> oldMeal = meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete meal id {}", id);
        Map<Integer, Meal> meals = repository.get(userId);
        return meals != null && meals.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get meal id {}", id);
        Map<Integer, Meal> meals = repository.get(userId);
        return meals == null ? null : meals.get(id);
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        log.info("getAll meal for user {}", userId);
        Map<Integer, Meal> meals = repository.get(userId);
        return CollectionUtils.isEmpty(meals) ? Collections.emptyList() : meals.values().stream()
                .sorted(Comparator.comparing(Meal::getDateTime))
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getMealFilteredByDate(LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd, int userId) {
        log.info("getMealFilteredByDate {}", userId);
        return getAll(userId).stream()
                .filter(meal -> isBetweenHalfOpen(meal.getDateTime(), dateTimeStart, dateTimeEnd))
                .collect(Collectors.toList());
    }
}

