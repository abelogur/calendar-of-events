package ru.korbit.cecommon.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artur Belogur on 10.10.17.
 */
@Entity
@Table(name = "event_types")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@ToString(exclude = {"events"})
public class EventType {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    @NonNull private String name;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "event_event_types",
            joinColumns = @JoinColumn(name = "event_type_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"))
    private List<Event> events = new ArrayList<>();
}
