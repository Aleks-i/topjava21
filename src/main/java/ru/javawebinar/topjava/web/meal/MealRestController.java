package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@RestController()
public class MealRestController extends AbstractMealController {
    static final String REST_URL = "/rest/profile/meals";

    private static final Logger log = getLogger(MealRestController.class);

    @GetMapping(value = REST_URL + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Meal get(@PathVariable int id) {
        log.debug("get meal id {}", id);
        return super.get(id);
    }

    @DeleteMapping(REST_URL + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.debug("delete meal id {}", id);
        super.delete(id);
    }

    @GetMapping(REST_URL)
    public List<MealTo> getAll() {
        log.debug("get meals");
        return super.getAll();
    }

    @PostMapping(value = REST_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Meal> createMeal(@RequestBody Meal meal) {
        Meal mealCreated = super.create(meal);

        URI newUriResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(mealCreated.getId()).toUri();

        return ResponseEntity.created(newUriResource).body(mealCreated);
    }

    @PutMapping(value = REST_URL + "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Meal meal, @PathVariable int id) {
        super.update(meal, id);
    }

    @GetMapping(REST_URL + "/filter")
    public List<MealTo> getBetween(@RequestParam @DateTimeFormat (iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
                                   @RequestParam @DateTimeFormat (iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime) {
        return super.getBetween(startDateTime.toLocalDate(), startDateTime.toLocalTime(),
                endDateTime.toLocalDate(), endDateTime.toLocalTime());
    }
}