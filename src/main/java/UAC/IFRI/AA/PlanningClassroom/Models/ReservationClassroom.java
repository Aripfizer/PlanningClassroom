package UAC.IFRI.AA.PlanningClassroom.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ReservationClassroom extends Reservation
{
    @OneToOne(cascade= CascadeType.ALL)
    @JoinColumn(name="resSalId")
    private Classroom classroom;
}
