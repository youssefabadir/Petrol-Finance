package com.ya.pf;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Slf4j
@RestController
public class HomeController {

    @GetMapping(value = "/", produces = "text/html")
    public ResponseEntity<String> index() {

        try {
            Resource index = new ClassPathResource("static/index.html");
            return ResponseEntity.ok().body(IOUtils.toString(index.getInputStream(), StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()).replaceAll(", ", ",\n"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
