package com.pethealthcare.demo.repository;

import com.pethealthcare.demo.model.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Integer> {
    Slot findSlotBySlotId(int slotId);
}
