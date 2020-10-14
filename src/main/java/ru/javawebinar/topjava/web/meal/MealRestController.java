package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;
import static ru.javawebinar.topjava.util.MealsUtil.getFilteredTos;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;

@Controller
public class MealRestController {
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);
    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal save(Meal meal, int userId) {
        log.info("save meal {}", meal);
        return service.save(meal, userId);
    }

    public void delete(int id, int userId) {
        log.info("delete meal {}", id);
        service.delete(id, userId);
    }

    public Meal get(int id, int userId) {
        log.info("get meal id {}", id);
        return service.get(id, userId);
    }

    public Collection<Meal> getAll(int userId) {
        log.info("getAll meal");
        return service.getAll(userId);
    }

    public List<MealTo> getFilterMealsToByDateTime(@Nullable LocalDate dateStart, @Nullable LocalDate dateEnd,
                                                   @Nullable LocalTime timeStart, @Nullable LocalTime timeEnd, int userId) {
        log.info("getFilterMealsToByDateTime");
        List<Meal> mealsFilteredByDate = service.getMealFilteredByDate(dateStart, dateEnd, userId);
        return getFilteredTos(mealsFilteredByDate, authUserCaloriesPerDay(), timeStart, timeEnd);
    }
}