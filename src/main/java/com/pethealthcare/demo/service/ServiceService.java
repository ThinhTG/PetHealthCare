package com.pethealthcare.demo.service;

import com.pethealthcare.demo.dto.request.ServiceCreateRequest;
import com.pethealthcare.demo.enums.ServiceStatus;
import com.pethealthcare.demo.mapper.ServiceMapper;
import com.pethealthcare.demo.model.BookingDetail;
import com.pethealthcare.demo.model.Services;
import com.pethealthcare.demo.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceService {
    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ServiceMapper serviceMapper;

    @Autowired
    private BookingDetailService bookingDetailService;

    public List<Services> getAllServices() {
        return serviceRepository.findAll();
    }

    public Services createService(ServiceCreateRequest request) {
        boolean exists = serviceRepository.existsByName(request.getName());
        if (!exists) {
            Services service = serviceMapper.toService(request);
            service.setImageUrl(request.getImageUrl());
            return serviceRepository.save(service);
        }
        return null;
    }

    public Services updateService(int serviceId, ServiceCreateRequest request) {
        Optional<Services> updateService = serviceRepository.findById(serviceId);
        if (updateService.isPresent()) {
            Services service = updateService.get();
            if(!service.getName().equals(request.getName())) {
                service.setName(request.getName());
            }
            if(!service.getDescription().equals(request.getDescription())) {
                service.setDescription(request.getDescription());
            }
            if(service.getPrice() != request.getPrice()) {
                service.setPrice(request.getPrice());
            }
            return serviceRepository.save(service);
        }

//        Services services = serviceMapper.toService(request);
        return null;
    }

    public List<BookingDetail> deleteService(int serviceId) {
        Services service = serviceRepository.findServicesByServiceId(serviceId);

        if (service != null && service.getStatus().equals(ServiceStatus.ACTIVE)) {
            List<BookingDetail> bookingDetails = service.getBookingDetails();
            List<BookingDetail> cancelledBookingDetails = new ArrayList<>();

            for (BookingDetail detail : bookingDetails) {
                if (detail.getStatus().equalsIgnoreCase("CONFIRMED")
                        || detail.getStatus().equalsIgnoreCase("WAITING")
                        || detail.getStatus().equalsIgnoreCase("COMPLETED")) {
                    cancelledBookingDetails.add(detail);
                }
            }

            service.setStatus(ServiceStatus.INACTIVE);
            serviceRepository.save(service);

            if (cancelledBookingDetails.isEmpty()) {
                return null;
            }

            return cancelledBookingDetails;
        }

        return null;
    }







    public Services getServiceById(int  serviceID) {

        return serviceRepository.findByServiceId(serviceID);
    }
}

