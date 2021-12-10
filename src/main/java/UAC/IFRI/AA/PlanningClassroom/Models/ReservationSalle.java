package UAC.IFRI.AA.PlanningClassroom.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationSalle extends Reservation
{
    @OneToOne(cascade= CascadeType.ALL)
    @JoinColumn(name="resSalId")
    private Salle salle;
}
