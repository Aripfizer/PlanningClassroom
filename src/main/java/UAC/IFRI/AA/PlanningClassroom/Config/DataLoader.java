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
        Role  role1 =  roleRepository.save(new Role(RoleName.ROLE_ETUDIANT));
        Role role2 = roleRepository.save(new Role(RoleName.ROLE_ENSEIGNANT));
        Role role3 = roleRepository.save(new Role(RoleName.ROLE_SUPERENSEIGNANT));


        Enseignant enseignant = new Enseignant("Ariking", "Admin", "admin","Masculin", "arieldossou00@gmail.com", "$2a$10$WL16u6y2gVX3rl8IKwZCkeHcBpf1.uhrWWqfL5TD650uaFIUxmZMe",  "67180009");

        enseignant.getRoles().add(role1);
        enseignant.getRoles().add(role2);
        enseignant.getRoles().add(role3);
        enseignantRepository.save(enseignant);
    }

}