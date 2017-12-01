package site.zongjian.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import site.zongjian.dto.User;
import site.zongjian.dto.UserQueryCondition;

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
        User user = new User();
        user.setUsername("tom");
        return user;
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

}
