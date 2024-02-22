package login.jwtlogin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@Getter
@Setter
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native")

    @Column(name = "user_id")
    private int id;
    private String email;
    private String name;
    private String mobile;
    private String role;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String pwd;

    @Column(name = "create_dt")
    private Date createDt;

    @Column(name = "refresh_token")
    private String refreshToken;

}
