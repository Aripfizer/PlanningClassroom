package UAC.IFRI.AA.PlanningClassroom.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })
})
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="user_type")
public abstract class User extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 15)
    private String username;


    @NotBlank
    @Size(max = 40)
    private String lastName;

    @NotBlank
    @Size(max = 40)
    private String firstName;

    @NotNull
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    @Size(max = 100)
    @JsonIgnore
    private String password;

    @NotBlank
    @Size(max = 20)
    private String number;

    @NotBlank
    @Size(max = 10)
    private String sexe;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();


    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "user")
    @JsonIgnore
    private List<Reservation> reservations = new ArrayList<>();


    public User(String username, String lastName, String firstName, String sexe , String email, String password, String number) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.number = number;
        this.sexe = sexe;
    }
}
