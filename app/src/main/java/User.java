import com.daedrii.bodyapp.BodyInfo;

public class User {

    private String name;

    private String email;

    private String senha;

    private String username;

    private BodyInfo bodyInfo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BodyInfo getBodyInfo() {
        return bodyInfo;
    }

    public void setBodyInfo(BodyInfo bodyInfo) {
        this.bodyInfo = bodyInfo;
    }



}
