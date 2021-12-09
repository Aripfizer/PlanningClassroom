package UAC.IFRI.AA.PlanningClassroom.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Optional;

@Entity
@DiscriminatorValue("ENSEIGNANT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enseignant extends User
{
    private boolean titulaire = false;
    private boolean state = false;

    public Enseignant(String username, String lastName, String firstName, String sexe, String email, String password, String number) {
        super(username, lastName, firstName, sexe, email, password, number);
        this.titulaire = true;
        this.state = true;
    }
}
