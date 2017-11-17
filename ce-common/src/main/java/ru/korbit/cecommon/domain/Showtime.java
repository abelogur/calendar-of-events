package ru.korbit.cecommon.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.korbit.cecommon.packet.GetIdable;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Created by Artur Belogur on 16.10.17.
 */
@Entity
@Table(name = "showtime")
@IdClass(CinemaEventHall.class)
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@ToString(exclude={"cinemaEvent", "hall"})
public class Showtime implements GetIdable {

    @JsonIgnore
    @Id
    @ManyToOne
    @JoinColumn(name = "cinema_event_id", referencedColumnName = "id")
    private CinemaEvent cinemaEvent;

    @JsonIgnore
    @Id
    @ManyToOne
    @JoinColumn(name = "hall_id", referencedColumnName = "id")
    private Hall hall;

    @NonNull
    @Column(name = "format")
    private String format;

    @NonNull
    @Column(name = "start_time")
    private ZonedDateTime startTime;

    @NonNull
    @Column(name = "price")
    private Integer price;

    @JsonIgnore
    @NonNull
    @Id
    @Column(name = "rambler_id")
    private Long ramblerId;

    @Override
    public Serializable getId() {
        return new CinemaEventHall(cinemaEvent.getId(), hall.getId(), ramblerId);
    }
}
