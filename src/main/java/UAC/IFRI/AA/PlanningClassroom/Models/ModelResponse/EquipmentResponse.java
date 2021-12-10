package UAC.IFRI.AA.PlanningClassroom.Models.ModelResponse;

import UAC.IFRI.AA.PlanningClassroom.Models.HardwareName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class EquipmentResponse {

    private Long id;

    private HardwareName hardwareName;

    private Timestamp dateStart;

    private Timestamp dateEnd;


}
