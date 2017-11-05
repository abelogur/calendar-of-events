package ru.korbit.cecommon.dao;

import lombok.NonNull;
import ru.korbit.cecommon.domain.Showtime;

import java.time.LocalDateTime;
import java.util.stream.Stream;

/**
 * Created by Artur Belogur on 17.10.17.
 */
public interface ShowtimeDao extends GenericDao<Showtime> {

    Stream<Showtime> getByHallOnDay(@NonNull Long hallId, LocalDateTime dateFrom);
}
