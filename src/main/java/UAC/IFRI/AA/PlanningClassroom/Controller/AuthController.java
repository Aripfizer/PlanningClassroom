package UAC.IFRI.AA.PlanningClassroom.Controller;

import UAC.IFRI.AA.PlanningClassroom.Exception.AppException;
import UAC.IFRI.AA.PlanningClassroom.Exception.BadRequestException;
import UAC.IFRI.AA.PlanningClassroom.Exception.ResourceNotFoundException;
import UAC.IFRI.AA.PlanningClassroom.Models.*;
import UAC.IFRI.AA.PlanningClassroom.Payload.ApiResponse;
import UAC.IFRI.AA.PlanningClassroom.Payload.JwtAuthenticationResponse;
import UAC.IFRI.AA.PlanningClassroom.Payload.LoginRequest;
import UAC.IFRI.AA.PlanningClassroom.Payload.SignUpRequest;
import UAC.IFRI.AA.PlanningClassroom.Repository.EnseignantRepository;
import UAC.IFRI.AA.PlanningClassroom.Repository.EtudiantRepository;
import UAC.IFRI.AA.PlanningClassroom.Repository.RoleRepository;
import UAC.IFRI.AA.PlanningClassroom.Repository.UserRepository;
import UAC.IFRI.AA.PlanningClassroom.Security.JwtTokenProvider;
import UAC.IFRI.AA.PlanningClassroom.Service.MailContent;
import UAC.IFRI.AA.PlanningClassroom.Service.MailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;
@Api( description="API pour l'authentification des Etudiants et Enseignants")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    EnseignantRepository enseignantRepository;
    @Autowired
    EtudiantRepository etudiantRepository;
   @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    MailService mailService;

    @ApiOperation(value = "La connexion d'un Utilisateur")
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }
    @ApiOperation(value = "Inscription d'un Utilisateur")
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) throws MessagingException {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }
        User user;
        // Creating user's account
        if(signUpRequest.getUserType().equals("ETUDIANT"))
        {
            Etudiant student = new Etudiant();
            student.setUsername(signUpRequest.getUsername());
            student.setLastName(signUpRequest.getLastName());
            student.setFirstName(signUpRequest.getFirstName());
            student.setEmail(signUpRequest.getEmail());
            student.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
            student.setNumber(signUpRequest.getNumber());
            student.setSexe(signUpRequest.getSexe());
            student.setNoMatricule(signUpRequest.getNoMatricule());
            student.setInscrit(false);

            Role userRole = roleRepository.findByName(RoleName.ROLE_ETUDIANT)
                    .orElseThrow(() -> new AppException("User Role: ETUDIANT not set."));

            student.getRoles().add(userRole);

            user = etudiantRepository.save(student);

        }
        else {
            Enseignant teacher = new Enseignant();
            teacher.setUsername(signUpRequest.getUsername());
            teacher.setLastName(signUpRequest.getLastName());
            teacher.setFirstName(signUpRequest.getFirstName());
            teacher.setEmail(signUpRequest.getEmail());
            teacher.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
            teacher.setNumber(signUpRequest.getNumber());
            teacher.setSexe(signUpRequest.getSexe());

            Role userRole1 = roleRepository.findByName(RoleName.ROLE_ENSEIGNANT)
                    .orElseThrow(() -> new AppException("User Role: ENSEIGNANT not set."));
            Role userRole2 = roleRepository.findByName(RoleName.ROLE_ETUDIANT)
                    .orElseThrow(() -> new AppException("User Role: ENSEIGNANT not set."));


            teacher.getRoles().add(userRole2);
            teacher.getRoles().add(userRole1);
            user = enseignantRepository.save(teacher);

            String title ="DEMANDE DE CONFIRMATION D'IDENTITE";


            MailContent mailContent = new MailContent(user.getLastName(), user.getFirstName(), user.getEmail(), user.getNumber(), user.getSexe(), title);

            String body = mailContent.generateEnseignantMSG();
            mailService.sendHtmlEmail("riravecariel@gmail.com",title, body);
        }



        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(user.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }




    @ApiOperation(value = "Modifier l'email d'un Utilisateur")
    @PutMapping("email/{email}")
    public  ResponseEntity<?> updateEmail(@PathVariable String email, @Valid @RequestBody LoginRequest loginRequest) {
        Optional<Enseignant> user = enseignantRepository.findByUsernameOrEmailAndPassword(loginRequest.getUsernameOrEmail(), loginRequest.getUsernameOrEmail(), passwordEncoder.encode(loginRequest.getPassword()));
        if(user.isPresent() && email != user.get().getEmail() && !user.get().isState())
        {
            if(!userRepository.existsByEmail(email))
            {
                Enseignant utilisateur = user.get();
                utilisateur.setEmail(email);
                utilisateur = enseignantRepository.save(utilisateur);
                return ResponseEntity.ok(utilisateur);
            }
            throw new BadRequestException("L'email est deja utiliser");
        }
        throw new ResourceNotFoundException("User", "Email", "Le user n'existe pas ou le compte est actif");
    }
}