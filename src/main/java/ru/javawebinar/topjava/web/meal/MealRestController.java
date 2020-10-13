package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import java.util.Collection;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

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
}