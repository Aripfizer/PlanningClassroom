package UAC.IFRI.AA.PlanningClassroom.Models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationMateriel extends Reservation
{
    /*
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "reservationMateriel")
    private List<Materiel> materiels = new ArrayList<>();

     */

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "price_menu",
            joinColumns = { @JoinColumn(name = "resMat_id", nullable = true) },
            inverseJoinColumns = { @JoinColumn(name = "materiel_id", nullable = true) })
    @JsonIgnore
    private List<Equipment> materiels = new ArrayList<>();

    /*
    @Size(min = 1, max = 2)
    private List<Materiel> materiels = new ArrayList<>();

     */
}
