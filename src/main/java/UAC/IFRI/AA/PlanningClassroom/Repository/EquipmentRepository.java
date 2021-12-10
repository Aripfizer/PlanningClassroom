package UAC.IFRI.AA.PlanningClassroom.Repository;

import UAC.IFRI.AA.PlanningClassroom.Models.Equipment;
import UAC.IFRI.AA.PlanningClassroom.Models.HardwareName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment, Long>
{
    boolean existsByHardwareName(HardwareName hardwareName);
}
