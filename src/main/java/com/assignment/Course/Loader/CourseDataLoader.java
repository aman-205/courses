package com.assignment.Course.Loader;

import com.assignment.Course.Entity.CourseDocument;
import com.assignment.Course.Repo.CourseRepo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;


@Component
public class CourseDataLoader implements CommandLineRunner {
    private final CourseRepo repo;

    private final ObjectMapper objectMapper;
    public CourseDataLoader(CourseRepo repo, ObjectMapper objectMapper) {
        this.objectMapper=objectMapper;
        this.repo = repo;
    }

    @Override
    public void run(String... args) throws Exception {
        if (repo.count() > 0) {
            System.out.println("Courses already indexed - skipping bootstrap.");
            return;
        }

        InputStream is = new ClassPathResource("sample-courses.json").getInputStream();
        List<CourseDocument> courses = objectMapper.readValue(is, new TypeReference<List<CourseDocument>>() {});
        repo.saveAll(courses);
        System.out.println("Indexed " + courses.size() + " courses.");
    }
}
