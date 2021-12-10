package UAC.IFRI.AA.PlanningClassroom.Controller;

import UAC.IFRI.AA.PlanningClassroom.Exception.BadRequestException;
import UAC.IFRI.AA.PlanningClassroom.Models.Classroom;
import UAC.IFRI.AA.PlanningClassroom.Models.ModelRequest.ReservationClassroomRequest;
import UAC.IFRI.AA.PlanningClassroom.Models.ReservationClassroom;
import UAC.IFRI.AA.PlanningClassroom.Models.User;
import UAC.IFRI.AA.PlanningClassroom.Repository.ClassroomRepository;
import UAC.IFRI.AA.PlanningClassroom.Repository.ReservationClassroomRepository;
import UAC.IFRI.AA.PlanningClassroom.Repository.UserRepository;
import UAC.IFRI.AA.PlanningClassroom.Security.CurrentUser;
import UAC.IFRI.AA.PlanningClassroom.Security.UserPrincipal;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Api( description="API pour les réservations de salle de cours")
@CrossOrigin("*")
@RestController
public class ReservationClassroomController
{
    @Autowired
    ClassroomRepository classroomRepository;
    @Autowired
    ReservationClassroomRepository rcr;
    @Autowired
    UserRepository userRepository;

    @ApiOperation(value = "Afficher la liste des Reservation de salle de cours effectuer par un utilisateur")
    @GetMapping("/api/user/class/reservation")
    @PreAuthorize("hasRole('ENSEIGNANT')")
    public List<Classroom> findAllClassroomReservationForUser(@CurrentUser UserPrincipal currenUser)
    {
        List<Classroom> classroomList = new ArrayList<>();

        List<ReservationClassroom> reservationClassroomList=  rcr.findAllByUserEmailAndState(currenUser.getEmail(), true);
        if(reservationClassroomList != null)
        {
            for(ReservationClassroom reservationClassroom : reservationClassroomList)
            {
                classroomList.add(reservationClassroom.getClassroom());
            }
            return classroomList;
        }
        return classroomList;
    }



    @ApiOperation(value = "Faire une réservation de salle de cours en passant l'id de la salle en path et en body un objet ReservationClassroomRequest")
    @PostMapping("/api/classroom/{id}/reservation")
    @PreAuthorize("hasRole('ENSEIGNANT')")
    public ResponseEntity<?> saveClassroomReservation(@Valid @RequestBody ReservationClassroomRequest reservation, @PathVariable Long id, @CurrentUser UserPrincipal currentUser) throws NotFoundException {
        return classroomRepository.findById(id).map(classroom -> {
            //SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            //sdf3.format(timestamp);
            if(reservation.getDateStart().before(new Timestamp(new Date().getTime()))) throw new BadRequestException("Verifier la date et l'heure actuelle");
            if(reservation.getDateStart().after(reservation.getDateEnd())) throw new BadRequestException("La date et heur de debut est superieur a la date et heur de fin");
           if(rcr.existsByDateStartBetweenAndState(reservation.getDateStart(), reservation.getDateEnd(), true)) throw new BadRequestException("La salle indisponible");
           if (rcr.existsByDateEndBetweenAndState(reservation.getDateStart(), reservation.getDateEnd(), true)) throw new BadRequestException("La salle est indisponible");

            User user = userRepository.findByEmail(currentUser.getEmail()).get();
            ReservationClassroom reservationClassroom = new ReservationClassroom();
            reservationClassroom.setDateStart(reservation.getDateStart());
            reservationClassroom.setDateEnd(reservation.getDateEnd());
            reservationClassroom.setClassroom(classroom);
            reservationClassroom.setUser(user);

            return ResponseEntity.ok(rcr.save(reservationClassroom));
        }).orElseThrow(() -> new NotFoundException("La Salle ayant L'id : " + id + " n'existe pas"));
    }

    @ApiOperation(value = "Supprimer une salle de cours en passant son id en path, a condition qu'elle existe !")
    @DeleteMapping("/api/classroom/reservation/{id}")
    @PreAuthorize("hasRole('ENSEIGNANT')")
    public ResponseEntity<?> deleteClassroomReservation(@PathVariable Long id) throws NotFoundException {
        return rcr.findByIdAndState(id, true).map(reservationClassroom -> {
            reservationClassroom.setState(false);
            rcr.save(reservationClassroom);
            return ResponseEntity.ok(null);
        }).orElseThrow(() -> new NotFoundException("La reservation de Classe ayant L'id : " + id + " n'existe pas"));
    }
}
