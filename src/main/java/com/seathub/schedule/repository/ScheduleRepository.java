package com.seathub.schedule.repository;

import com.seathub.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByProductIdOrderByStartAtAsc(Long productId);
}
