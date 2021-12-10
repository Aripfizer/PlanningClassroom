package UAC.IFRI.AA.PlanningClassroom.Models.ModelRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationClassroomRequest {

    @NotNull
    private Timestamp dateStart;

    @NotNull
    private Timestamp dateEnd;

}
