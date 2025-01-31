package ru.zavorykin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zavorykin.model.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}