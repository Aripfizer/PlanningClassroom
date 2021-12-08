package UAC.IFRI.AA.PlanningClassroom.Payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    @NotBlank
    @Size(max = 20)
    private String userType;

    @NotBlank
    @Size(max = 40)
    private String lastName;

    @NotBlank
    @Size(max = 40)
    private String firstName;

    @NotBlank
    @Size(min = 3, max = 15)
    private String username;

    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @Nullable
    @Size(min = 5, max = 20)
    private String password;

    @NotBlank
    @Size(max = 20)
    private String number;

    @NotBlank
    @Size(max = 10)
    private String sexe;

    private String noMatricule;

    private boolean inscrit;

    private boolean titulaire;
    private boolean responsable;
}