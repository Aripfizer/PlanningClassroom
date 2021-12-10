package UAC.IFRI.AA.PlanningClassroom.Repository;

import UAC.IFRI.AA.PlanningClassroom.Models.ReservationClassroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ReservationClassroomRepository extends JpaRepository<ReservationClassroom, Long> {
    boolean existsByDateStartBetweenAndState(Date dateStart, Date dateEnd, boolean state);
    boolean existsByDateEndBetweenAndState(Date dateStart, Date dateEnd, boolean state);
    Optional<ReservationClassroom> findByIdAndState(Long id, boolean state);
    List<ReservationClassroom> findAllByUserEmailAndState(String email, boolean state);
}
