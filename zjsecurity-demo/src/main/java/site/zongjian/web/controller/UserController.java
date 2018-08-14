package site.zongjian.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import site.zongjian.dto.User;
import site.zongjian.dto.UserQueryCondition;
import site.zongjian.exception.UserNotExitException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zongjian
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

    // @RequestMapping(value = "/user", method = RequestMethod.GET)
    @GetMapping
    @JsonView(User.UserSimpleView.class)
    // public List<User> query(@RequestParam(required = false, name = "nickname") String username)
    public List<User> query(UserQueryCondition condition, @PageableDefault() Pageable pageable) {
        System.out.println(ReflectionToStringBuilder.toString(condition, ToStringStyle.MULTI_LINE_STYLE));

        System.out.println(pageable.getPageSize());
        System.out.println(pageable.getPageNumber());
        System.out.println(pageable.getSort());

        List users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        users.add(new User());
        return users;
    }

    // @RequestMapping(value = "/user/{id:\\d+}", method = RequestMethod.GET)
    @GetMapping("/{id:\\d+}")
    @JsonView(User.UserDetailView.class)
    public User getInfo(@PathVariable String id) {

//        throw new RuntimeException("用户不存在！");
        throw new UserNotExitException(id);

//        User user = new User();
//        user.setUsername("tom");
//        return user;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user, BindingResult errors) {

        if (errors.hasErrors()) {
            errors.getAllErrors()
                    .stream()
                    .forEach(error -> System.out.println(error.getDefaultMessage()));
        }

        System.out.println(user.getId());
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        System.out.println(user.getBirthday());

        user.setId(1);
        return user;
    }

    @PutMapping("/{id:\\d+}")
    public User update(@Valid @RequestBody User user, BindingResult errors) {

        if (errors.hasErrors()) {
            errors.getAllErrors()
                    .stream()
                    .forEach(error -> {
                        FieldError fieldError = (FieldError) error;
                        String message = fieldError.getField() + " " + error.getDefaultMessage();
                        System.out.println(message);
                    });
        }

        System.out.println(user.getId());
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        System.out.println(user.getBirthday());

        user.setId(1);
        return user;
    }

    @DeleteMapping("/{id:\\d+}")
    public void delete(@PathVariable String id) {
        System.out.println(id);
    }

}
