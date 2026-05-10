package models;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    private String email;
    private String password;
    private String name;

    public User withEmail(String email){
        this.email = email;
        return this;
    }

    public User withPassword(String password){
        this.password = password;
        return this;
    }

    public User withName(String name){
        this.name = name;
        return this;
    }
}

