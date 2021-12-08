package UAC.IFRI.AA.PlanningClassroom.Repository;


import UAC.IFRI.AA.PlanningClassroom.Models.Enseignant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {
    Optional<Enseignant> findByUsernameOrEmailAndPassword(String username, String email, String password);
    Boolean existsByEmail(String email);
    Optional<Enseignant> findByUsername(String username);
    List<Enseignant> findAllByState(boolean state);

    Optional<Enseignant> findByEmail(String email);
}
