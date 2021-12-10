package UAC.IFRI.AA.PlanningClassroom.Models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "classrooms", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "name"
        })
})
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 25)
    private String name;

    @Min(value = 10, message = "Ajouter au moin 10 place dans la salle")
    private int effectif;
}
