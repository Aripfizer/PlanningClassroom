package UAC.IFRI.AA.PlanningClassroom.Controller;

import UAC.IFRI.AA.PlanningClassroom.Models.Classroom;
import UAC.IFRI.AA.PlanningClassroom.Repository.ClassroomRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin("*")
@RestController
public class ClassroomController
{
    @Autowired
    private ClassroomRepository classroomRepository;


    @GetMapping("/api/classrooms")
    @PreAuthorize("hasRole('ENSEIGNANT')")
    public List<Classroom> findAllClassrooms()
    {
        return classroomRepository.findAll();
    }


    @PostMapping("/api/classroom")
    @PreAuthorize("hasRole('SUPERENSEIGNANT')")
    public Classroom saveClassroom(@Valid @RequestBody Classroom classroom)
    {
        return classroomRepository.save(classroom);
    }


    @GetMapping("/api/classroom/{id}")
    @PreAuthorize("hasRole('ENSEIGNANT')")
    public Classroom getOneClassroom(@Valid @PathVariable Long id)
    {
        return classroomRepository.findById(id).get();
    }

    @PutMapping("/api/classroom/{id}")
    @PreAuthorize("hasRole('SUPERENSEIGNANT')")
    public Classroom updateClassroom(@Valid @RequestBody Classroom classroom, @PathVariable Long id) throws NotFoundException {
        return classroomRepository.findById(id).map(c -> {
            c.setName(classroom.getName());
            c.setEffectif(classroom.getEffectif());
            return classroomRepository.save(c);
        }).orElseThrow(() -> new NotFoundException("La Salle ayant l'id " + id + " n'existe pas"));
    }

    @DeleteMapping("/api/classroom/{id}")
    @PreAuthorize("hasRole('SUPERENSEIGNANT')")
    public ResponseEntity<?> deleteClassroom(@PathVariable Long id) throws NotFoundException {
        return classroomRepository.findById(id).map(classroom -> {
            classroomRepository.delete(classroom);
            return ResponseEntity.ok(null);
        }).orElseThrow(() -> new NotFoundException("La salle ayant L'id : " + id + " n'existe pas"));
    }
}
