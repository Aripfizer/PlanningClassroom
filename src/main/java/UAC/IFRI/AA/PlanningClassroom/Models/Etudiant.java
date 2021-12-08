package UAC.IFRI.AA.PlanningClassroom.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
@Entity
@DiscriminatorValue("ETUDIANT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Etudiant extends User
{
    private String noMatricule;
    private boolean inscrit = true;
}
