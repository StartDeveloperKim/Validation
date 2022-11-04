package spring.validstudy.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.validstudy.dto.ErrorResult;
import spring.validstudy.dto.UserDto;
import spring.validstudy.exception.UserException;

@Slf4j
@RestController
@RequestMapping("/api2")
public class ApiExceptionController {

    @GetMapping("/user/{id}")
    public UserDto getUser(@PathVariable("id") String id) {

        if (id.equals("exception")) {
            throw new RuntimeException("잘못된 사용자");
        }
        if (id.equals("bad")) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }
        if (id.equals("user-ex")) {
            throw new UserException("사용자 오류");
        }

        return new UserDto(id, "Korea " + id);
    }
}
