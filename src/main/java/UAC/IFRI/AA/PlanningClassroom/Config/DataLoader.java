package UAC.IFRI.AA.PlanningClassroom.Config;

import UAC.IFRI.AA.PlanningClassroom.Models.Enseignant;
import UAC.IFRI.AA.PlanningClassroom.Models.Role;
import UAC.IFRI.AA.PlanningClassroom.Models.RoleName;
import UAC.IFRI.AA.PlanningClassroom.Repository.EnseignantRepository;
import UAC.IFRI.AA.PlanningClassroom.Repository.RoleRepository;
import UAC.IFRI.AA.PlanningClassroom.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    EnseignantRepository enseignantRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if(!roleRepository.existsByName(RoleName.ROLE_ETUDIANT)) roleRepository.save(new Role(RoleName.ROLE_ETUDIANT));

        if(!roleRepository.existsByName(RoleName.ROLE_ENSEIGNANT))  roleRepository.save(new Role(RoleName.ROLE_ENSEIGNANT));

        if(!roleRepository.existsByName(RoleName.ROLE_SUPERENSEIGNANT)) roleRepository.save(new Role(RoleName.ROLE_SUPERENSEIGNANT));

    }

}