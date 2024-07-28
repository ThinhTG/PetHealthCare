package com.pethealthcare.demo.service;

import com.pethealthcare.demo.dto.request.BookingDetailCreateRequest;
import com.pethealthcare.demo.dto.request.BookingDetailUpdateRequest;
import com.pethealthcare.demo.enums.BookingDetailStatus;
import com.pethealthcare.demo.enums.BookingStatus;
import com.pethealthcare.demo.enums.ServiceSlotStatus;
import com.pethealthcare.demo.mapper.BookingDetailMapper;
import com.pethealthcare.demo.model.*;
import com.pethealthcare.demo.repository.*;
import com.pethealthcare.demo.repository.UserRepository;
import com.pethealthcare.demo.response.MostUsedServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
    private SlotRepository slotRepository;

    @Autowired
    private BookingDetailMapper bookingDetailMapper;

    @Autowired
    private ServiceSlotService serviceSlotService;

    public List<BookingDetail> getAllBookingDetail() {
        return bookingDetailRepository.findAll();
    }

    public List<BookingDetail> getAllBookingDetailNeedCage() {
        List<BookingDetail> bookingDetails = bookingDetailRepository.findAll();
        List<BookingDetail> bookingDetailsNeedCage = new ArrayList<>();
        for (BookingDetail bookingDetail : bookingDetails) {
            if (bookingDetail.isNeedCage()) {
                bookingDetailsNeedCage.add(bookingDetail);
            }

        }
        return bookingDetailsNeedCage;
    }

    public void createBookingDetail(BookingDetailCreateRequest request, Booking newBooking) {
        BookingDetail bookingDetail = bookingDetailMapper.toBookingDetail(request);
        bookingDetail.setDate(request.getDate());
        bookingDetail.setStatus(BookingDetailStatus.WAITING);
        bookingDetail.setVetCancelled(false);
        bookingDetail.setBooking(newBooking);


        User user = userRepository.findUserByUserId(request.getVeterinarianId());
        bookingDetail.setUser(user);

        Pet pet = petRepository.findPetByPetId(request.getPetId());
        bookingDetail.setPet(pet);

        Services services = serviceRepository.findByServiceId(request.getServiceId());
        bookingDetail.setServices(services);

        Slot slot = slotRepository.findSlotBySlotId(request.getSlotId());
        bookingDetail.setSlot(slot);

        serviceSlotService.updateServiceSlotStatus(request.getVeterinarianId(), request.getDate(), request.getSlotId(), ServiceSlotStatus.BOOKED);
        bookingDetailRepository.save(bookingDetail);
    }

    public BookingDetail updateNeedCage(int bookingDetailId) {
        Optional<BookingDetail> optionalBookingDetail = bookingDetailRepository.findById(bookingDetailId);
        if (optionalBookingDetail.isPresent()) {
            BookingDetail bookingDetail = optionalBookingDetail.get();
            // Save updated user
            bookingDetail.setNeedCage(!bookingDetail.isNeedCage());
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

    public BookingDetail updateVetCancel(int bookingDetailId) {
        BookingDetail bookingDetail = bookingDetailRepository.findBookingDetailByBookingDetailId(bookingDetailId);
        bookingDetail.setVetCancelled(true);
        return bookingDetailRepository.save(bookingDetail);
    }

    public List<BookingDetail> getBookingDetailByVetCancel() {
        return bookingDetailRepository.findBookingDetailByVetCancelled(true);
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

    public List<BookingDetail> getBookingDetailByPhone(String phone, LocalDate date) {
        List<Booking> bookings = bookingRepository.findBookingByUser_Phone(phone);
        List<BookingDetail> bookingDetails = new ArrayList<>();
        for (Booking booking : bookings) {
            bookingDetails.addAll(bookingDetailRepository.getBookingDetailsByBookingAndDate(booking, date));
        }
        if (bookingDetails.isEmpty()) {
            return null;
        }
        return bookingDetails;
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
