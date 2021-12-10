package UAC.IFRI.AA.PlanningClassroom.Controller;

import UAC.IFRI.AA.PlanningClassroom.Models.Classroom;
import UAC.IFRI.AA.PlanningClassroom.Repository.ClassroomRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
@Api( description="API pour gerer les operations CRUD sur les salles de cours")
@CrossOrigin("*")
@RestController
public class ClassroomController
{
    @Autowired
    private ClassroomRepository classroomRepository;

    @ApiOperation(value = "La liste des Salles de cours enregistrées dans la base de donnée")
    @GetMapping("/api/classrooms")
    @PreAuthorize("hasRole('ENSEIGNANT')")
    public List<Classroom> findAllClassrooms()
    {
        return classroomRepository.findAll();
    }

    @ApiOperation(value = "Enregistrer une salle de cours")
    @PostMapping("/api/classroom")
    @PreAuthorize("hasRole('SUPERENSEIGNANT')")
    public Classroom saveClassroom(@Valid @RequestBody Classroom classroom)
    {
        return classroomRepository.save(classroom);
    }

    @ApiOperation(value = "Retourne une Salle de cours correspondant a son id passer dans le path a condition qu'elle existe !")
    @GetMapping("/api/classroom/{id}")
    @PreAuthorize("hasRole('ENSEIGNANT')")
    public Classroom getOneClassroom(@Valid @PathVariable Long id)
    {
        return classroomRepository.findById(id).get();
    }

    @ApiOperation(value = "Mets a jour une Salle de cours en passant son id et un objet Classroom en body")
    @PutMapping("/api/classroom/{id}")
    @PreAuthorize("hasRole('SUPERENSEIGNANT')")
    public Classroom updateClassroom(@Valid @RequestBody Classroom classroom, @PathVariable Long id) throws NotFoundException {
        return classroomRepository.findById(id).map(c -> {
            c.setName(classroom.getName());
            c.setEffectif(classroom.getEffectif());
            return classroomRepository.save(c);
        }).orElseThrow(() -> new NotFoundException("La Salle ayant l'id " + id + " n'existe pas"));
    }

    @ApiOperation(value = "Supprimer une salle de cours en renseignant son id, a condition que cette derniere existe !")
    @DeleteMapping("/api/classroom/{id}")
    @PreAuthorize("hasRole('SUPERENSEIGNANT')")
    public ResponseEntity<?> deleteClassroom(@PathVariable Long id) throws NotFoundException {
        return classroomRepository.findById(id).map(classroom -> {
            classroomRepository.delete(classroom);
            return ResponseEntity.ok(null);
        }).orElseThrow(() -> new NotFoundException("La salle ayant L'id : " + id + " n'existe pas"));
    }
}
