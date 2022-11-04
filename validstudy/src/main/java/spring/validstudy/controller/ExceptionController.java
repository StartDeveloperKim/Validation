package spring.validstudy.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import spring.validstudy.dto.UserDto;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
public class ExceptionController {

    @GetMapping("/error-404")
    public void error404(HttpServletResponse response) throws IOException {
        response.sendError(404, "404오류!!");
    }

    @GetMapping("/error-500")
    public void error500(HttpServletResponse response) throws IOException {
        response.sendError(500, "500오류!!");
    }

    @GetMapping("/api/user/{id}")
    public UserDto getUser(@PathVariable("id") String id) {

        if (id.equals("exception")) {
            throw new RuntimeException("잘못된 사용자");
        }

        return new UserDto(id, "Korea " + id);
    }


}
