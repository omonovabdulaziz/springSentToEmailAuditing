package uz.pdp.springsecurityemailsenderauditing.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {
    @NotNull
    @Length(min = 3, max = 50)
    private String firstName;
    @NotNull
    @Length(min = 3, max = 50)
    private String lastName;
    @Email
    private String email;
    @NotNull
    private String password;


}
