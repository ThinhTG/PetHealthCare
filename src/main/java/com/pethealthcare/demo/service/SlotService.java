package com.pethealthcare.demo.service;

import com.pethealthcare.demo.dto.request.SlotCreateRequest;
import com.pethealthcare.demo.model.Slot;
import com.pethealthcare.demo.responsitory.SlotRepository;
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
