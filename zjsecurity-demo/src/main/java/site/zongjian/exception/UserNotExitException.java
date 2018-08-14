package site.zongjian.exception;

public class UserNotExitException extends RuntimeException {

    private static final long serialVersionUID = -3727308783370998561L;

    private String id;

    public UserNotExitException(String id) {
        super("用户不存在！");
        this.id = id;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
