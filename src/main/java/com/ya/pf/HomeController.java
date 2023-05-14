package com.ya.pf;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
public class HomeController {

    @GetMapping(value = "/", produces = "text/html")
    public ResponseEntity<String> index() throws IOException {

        Resource index = new ClassPathResource("static/index.html");
        return ResponseEntity.ok().body(IOUtils.toString(index.getInputStream(), StandardCharsets.UTF_8));
    }

}
