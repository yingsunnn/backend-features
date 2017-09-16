package ying.backend_features.jpa;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(name = "t_prescription")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "t_prescription_drug",
            joinColumns = {@JoinColumn(name = "prescription_id")},
            inverseJoinColumns = {@JoinColumn(name = "drug_id")})
    private List<Drug> drugs;

    @Column(name = "create_time")
    private ZonedDateTime createTime;
}
