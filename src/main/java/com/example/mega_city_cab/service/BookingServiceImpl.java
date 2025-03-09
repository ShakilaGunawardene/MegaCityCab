package com.example.mega_city_cab.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mega_city_cab.DTO.BookingRequestDTO;
import com.example.mega_city_cab.DTO.CancellationRequest;
import com.example.mega_city_cab.entity.Booking;
import com.example.mega_city_cab.entity.BookingStatus;
import com.example.mega_city_cab.entity.Driver;
import com.example.mega_city_cab.entity.Vehicle;
import com.example.mega_city_cab.exception.InvalidBookingException;
import com.example.mega_city_cab.exception.ResourceNotFoundException;
import com.example.mega_city_cab.exception.UnauthorizedException;
import com.example.mega_city_cab.repository.BookingRepository;
import com.example.mega_city_cab.repository.DriverRepository;
import com.example.mega_city_cab.repository.VehicleRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private DriverRepository driverRepository;


    private static final int CANCELLATION_WINDOW_HOURS = 24;
    private static final double CANCELLATION_FEE_PERCENTAGE = 0.1;

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();

    }

    @Override
    public Booking getBookingById(String bookingId) {
        return bookingRepository.findById(bookingId).orElse(null);
    }

    @Override
    @Transactional
    public Booking createBooking(BookingRequestDTO request) {

        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));
        if (!isVehicleAvailableForTime(vehicle, request.getPickupDate())) {
            throw new InvalidBookingException("Vehicle is not available for requested time");

        }

        Booking booking = new Booking();
        booking.setCustomerId(request.getCustomerId());
        booking.setVehicleId(request.getVehicleId());
        booking.setPickupLocation(request.getPickupLocation());
        booking.setDropLocation(request.getDropLocation());
        booking.setPickupDate(request.getPickupDate());
        booking.setBookingDate(LocalDateTime.now().format(DATE_FORMATTER));
        booking.setDriverRequired(request.isDriverRequired());
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setTotalAmount(calculateBookingAmount(vehicle, request));

        if (request.isDriverRequired()) {
            assignDriverToBooking(booking, vehicle);

        }

        vehicle.setAvailable(false);
        vehicleRepository.save(vehicle);

        log.info("Create new booking with ID :  {} for customer : {}",
                booking.getBookingId(), booking.getCustomerId());
        return bookingRepository.save(booking);

    }

    @Transactional
    public Booking cancelBooking(String customerId, CancellationRequest request) {

        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!booking.getCustomerId().equals(customerId)) {

            throw new UnauthorizedException("Not Authorized to cancel booking");

        }

        if (!booking.canBeCancelled()) {
            throw new InvalidBookingException("Booking can not be  cancelled in current situation");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancellationReason(request.getReason());
        booking.setCancellationTime(LocalDateTime.now().format(DATE_FORMATTER));

        releaseBookingResource(booking);
        handleCancellationRefund(booking);

        log.info("Cancelled booking with ID: {} for customer: {}",
                booking.getBookingId(), booking.getCustomerId()

        );

        return bookingRepository.save(booking);

    }

    @Transactional(readOnly = true)
    public List<Booking> getCustomerBookings(String customerId) {
        return bookingRepository.findByCustomerId(customerId);
    }

    @Transactional(readOnly = true)
    public Booking getBookingDetails(String customerId, String bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!booking.getCustomerId().equals(customerId)) {

            throw new UnauthorizedException("Not authorized to view this booking");
        }

        return booking;
    }

    @Override
    public void deleteBooking(String bookingId, String customerId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!booking.getCustomerId().equals(customerId)) {
            throw new UnauthorizedException("Not authorized to delete this booking ");
        }

        if (!booking.canBeDeleted()) {
            throw new InvalidBookingException("Booking can't delete in current state");
        }

        releaseBookingResource(booking);

        bookingRepository.delete(booking);
        log.info("Deleted booking with ID : {} for customer: {} ", bookingId, customerId);
    }

    private boolean isVehicleAvailableForTime(Vehicle vehicle, String requestedTime) {

        if (!vehicle.isAvailable()) {
            return false;
        }

        List<Booking> existingBookings = bookingRepository.findByVehicleIdAndStatus(

                vehicle.getVehicleId(),
                BookingStatus.CONFIRMED

        );

        return existingBookings.stream()
                .noneMatch(booking -> isTimeOverlapping(booking.getPickupDate(), requestedTime));

    }

    private boolean isTimeOverlapping(String existing, String requested) {
        LocalDateTime existingTime = LocalDateTime.parse(existing, DATE_FORMATTER);
        LocalDateTime requestedTime = LocalDateTime.parse(requested, DATE_FORMATTER);

        Duration buffer = Duration.ofHours(1);
        return Math.abs(Duration.between(existingTime, requestedTime).toHours()) < buffer.toHours();

    }

    private double calculateBookingAmount(Vehicle vehicle, BookingRequestDTO request) {
        double baseAmount = vehicle.getBaseRate();
        if (request.isDriverRequired()) {
            baseAmount += vehicle.getDriverRate();
        }

        return baseAmount;
    }

    private void assignDriverToBooking(Booking booking, Vehicle vehicle) {

        Driver driver;

        if (vehicle.getAssignedDriverId() != null) {
            driver = driverRepository.findById(vehicle.getAssignedDriverId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assigned driver  not found"));

            if (!driver.isAvailable()) {
                throw new InvalidBookingException("Vehicles's assigned driver is not available");
            }
        } else {
            driver = driverRepository.findFirstByAvailableAndHasOwnVehicleFalse(true)
                    .orElseThrow(() -> new ResourceNotFoundException("No available driver"));
        }

        booking.setDriverId(driver.getDriverId());
        driver.setAvailable(false);
        driverRepository.save(driver);
        log.info("Assign driver {} to booking {}", driver.getDriverId(), booking.getBookingId());

    }

    private void releaseBookingResource(Booking booking) {

        if (booking.getVehicleId() != null) {

            Vehicle vehicle = vehicleRepository.findById(booking.getVehicleId()).orElse(null);
            if (vehicle != null && !vehicle.isAvailable()) {
                vehicle.setAvailable(true);
                vehicleRepository.save(vehicle);
                log.info("Released vehicle {} from booking {} ", vehicle.getVehicleId(), booking.getBookingId());
            }
        }

        if (booking.getDriverId() != null) {
            Driver driver = driverRepository.findById(booking.getDriverId()).orElse(null);

            if (driver != null && !driver.isAvailable()) {

                driver.setAvailable(true);
                driverRepository.save(driver);
                log.info("Released driver {} from booking {}", booking.getBookingId(), driver.getDriverId());
            }
        }
    }

    private void handleCancellationRefund(Booking booking) {

        LocalDateTime pickupDateTime = LocalDateTime.parse(booking.getPickupDate(), DATE_FORMATTER);
        LocalDateTime cancellationDeadline = pickupDateTime.minusHours(CANCELLATION_WINDOW_HOURS);
        if (LocalDateTime.now().isBefore(cancellationDeadline)) {

            booking.setRefundAmount(booking.getTotalAmount());
        } else {
            double cancellationFee = booking.getTotalAmount() * CANCELLATION_FEE_PERCENTAGE;
            booking.setRefundAmount(booking.getTotalAmount() - cancellationFee);
        }
        log.info("Processing refund of {} for booking {}",
                booking.getRefundAmount(), booking.getBookingId());
    }

    @Scheduled(fixedRate = 10000)
    public void checkAndUpdateCarAvailability() {
        String currentTime = LocalDateTime.now().format(DATE_FORMATTER);
        List<Booking> activeBookings = bookingRepository.findByStatusAndPickupDateBefore(
                BookingStatus.CONFIRMED, currentTime);

        for (Booking booking : activeBookings) {
            updateBookingStatus(booking);
        }

        log.info("Completed periodic booking status check ");

    }

    private void updateBookingStatus(Booking booking) {

        LocalDateTime pickupTime = LocalDateTime.parse(booking.getPickupDate(), DATE_FORMATTER);
        LocalDateTime now = LocalDateTime.now();

        if (now.isAfter(pickupTime)) {

            booking.setStatus(BookingStatus.IN_PROGRESS);
            bookingRepository.save(booking);
            log.info("updated booking {} status to IN_PROGRESS", booking.getBookingId());

        }

    }

    
}
