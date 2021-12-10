package UAC.IFRI.AA.PlanningClassroom.Repository;

import UAC.IFRI.AA.PlanningClassroom.Models.ReservationClassroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface ReservationClassroomRepository extends JpaRepository<ReservationClassroom, Long> {
    boolean existsByDateStartBetween(Date dateStart, Date dateEnd);
    boolean existsByDateEndBetween(Date dateStart, Date dateEnd);
}
