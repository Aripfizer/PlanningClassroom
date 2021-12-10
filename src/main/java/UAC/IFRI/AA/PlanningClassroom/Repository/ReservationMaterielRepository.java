package UAC.IFRI.AA.PlanningClassroom.Repository;

import UAC.IFRI.AA.PlanningClassroom.Models.ReservationMateriel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ReservationMaterielRepository extends JpaRepository<ReservationMateriel, Long> {
    boolean existsByDateStartBetweenAndState(Date dateStart, Date dateEnd, boolean state);
    boolean existsByDateEndBetweenAndState(Date dateStart, Date dateEnd, boolean state);
    Optional<ReservationMateriel> findByIdAndState(Long id, boolean state);
    List<ReservationMateriel> findAllByUserEmailAndState(String email, boolean state);
}
