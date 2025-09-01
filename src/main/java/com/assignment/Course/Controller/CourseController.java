package com.assignment.Course.Controller;


import com.assignment.Course.Entity.CourseDocument;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CourseController {

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());


    private List<CourseDocument> loadCourses() throws Exception {
        InputStream is = new ClassPathResource("sample-courses.json").getInputStream();
        return mapper.readValue(is, new TypeReference<List<CourseDocument>>() {});
    }

    @GetMapping("/search")
    public Map<String, Object> searchCourses(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String startDate,
            @RequestParam(defaultValue = "upcoming") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) throws Exception {

        List<CourseDocument> courses = loadCourses();

        // filtering
        List<CourseDocument> filtered = courses.stream()
                .filter(c -> q == null || c.getTitle().toLowerCase().contains(q.toLowerCase()))
                .filter(c -> category == null || c.getCategory().equalsIgnoreCase(category))
                .filter(c -> type == null || c.getType().equalsIgnoreCase(type))
                .filter(c -> minAge == null || c.getMinAge() >= minAge)
                .filter(c -> maxAge == null || c.getMaxAge() <= maxAge)
                .filter(c -> minPrice == null || c.getPrice() >= minPrice)
                .filter(c -> maxPrice == null || c.getPrice() <= maxPrice)
                .filter(c -> {
                    if (startDate == null) return true;
                    LocalDate d = LocalDate.parse(startDate);
                    return !c.getNextSessionDate().isBefore(d);
                })
                .collect(Collectors.toList());

        // sorting
        Comparator<CourseDocument> comparator;
        switch (sort) {
            case "priceAsc":
                comparator = Comparator.comparingDouble(CourseDocument::getPrice);
                break;
            case "priceDesc":
                comparator = Comparator.comparingDouble(CourseDocument::getPrice).reversed();
                break;
            default: // upcoming
                comparator = Comparator.comparing(CourseDocument::getNextSessionDate);
        }

        filtered = filtered.stream().sorted(comparator).collect(Collectors.toList());

        // pagination
        int total = filtered.size();
        int fromIndex = Math.min(page * size, total);
        int toIndex = Math.min(fromIndex + size, total);
        List<CourseDocument> paginated = filtered.subList(fromIndex, toIndex);

        // response
        Map<String, Object> response = new HashMap<>();
        response.put("total", total);
        response.put("courses", paginated);
        return response;
    }
}