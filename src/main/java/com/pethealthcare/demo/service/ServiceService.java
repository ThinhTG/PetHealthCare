package com.pethealthcare.demo.service;

import com.pethealthcare.demo.dto.request.ServiceCreateRequest;
import com.pethealthcare.demo.enums.BookingDetailStatus;
import com.pethealthcare.demo.mapper.ServiceMapper;
import com.pethealthcare.demo.model.BookingDetail;
import com.pethealthcare.demo.model.Services;
import com.pethealthcare.demo.repository.BookingDetailRepository;
import com.pethealthcare.demo.repository.ServiceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceService {
    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ServiceMapper serviceMapper;

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    @Autowired
    private BookingDetailRepository bookingDetailRepository;

    public List<Services> getAllServices() {
        return serviceRepository.findAll();
    }

    public List<Services> getAllActiveServices() {
        return serviceRepository.findAllByStatus(true);
    }

    public Services createService(ServiceCreateRequest request, MultipartFile file) throws IOException {
        Services exists = serviceRepository.findServicesByName(request.getName());
        if (exists != null && exists.isStatus()) {
            return null;
        }
        Services service = serviceMapper.toService(request);
        service.setStatus(true);
        if (file != null && !file.isEmpty()) {
            String fileName = firebaseStorageService.uploadFile(file);
            service.setImageUrl(fileName);
        }
        return serviceRepository.save(service);
    }

    public Services updateService(int serviceId, ServiceCreateRequest request, MultipartFile file) throws IOException {
        Services service = serviceRepository.findServicesByServiceId(serviceId);
        List<BookingDetail> bookingDetail = bookingDetailRepository.
                findBookingDetailByServices_ServiceIdAndStatusIn(serviceId,
                        Arrays.asList(BookingDetailStatus.WAITING, BookingDetailStatus.CONFIRMED, BookingDetailStatus.EXAMINING));
        if (!bookingDetail.isEmpty()) {
            return null;
        }
        if (service != null) {
            if(!service.getName().equals(request.getName())) {
                service.setName(request.getName());
            }
            if(!service.getDescription().equals(request.getDescription())) {
                service.setDescription(request.getDescription());
            }
            if(service.getPrice() != request.getPrice()) {
                service.setPrice(request.getPrice());
            }
            if (file != null && !file.isEmpty()) {
                String fileName = firebaseStorageService.uploadFile(file);
                service.setImageUrl(fileName);
            }
            return serviceRepository.save(service);
        }

        return null;
    }

    public boolean deleteService(int serviceId) {
        Services service = serviceRepository.findServicesByServiceId(serviceId);
        List<BookingDetail> bookingDetail = bookingDetailRepository.
                findBookingDetailByServices_ServiceIdAndStatusIn(serviceId,
                        Arrays.asList(BookingDetailStatus.WAITING, BookingDetailStatus.CONFIRMED, BookingDetailStatus.EXAMINING));
        if (!bookingDetail.isEmpty()) {
            return false;
        }
        service.setStatus(false);
        serviceRepository.save(service);
        return true;
    }

    public Services getServiceById(int  serviceID) {

        return serviceRepository.findByServiceId(serviceID);
    }
}

