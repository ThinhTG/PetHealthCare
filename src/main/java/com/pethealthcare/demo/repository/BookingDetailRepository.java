package com.pethealthcare.demo.repository;

import com.google.api.client.util.DateTime;
import com.pethealthcare.demo.enums.BookingDetailStatus;
import com.pethealthcare.demo.model.Booking;
import com.pethealthcare.demo.model.BookingDetail;
import com.pethealthcare.demo.model.Pet;
import com.pethealthcare.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface BookingDetailRepository extends JpaRepository<BookingDetail, Integer> {

    @Query("SELECT b FROM BookingDetail b WHERE b.date = :date")
    List<BookingDetail> findBookingDetailsFromDate(@Param("date") Date date);

    @Query("SELECT b FROM BookingDetail b WHERE b.date = :date")
    List<BookingDetail> findBookingDetailsFromLocalDate(@Param("date") LocalDate date);

    List<BookingDetail> findBookingDetailByNeedCage(boolean needCage);

    BookingDetail findBookingDetailByBookingDetailId(int id);

    List<BookingDetail> getBookingDetailsByBookingAndDate(Booking booking, LocalDate date);

    List<BookingDetail> getBookingDetailByuser(User user);

    List<BookingDetail> getBookingDetailByStatus(String status);

    List<BookingDetail> getBookingDetailByPet(Pet pet);

    List<BookingDetail> findBookingDetailByBooking(Booking booking);

    @Query("SELECT bd FROM BookingDetail bd WHERE MONTH(bd.date) = :month AND YEAR(bd.date) = :year")
    List<BookingDetail> findMostUsedServiceByMonthAndYear(@Param("month") int month, @Param("year") int year);

    BookingDetail findBookingDetailByStatusAndServices_ServiceId(BookingDetailStatus status, int serviceId);

    BookingDetail findBookingDetailByPet_PetIdAndStatus(int id, BookingDetailStatus status);

    List<BookingDetail> getBookingDetailsByBooking(Booking booking);

    List<BookingDetail> findBookingDetailByVetCancelled(boolean vetCancelled);
}
