package com.pethealthcare.demo.service;

import com.pethealthcare.demo.dto.request.BookingDetailUpdateRequest;
import com.pethealthcare.demo.enums.ServiceStatus;
import com.pethealthcare.demo.model.*;
import com.pethealthcare.demo.repository.*;
import com.pethealthcare.demo.repository.UserRepository;
import com.pethealthcare.demo.response.MostUsedServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class BookingDetailService {
    @Autowired
    private BookingDetailRepository bookingDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    public List<BookingDetail> getAllBookingDetail() {
        return bookingDetailRepository.findAll();
    }


    public List<BookingDetail> getAllBookingDetailNeedCage() {
        List<BookingDetail> bookingDetails = bookingDetailRepository.findAll();
        List<BookingDetail> bookingDetailsNeedCage = new ArrayList<BookingDetail>();
        for (BookingDetail bookingDetail : bookingDetails) {
            if (bookingDetail.isNeedCage()) {
                bookingDetailsNeedCage.add(bookingDetail);
            }

        }
        return bookingDetailsNeedCage;
    }


    public BookingDetail updateNeedCage(int bookingDetailId) {
        Optional<BookingDetail> optionalBookingDetail = bookingDetailRepository.findById(bookingDetailId);
        if (optionalBookingDetail.isPresent()) {
            BookingDetail bookingDetail = optionalBookingDetail.get();
            if (!bookingDetail.isNeedCage()) {
                bookingDetail.setNeedCage(true);
                // Save updated user
            } else {
                bookingDetail.setNeedCage(false);
            }
            bookingDetailRepository.save(bookingDetail);
            return bookingDetail;
        } else {
            return null;
        }
    }

    public List<BookingDetail> getBookingDetailByBookingId(int bookingId) {
        Booking booking = new Booking();
        booking.setBookingId(bookingId);
        return bookingDetailRepository.getBookingDetailsByBooking(booking);
    }

    public List<BookingDetail> getBookingDetailByDate(Date date) {
        return bookingDetailRepository.findBookingDetailsFromDate(date);
    }

    public List<BookingDetail> getBookingDetailByNeedCage() {
        return bookingDetailRepository.findBookingDetailByNeedCage(true);
    }

    public List<BookingDetail> getBookingDetailByUser(int userId) {
        User user = userRepository.findUserByUserId(userId);
        return bookingDetailRepository.getBookingDetailByuser(user);


    }

    public List<BookingDetail> getBookingDetailStatusByVet(int vetId) {
        User user = userRepository.findUserByUserId(vetId);
        List<BookingDetail> bookingDetails = bookingDetailRepository.getBookingDetailByuser(user);
        List<BookingDetail> nonCancelledBookingDetails = new ArrayList<>();

        for (BookingDetail bookingDetail : bookingDetails) {
            if (!bookingDetail.getStatus().equalsIgnoreCase("CANCELLED")) {
                nonCancelledBookingDetails.add(bookingDetail);
            }
        }

        return nonCancelledBookingDetails;
    }



    public List<BookingDetail> getBookingDetailByCus(int cusId) {
        User user = userRepository.findUserByUserId(cusId);
        List<Booking> bookings = bookingRepository.getBookingByUser(user);
        List<BookingDetail> bookingDetails = new ArrayList<>();
        List<BookingDetail> bookingDetailByBooking;
        for (Booking booking : bookings) {
            bookingDetailByBooking = bookingDetailRepository.findBookingDetailByBooking(booking);
            for (BookingDetail bookingDetail : bookingDetailByBooking) {
                if (bookingDetail.getStatus().equalsIgnoreCase("COMPLETED")) {
                    bookingDetails.add(bookingDetail);
                }
            }
        }
        return bookingDetails;
    }

    public List<BookingDetail> getBookingDetailBySerivceInactive(){
        List<BookingDetail> bookingDetails = bookingDetailRepository.findAll();
        List<BookingDetail> bookingDetailByServiceInactive = new ArrayList<>();
        for (BookingDetail bookingDetail : bookingDetails) {
            if (bookingDetail.getServices().getStatus().equals(ServiceStatus.INACTIVE)) {
                bookingDetailByServiceInactive.add(bookingDetail);
            }
        }
        return bookingDetailByServiceInactive;
    }

    public BookingDetail updateBookingDetail(int id, BookingDetailUpdateRequest request) {
        BookingDetail bookingDetail = bookingDetailRepository.findBookingDetailByBookingDetailId(id);

        if (bookingDetail != null) {
            bookingDetail.setServices(request.getService());
            bookingDetail.setDate(request.getServiceSlot().getDate());
            bookingDetail.setSlot(request.getServiceSlot().getSlot());
            bookingDetail.setUser(request.getServiceSlot().getUser());
            bookingDetailRepository.save(bookingDetail);
        }

        return bookingDetail;
    }


    public List<BookingDetail> getBookingDetailByStatus(String status) {
        return bookingDetailRepository.getBookingDetailByStatus(status);
    }

    public void deleteBookingDetail(int bookingDetailId) {
        BookingDetail bookingDetail = bookingDetailRepository.findBookingDetailByBookingDetailId(bookingDetailId);
        bookingDetail.setStatus("CANCELLED");
        bookingDetailRepository.save(bookingDetail);

        List<BookingDetail> bookingDetails = bookingDetailRepository.getBookingDetailsByBooking(bookingDetail.getBooking());

        boolean allCancelled = bookingDetails.stream().allMatch(detail -> detail.getStatus().equalsIgnoreCase("CANCELLED"));


        if (allCancelled) {
            Booking booking = bookingDetail.getBooking();
            booking.setStatus("CANCELLED");
            bookingRepository.save(booking);
        }
    }

    public BookingDetail updateStatusBookingDetail(int bookingDetailId, String status) {
        BookingDetail bookingDetail = bookingDetailRepository.findBookingDetailByBookingDetailId(bookingDetailId);
        bookingDetail.setStatus(status);
        return bookingDetailRepository.save(bookingDetail);
    }

    public List<BookingDetail> getBookingDetailByStayCage() {

        List<Pet> pets = petRepository.getPetByStayCage(true);

        List<BookingDetail> bookingDetails = new ArrayList<>();

        for (Pet pet : pets) {
            bookingDetails.addAll(bookingDetailRepository.getBookingDetailByPet(pet));
        }

        return bookingDetails;
    }

    public void updateStatusByBookingId(int bookingId, String status) {
        Booking booking = bookingRepository.findBookingByBookingId(bookingId);
        List<BookingDetail> bookingDetails = bookingDetailRepository.getBookingDetailsByBooking(booking);
        for (BookingDetail detail : bookingDetails) {
            detail.setStatus(status);
        }
        bookingDetailRepository.saveAll(bookingDetails);
    }

    public MostUsedServiceResponse mostUsedService(int month, int year) {
        List<BookingDetail> bookingDetails = bookingDetailRepository
                .findMostUsedServiceByMonthAndYear(month, year);
        Map<Integer, Integer> serviceIdCount = new HashMap<>();
        for (BookingDetail bookingDetail : bookingDetails) {
            int serviceId = bookingDetail.getServices().getServiceId();
            serviceIdCount.put(serviceId, serviceIdCount.getOrDefault(serviceId, 0) + 1);
        }

        int maxCount = 0;
        for (int count : serviceIdCount.values()) {
            if (count > maxCount) {
                maxCount = count;
            }
        }

        List<Services> mostFrequentServices = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : serviceIdCount.entrySet()) {
            if (entry.getValue() == maxCount) {
                Services service = serviceRepository.findServicesByServiceId(entry.getKey());
                if (service != null) {
                    mostFrequentServices.add(service);
                }
            }
        }

        if (mostFrequentServices.isEmpty()) {
            throw new RuntimeException("No services found");
        }

        return new MostUsedServiceResponse(maxCount, mostFrequentServices);
    }
}
