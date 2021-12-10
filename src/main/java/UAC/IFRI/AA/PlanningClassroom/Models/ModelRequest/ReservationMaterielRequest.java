package UAC.IFRI.AA.PlanningClassroom.Models.ModelRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationMaterielRequest {
    @NotNull
    private Timestamp dateStart;

    @NotNull
    private Timestamp dateEnd;

    @NotEmpty(message = "Ajouter au moin un Matériel")
    List<Long> materiel_Id = new ArrayList<>();
}
