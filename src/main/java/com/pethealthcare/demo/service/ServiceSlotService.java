package com.pethealthcare.demo.service;

import com.pethealthcare.demo.dto.request.GetSlotAvailableRequest;
import com.pethealthcare.demo.dto.request.GetVetAvailableRequest;
import com.pethealthcare.demo.dto.request.ServiceSlotCreateRequest;
import com.pethealthcare.demo.enums.ServiceSlotStatus;
import com.pethealthcare.demo.mapper.ServiceSlotMapper;
import com.pethealthcare.demo.model.BookingDetail;
import com.pethealthcare.demo.model.ServiceSlot;
import com.pethealthcare.demo.model.Slot;
import com.pethealthcare.demo.model.User;
import com.pethealthcare.demo.repository.BookingDetailRepository;
import com.pethealthcare.demo.repository.ServiceSlotRepository;
import com.pethealthcare.demo.repository.SlotRepository;
import com.pethealthcare.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceSlotService {
    @Autowired
    private ServiceSlotRepository serviceSlotRepository;

    @Autowired
    private ServiceSlotMapper serviceSlotMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SlotRepository slotRepository;
    @Autowired
    private BookingDetailRepository bookingDetailRepository;

    public List<ServiceSlot> getSlotAvailable(GetSlotAvailableRequest request) {
        User user = userRepository.findUserByUserId(request.getUserId());

        return serviceSlotRepository.findByUserAndDateAndStatus(user,
                request.getDate(), ServiceSlotStatus.AVAILABLE);
    }

    public List<ServiceSlot> getVetAvailable(GetVetAvailableRequest request) {
        return serviceSlotRepository.findServiceSlotByDateAndSlot_SlotIdAndAndStatus(request.getDate(),
                request.getSlotId(), ServiceSlotStatus.AVAILABLE);
    }

    public String addServiceSlots(List<ServiceSlotCreateRequest> requests) {
        for (ServiceSlotCreateRequest request : requests) {
            User user = userRepository.findUserByUserId(request.getUserId());
            Slot slot = slotRepository.findSlotBySlotId(request.getSlotId());

            if (slot == null) {
                return "Slot with ID " + request.getSlotId() + " is not existed";
            }

            boolean existed = serviceSlotRepository.existsByUserAndSlotAndDate(user, slot, request.getDate());
            if (!existed) {
                ServiceSlot serviceSlot = serviceSlotMapper.toServiceSlot(request);
                serviceSlot.setStatus(ServiceSlotStatus.AVAILABLE);
                serviceSlot.setUser(user);
                serviceSlot.setSlot(slot);
                serviceSlotRepository.save(serviceSlot);
            } else {
                return "ServiceSlot with user " + request.getUserId() + " and slot " + request.getSlotId() + " is already existed";
            }
        }

        return "Insert Service Slots Successfully";
    }

    public void updateServiceSlotStatus(int userId, LocalDate date, int slotId, ServiceSlotStatus status) {
        User user = userRepository.findUserByUserId(userId);
        Slot slot = slotRepository.findSlotBySlotId(slotId);
        ServiceSlot serviceSlot = serviceSlotRepository.findServiceSlotByUserAndDateAndSlot(user, date, slot);
        if (serviceSlot == null) {
            throw new RuntimeException("Service Slot is not existed");
        }
        serviceSlot.setStatus(status);

        serviceSlotRepository.save(serviceSlot);
    }

    public void cancelSlot(int userId, LocalDate date, int slotId) {
        User user = userRepository.findUserByUserId(userId);
        Slot slot = slotRepository.findSlotBySlotId(slotId);
        ServiceSlot serviceSlot = serviceSlotRepository.findServiceSlotByUserAndDateAndSlot(user, date, slot);
        if (serviceSlot == null) {
            throw new RuntimeException("Service Slot is not existed");
        }
        serviceSlot.setStatus(ServiceSlotStatus.AVAILABLE);

        serviceSlotRepository.save(serviceSlot);
    }

    public List<ServiceSlot> getSlotNotCreate(GetSlotAvailableRequest request) {
        User user = userRepository.findUserByUserId(request.getUserId());

        return serviceSlotRepository.findByUserAndDate(user,
                request.getDate());
    }

    public List<ServiceSlot> cancelServiceSlot(LocalDate date, int vetId) {
        User user = userRepository.findUserByUserId(vetId);
        List<ServiceSlot> serviceSlotsByVet = serviceSlotRepository.getServiceSlotByUserAndDate(user, date);
        List<ServiceSlot> serviceSlots = new ArrayList<>();
        List<BookingDetail> bookingDetails = bookingDetailRepository.findBookingDetailByUser_UserIdAndDate(vetId, date);
        for (BookingDetail bookingDetail : bookingDetails) {
            bookingDetail.setVetCancelled(true);
            bookingDetailRepository.save(bookingDetail);
        }

        for (ServiceSlot serviceSlot : serviceSlotsByVet) {
            serviceSlot.setStatus(ServiceSlotStatus.CANCELLED);
            serviceSlotRepository.save(serviceSlot);
            serviceSlots.add(serviceSlot);
        }
        return serviceSlots;
    }

    public List<ServiceSlot> getSlotByDateAndVet(int vetId, LocalDate date) {
        return serviceSlotRepository.findServiceSlotByDateAndUser_UserId(date, vetId);
    }

}
