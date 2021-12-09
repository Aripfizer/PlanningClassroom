package UAC.IFRI.AA.PlanningClassroom.Controller;

import UAC.IFRI.AA.PlanningClassroom.Exception.AppException;
import UAC.IFRI.AA.PlanningClassroom.Exception.BadRequestException;
import UAC.IFRI.AA.PlanningClassroom.Exception.ResourceNotFoundException;
import UAC.IFRI.AA.PlanningClassroom.Models.Enseignant;
import UAC.IFRI.AA.PlanningClassroom.Models.ModelRequest.PutPasswordField;
import UAC.IFRI.AA.PlanningClassroom.Models.ModelResponse.UserResponse;
import UAC.IFRI.AA.PlanningClassroom.Models.Role;
import UAC.IFRI.AA.PlanningClassroom.Models.RoleName;
import UAC.IFRI.AA.PlanningClassroom.Models.User;
import UAC.IFRI.AA.PlanningClassroom.Payload.ApiResponse;
import UAC.IFRI.AA.PlanningClassroom.Payload.SignUpRequest;
import UAC.IFRI.AA.PlanningClassroom.Repository.EnseignantRepository;
import UAC.IFRI.AA.PlanningClassroom.Repository.EtudiantRepository;
import UAC.IFRI.AA.PlanningClassroom.Repository.RoleRepository;
import UAC.IFRI.AA.PlanningClassroom.Repository.UserRepository;
import UAC.IFRI.AA.PlanningClassroom.Security.CurrentUser;
import UAC.IFRI.AA.PlanningClassroom.Security.UserPrincipal;
import UAC.IFRI.AA.PlanningClassroom.Service.MailContent;
import UAC.IFRI.AA.PlanningClassroom.Service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
@CrossOrigin("*")
@RestController
public class UserController
{
    @Autowired
    UserRepository userRepository;
    @Autowired
    EnseignantRepository enseignantRepository;
    @Autowired
    EtudiantRepository etudiantRepository;
    @Autowired
    MailService mailService;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @DeleteMapping("/api/user/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable String email)
    {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent())
        {
            userRepository.delete(user.get());
            return ResponseEntity.ok(null);
        }
        throw new ResourceNotFoundException("User", "Email", "L'User n'est pas retrouver");
    }


    @PreAuthorize("hasRole('SUPERENSEIGNANT')")
    @PutMapping("/api/upgrade/{username}")
    public ResponseEntity<?> activeAccount(@PathVariable String username) throws MessagingException {
        Optional<Enseignant> user = enseignantRepository.findByUsername(username);
        if(user.isPresent())
        {
            Enseignant enseignant = user.get();
            enseignant.setState(true);
            enseignant.setTitulaire(true);
            enseignant = enseignantRepository.save(enseignant);

            String title ="CONFIRMATION D'INSCRIPTION";
            MailContent mailContent = new MailContent(enseignant.getLastName(), enseignant.getFirstName(), enseignant.getEmail(), enseignant.getNumber(), enseignant.getSexe(), title);

            String body = mailContent.successConfirmMSG();
            mailService.sendHtmlEmail(enseignant.getEmail(),title, body);

            UserResponse userResponse = new UserResponse();

            userResponse.setId(enseignant.getId());
            userResponse.setFirstName(enseignant.getFirstName());
            userResponse.setEmail(enseignant.getEmail());
            userResponse.setLastName(enseignant.getLastName());
            userResponse.setNumber(enseignant.getNumber());
            userResponse.setUserName(enseignant.getUsername());

            return ResponseEntity.ok(userResponse);
        }
        throw new ResourceNotFoundException("User", "Enseignant", "L'Enseignant n'est pas retrouver");
    }


    @GetMapping("/api/invalid/enseignants")
    @PreAuthorize("hasRole('SUPERENSEIGNANT')")
    public ResponseEntity<?> invalidEnseignants(){
        return ResponseEntity.ok(enseignantRepository.findAllByState(false));
    }

    @GetMapping("/api/user/me")
    @PreAuthorize("hasRole('ETUDIANT')")
    public UserResponse getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserResponse userSummary = new UserResponse(currentUser.getId(),currentUser.getUsername(), currentUser.getLastName(), currentUser.getFirstName(), currentUser.getEmail(),currentUser.getNumber());
        return userSummary;
    }

    @PutMapping("/api/user/me")
    @PreAuthorize("hasRole('ETUDIANT')")
    public UserResponse PutCurrentUser(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody SignUpRequest user)
    {
        User userSummary =  userRepository.findByEmail(currentUser.getEmail()).get();

        userSummary.setEmail(user.getEmail());
        userSummary.setFirstName(user.getFirstName());
        userSummary.setLastName(user.getLastName());
        userSummary.setNumber(user.getNumber());
        userSummary.setSexe(user.getSexe());

        userSummary = userRepository.save(userSummary);

        UserResponse userResponse = new UserResponse();

        userResponse.setId(userSummary.getId());
        userResponse.setFirstName(userSummary.getFirstName());
        userResponse.setEmail(userSummary.getEmail());
        userResponse.setLastName(userSummary.getLastName());
        userResponse.setNumber(userSummary.getNumber());
        userResponse.setUserName(userSummary.getUsername());

        return  userResponse;
    }


    @PutMapping("/api/user/{email}")
    @PreAuthorize("hasRole('SUPERENSEIGNANT')")
    public ResponseEntity<?> AddRole(@CurrentUser UserPrincipal currentUser, @PathVariable String email)
    {
        RoleName role = RoleName.ROLE_SUPERENSEIGNANT;
        Optional<Enseignant> userA = enseignantRepository.findByEmail(email);
        if(userA.isPresent())
        {
            Enseignant user = userA.get();
            boolean trouver  = false;
            for(Role currentRole : user.getRoles())
            {
                if(currentRole.getName().equals(role))
                {
                    trouver = true;
                }
            }
            if(trouver)
            {
                throw new BadRequestException("No Match Role Or The User have : " + role);
            }else
            {
                Role userRole = roleRepository.findByName(role)
                        .orElseThrow(() -> new AppException("User Role not set."));

                user.getRoles().add(userRole);
                User result = enseignantRepository.save(user);
                return new ResponseEntity(new ApiResponse(true, "The : " + role + " is add"), HttpStatus.OK);
            }
        }else
        {
            throw new ResourceNotFoundException("User", "email", email);
        }
    }
    @PutMapping("/api/user/me/password")
    @PreAuthorize("hasRole('ETUDIANT')")
    public UserResponse PutCurrentUserPassword(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody PutPasswordField putPasswordField)
    {
        if(putPasswordField.getCurrentPassword() == null || putPasswordField.getNewPassword() == null)
        {
            throw new BadRequestException("Password Field is Null");
        }
        if(passwordEncoder.matches(putPasswordField.getCurrentPassword(), currentUser.getPassword()))
        {
            User userSummary =  userRepository.findByEmail(currentUser.getEmail()).get();

            userSummary.setPassword(passwordEncoder.encode(putPasswordField.getNewPassword()));

            userSummary = userRepository.save(userSummary);

            UserResponse userResponse = new UserResponse();

            userResponse.setFirstName(userSummary.getFirstName());
            userResponse.setEmail(userSummary.getEmail());
            userResponse.setId(userSummary.getId());
            userResponse.setLastName(userSummary.getLastName());
            userResponse.setNumber(userSummary.getNumber());
            userResponse.setUserName(userSummary.getUsername());

            return  userResponse;

        }else
        {
            throw new BadRequestException("Password Incorrect");
        }
    }

    @GetMapping("/api/user/me/role")
    @PreAuthorize("hasRole('ETUDIANT')")
    public ResponseEntity<Role> getCurrentUserRole(@CurrentUser UserPrincipal currentUser) {
        Optional<User> user = userRepository.findByEmail(currentUser.getEmail());

        List<Role> roleList = user.get().getRoles();
        for (Role role : roleList)
        {
            if(role.getName().equals(RoleName.ROLE_SUPERENSEIGNANT))
            {
                return new ResponseEntity<>(roleRepository.findByName(RoleName.ROLE_SUPERENSEIGNANT).get(), HttpStatus.OK);
            }
        }
        for (Role role : roleList)
        {
            if(role.getName().equals(RoleName.ROLE_ENSEIGNANT))
            {
                return new ResponseEntity<>(roleRepository.findByName(RoleName.ROLE_ENSEIGNANT).get(), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(roleRepository.findByName(RoleName.ROLE_ETUDIANT).get(), HttpStatus.OK);
    }
}
