package com.pethealthcare.demo.responsitory;

import com.pethealthcare.demo.model.ServiceSlot;
import com.pethealthcare.demo.model.Slot;
import com.pethealthcare.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface ServiceSlotRepository extends JpaRepository<ServiceSlot, Integer> {
    List<ServiceSlot> findByUserAndDateAndStatus(User user, LocalDate date, boolean status);

    boolean existsByUserAndSlotAndDate(User user, Slot slot, LocalDate date);

<<<<<<< HEAD
    ServiceSlot findServiceSlotByUserAndDateAndSlot (User user, LocalDate date, Slot slot);

    List<ServiceSlot> findByUserAndDate(User user, LocalDate date);
=======
    ServiceSlot findByUserAndDateAndSlot(User user, Date date, Slot slot);
>>>>>>> 7e37d1433ea47f0f198cb908eacd0060d9d451d3
}
