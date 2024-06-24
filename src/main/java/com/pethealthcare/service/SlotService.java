package com.pethealthcare.service;

import com.pethealthcare.dto.request.SlotCreateRequest;
import com.pethealthcare.model.Slot;
import com.pethealthcare.responsitory.SlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SlotService {
    @Autowired
    private SlotRepository slotRepository;

    public String addSlot(SlotCreateRequest request) {
        Slot slot = new Slot();
        slot.setStartTime(request.getStartTime());
        slot.setEndTime(request.getEndTime());
        slotRepository.save(slot);
        return "Slot added successfully";
    }
}
