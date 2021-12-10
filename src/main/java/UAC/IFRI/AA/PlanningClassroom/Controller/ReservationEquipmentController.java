package UAC.IFRI.AA.PlanningClassroom.Controller;

import UAC.IFRI.AA.PlanningClassroom.Exception.BadRequestException;
import UAC.IFRI.AA.PlanningClassroom.Models.Equipment;
import UAC.IFRI.AA.PlanningClassroom.Models.ModelRequest.ReservationMaterielRequest;
import UAC.IFRI.AA.PlanningClassroom.Models.ModelResponse.EquipmentResponse;
import UAC.IFRI.AA.PlanningClassroom.Models.ReservationMateriel;
import UAC.IFRI.AA.PlanningClassroom.Models.User;
import UAC.IFRI.AA.PlanningClassroom.Repository.EquipmentRepository;
import UAC.IFRI.AA.PlanningClassroom.Repository.ReservationMaterielRepository;
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
import java.util.Optional;
@Api( description="API pour la réservation des Matériels")
@CrossOrigin("*")
@RestController
public class ReservationEquipmentController
{
    @Autowired
    EquipmentRepository equipmentRepository;
    @Autowired
    ReservationMaterielRepository rmr;
    @Autowired
    UserRepository userRepository;

    @ApiOperation(value = "Affiche la liste des Matériels réservés par un utilisateur")
    @GetMapping("/api/user/equipment/reservation")
    @PreAuthorize("hasRole('ENSEIGNANT')")
    public List<EquipmentResponse> findAllEquipmentReservationForUser(@CurrentUser UserPrincipal currenUser)
    {
        List<EquipmentResponse> equipmentList = new ArrayList<>();

        List<ReservationMateriel> reservationMaterielList=  rmr.findAllByUserEmailAndState(currenUser.getEmail(), true);
        if(reservationMaterielList != null)
        {
            for(ReservationMateriel reservationMateriel : reservationMaterielList)
            {
                for(Equipment equipment : reservationMateriel.getMateriels())
                {
                    EquipmentResponse equipmentResponse = new EquipmentResponse();
                    equipmentResponse.setDateStart((Timestamp) reservationMateriel.getDateStart());
                    equipmentResponse.setDateEnd((Timestamp) reservationMateriel.getDateEnd());
                    equipmentResponse.setHardwareName(equipment.getHardwareName());
                    equipmentResponse.setId(equipment.getId());
                    equipmentList.add(equipmentResponse);
                }

            }
            return equipmentList;
        }
        return equipmentList;
    }

    @ApiOperation(value = "Réservation des Matériel en passant en body un objet ReservationMaterielRequest")
    @PostMapping("/api/equipment/reservation")
    @PreAuthorize("hasRole('ENSEIGNANT')")
    public ResponseEntity<?> saveMaterielsReservation(@Valid @RequestBody ReservationMaterielRequest reservation, @CurrentUser UserPrincipal currentUser)
    {
        User user = userRepository.findByEmail(currentUser.getEmail()).get();
        ReservationMateriel reservationMateriel = new ReservationMateriel();
        reservationMateriel.setUser(user);
        reservationMateriel.setDateStart(reservation.getDateStart());
        reservationMateriel.setDateEnd(reservation.getDateEnd());


        if(reservation.getDateStart().before(new Timestamp(new Date().getTime()))) throw new BadRequestException("Verifier la date et l'heure actuelle");
        if(reservation.getDateStart().after(reservation.getDateEnd())) throw new BadRequestException("La date et heur de debut est superieur a la date et heur de fin");
        if(rmr.existsByDateStartBetweenAndState(reservation.getDateStart(), reservation.getDateEnd(), true)) throw new BadRequestException("La salle indisponible");
        if (rmr.existsByDateEndBetweenAndState(reservation.getDateStart(), reservation.getDateEnd(), true)) throw new BadRequestException("La salle est indisponible");

        for(Long id : reservation.getMateriel_Id())
        {
            Optional<Equipment> equipment = equipmentRepository.findById(id);
            if(equipment.isPresent())
            {
                reservationMateriel.getMateriels().add(equipment.get());
                reservationMateriel = rmr.save(reservationMateriel);

                Equipment eq = equipment.get();

                eq.getReservationMateriels().add(reservationMateriel);
                equipmentRepository.save(eq);

            }
        }
        return ResponseEntity.ok(reservationMateriel);

    }

    @ApiOperation(value = "Supprimer une reservation en passant son id, a codition qu'elle existe !")
    @DeleteMapping("/api/equipment/reservation/{id}")
    @PreAuthorize("hasRole('ENSEIGNANT')")
    public ResponseEntity<?> deleteEquipmentReservation(@PathVariable Long id) throws NotFoundException {
        return rmr.findByIdAndState(id, true).map(reservationMateriel -> {
            reservationMateriel.setState(false);
            rmr.save(reservationMateriel);
            return ResponseEntity.ok(null);
        }).orElseThrow(() -> new NotFoundException("La reservation du materiel ayant L'id : " + id + " n'existe pas"));
    }

}
