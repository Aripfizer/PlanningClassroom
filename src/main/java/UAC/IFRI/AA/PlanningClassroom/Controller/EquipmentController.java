package UAC.IFRI.AA.PlanningClassroom.Controller;

import UAC.IFRI.AA.PlanningClassroom.Models.Equipment;
import UAC.IFRI.AA.PlanningClassroom.Repository.EquipmentRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin("*")
@RestController
public class EquipmentController
{
    @Autowired
    private EquipmentRepository equipmentRepository;


    @GetMapping("/api/equipments")
    @PreAuthorize("hasRole('ENSEIGNANT')")
    public List<Equipment> findAllEquipments()
    {
        return equipmentRepository.findAll();
    }


    @PostMapping("/api/equipment")
    @PreAuthorize("hasRole('SUPERENSEIGNANT')")
    public Equipment saveEquipment(@Valid @RequestBody Equipment materiel)
    {
        return equipmentRepository.save(materiel);
    }


    @GetMapping("/api/equipment/{id}")
    @PreAuthorize("hasRole('ENSEIGNANT')")
    public Equipment getOneEquipment(@Valid @PathVariable Long id)
    {
        return equipmentRepository.findById(id).get();
    }

    @PutMapping("/api/equipments/{id}")
    @PreAuthorize("hasRole('SUPERENSEIGNANT')")
    public Equipment updateEquipment(@Valid @RequestBody Equipment materiel, @PathVariable Long id) throws NotFoundException {
        return equipmentRepository.findById(id).map(m -> {
            m.setHardwareName(materiel.getHardwareName());
            return equipmentRepository.save(m);
        }).orElseThrow(() -> new NotFoundException("Le Matériel ayant l'id " + id + " n'existe pas"));
    }

    @DeleteMapping("/api/equipment/{id}")
    @PreAuthorize("hasRole('SUPERENSEIGNANT')")
    public ResponseEntity<?> deleteEquipment(@PathVariable Long id) throws NotFoundException {
        return equipmentRepository.findById(id).map(materiel -> {
            equipmentRepository.delete(materiel);
            return ResponseEntity.ok(null);
        }).orElseThrow(() -> new NotFoundException("Le Matériel ayant L'id : " + id + " n'existe pas"));
    }
}
