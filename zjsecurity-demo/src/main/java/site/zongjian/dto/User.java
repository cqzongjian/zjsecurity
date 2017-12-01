package site.zongjian.dto;

import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

public class User {

    public interface UserSimpleView {}
    public interface UserDetailView extends UserSimpleView {}

    private int id;
    private String username;
    @NotBlank  // 不为空校验
    private String password;
    private Date birthday;

    @JsonView(UserSimpleView.class)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonView(UserSimpleView.class)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonView(UserDetailView.class)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonView(UserSimpleView.class)
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
