package com.pethealthcare.demo.service;

import com.pethealthcare.demo.dto.request.BookingDetailUpdateRequest;
import com.pethealthcare.demo.enums.BookingDetailStatus;
import com.pethealthcare.demo.enums.BookingStatus;
import com.pethealthcare.demo.model.*;
import com.pethealthcare.demo.repository.*;
import com.pethealthcare.demo.repository.UserRepository;
import com.pethealthcare.demo.response.MostUsedServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDate;
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
    @Autowired
    private ServiceSlotService serviceSlotService;
    @Autowired
    private UserService userService;

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

    public List<BookingDetail> getBookingDetailByVetCancel(int vetId) {
        User user = userRepository.findUserByUserId(vetId);
        List<BookingDetail> bookingDetails = bookingDetailRepository.getBookingDetailByuser(user);
        List<BookingDetail> bookingDetailByVetCancel = new ArrayList<>();

        for (BookingDetail bookingDetail : bookingDetails) {
            if (bookingDetail.isVetCancelled()) {
                bookingDetailByVetCancel.add(bookingDetail);
            }
        }

        return bookingDetailByVetCancel;
    }

    public List<BookingDetail> getBookingDetailStatusByVet(int vetId) {
        User user = userRepository.findUserByUserId(vetId);
        List<BookingDetail> bookingDetails = bookingDetailRepository.getBookingDetailByuser(user);
        List<BookingDetail> nonCancelledBookingDetails = new ArrayList<>();

        for (BookingDetail bookingDetail : bookingDetails) {
            if ( bookingDetail.getStatus() != BookingDetailStatus.CANCELLED && bookingDetail.getStatus() != BookingDetailStatus.WAITING){
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
                if (bookingDetail.getStatus() == BookingDetailStatus.COMPLETED) {
                    bookingDetails.add(bookingDetail);
                }
            }
        }
        return bookingDetails;
    }

    public List<BookingDetail> getBookingDetailByCusPhoneAndDate(String phone, LocalDate date) {
        User user = userService.getUserByNumberPhone(phone);
        List<Booking> bookings = bookingRepository.getBookingByUser(user);
        List<BookingDetail> bookingDetails = new ArrayList<>();
        List<BookingDetail> bookingDetailByBooking;
        for (Booking booking : bookings) {
            bookingDetailByBooking = bookingDetailRepository.findBookingDetailByBooking(booking);
            for (BookingDetail bookingDetail : bookingDetailByBooking) {
                if (bookingDetail.getStatus() == BookingDetailStatus.WAITING && bookingDetail.getDate().equals(date)){
                    bookingDetails.add(bookingDetail);
                }
            }
        }
        return bookingDetails;
    }

//    public List<BookingDetail> getBookingDetailBySerivceInactive(){
//        List<BookingDetail> bookingDetails = bookingDetailRepository.findAll();
//        List<BookingDetail> bookingDetailByServiceInactive = new ArrayList<>();
//        for (BookingDetail bookingDetail : bookingDetails) {
//            if (bookingDetail.getServices().getStatus()) {
//                bookingDetailByServiceInactive.add(bookingDetail);
//            }
//        }
//        return bookingDetailByServiceInactive;
//    }

    public void deleteBookingDetailByPet(int petId) {
        Pet pet = petRepository.findPetByPetId(petId);
        List<BookingDetail> bookingDetails = bookingDetailRepository.getBookingDetailByPet(pet);
        for (BookingDetail bookingDetail : bookingDetails) {
            bookingDetail.setStatus(BookingDetailStatus.CANCELLED);
            bookingDetailRepository.save(bookingDetail);

            List<BookingDetail> bookingDetailss = bookingDetailRepository.getBookingDetailsByBooking(bookingDetail.getBooking());

            boolean allCancelled = bookingDetailss.stream().allMatch(detail -> detail.getStatus() == BookingDetailStatus.CANCELLED);


            if (allCancelled) {
                Booking booking = bookingDetail.getBooking();
                booking.setStatus(BookingStatus.CANCELLED);
                bookingRepository.save(booking);
            }
        }
    }

    public void deleteBookingDetail(int bookingDetailId) {
        BookingDetail bookingDetail = bookingDetailRepository.findBookingDetailByBookingDetailId(bookingDetailId);
        bookingDetail.setStatus(BookingDetailStatus.CANCELLED);
        bookingDetailRepository.save(bookingDetail);

        List<BookingDetail> bookingDetails = bookingDetailRepository.getBookingDetailsByBooking(bookingDetail.getBooking());

        boolean allCancelled = bookingDetails.stream().allMatch(detail -> detail.getStatus() == BookingDetailStatus.CANCELLED);


        if (allCancelled) {
            Booking booking = bookingDetail.getBooking();
            booking.setStatus(BookingStatus.CANCELLED);
            bookingRepository.save(booking);
        }
    }


    public List<BookingDetail> getBookingDetailByPetIsDeleted(int petId){
        List<BookingDetail> bookingDetails = bookingDetailRepository.findAll();
        List<BookingDetail> bookingDetailByStatus = new ArrayList<>();
        for (BookingDetail bookingDetail : bookingDetails) {
            if (bookingDetail.getStatus() == BookingDetailStatus.CONFIRMED) {
                bookingDetailByStatus.add(bookingDetail);
            }
        }
        List<BookingDetail> bookingDetailByPet = new ArrayList<>();
        for (BookingDetail bookingDetail : bookingDetailByStatus) {
            if (bookingDetail.getPet().getPetId() == petId) {
                bookingDetailByPet.add(bookingDetail);
            }
        }
        return bookingDetailByPet;
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



    public BookingDetail updateStatusBookingDetail(int bookingDetailId, BookingDetailStatus status) {
        BookingDetail bookingDetail = bookingDetailRepository.findBookingDetailByBookingDetailId(bookingDetailId);
        bookingDetail.setStatus(status);
        return bookingDetailRepository.save(bookingDetail);
    }

    public List<BookingDetail> updateStatusBookingDetailVetCancel(LocalDate dateTime) {
        List<BookingDetail> bookingDetail = bookingDetailRepository.findBookingDetailsFromLocalDate(dateTime);
        for (BookingDetail detail : bookingDetail) {
            detail.setVetCancelled(true);
            bookingDetailRepository.save(detail);
        }
        return bookingDetail;
    }



    public List<BookingDetail> getBookingDetailByStayCage() {

        List<Pet> pets = petRepository.getPetByStayCage(true);

        List<BookingDetail> bookingDetails = new ArrayList<>();

        for (Pet pet : pets) {
            bookingDetails.addAll(bookingDetailRepository.getBookingDetailByPet(pet));
        }

        return bookingDetails;
    }

    public void updateStatusByBookingId(int bookingId, BookingDetailStatus status) {
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
