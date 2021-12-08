package UAC.IFRI.AA.PlanningClassroom.Repository;

import UAC.IFRI.AA.PlanningClassroom.Models.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
}
