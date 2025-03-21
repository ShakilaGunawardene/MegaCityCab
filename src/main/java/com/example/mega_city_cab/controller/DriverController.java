package com.example.mega_city_cab.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.mega_city_cab.entity.Booking;
import com.example.mega_city_cab.entity.Customer;
import com.example.mega_city_cab.entity.Driver;
import com.example.mega_city_cab.entity.Vehicle;
import com.example.mega_city_cab.exception.ResourceNotFoundException;
import com.example.mega_city_cab.repository.CustomerRepository;
import com.example.mega_city_cab.service.BookingService;
import com.example.mega_city_cab.service.CloudinaryService;
import com.example.mega_city_cab.service.DriverService;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/auth/driver")
@Slf4j
public class DriverController {
    @Autowired
    private DriverService driverService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BookingService bookingService;

    @GetMapping("/getalldrivers")
    public List<Driver> getAllDrivers() {
        return driverService.getAllDrivers();
    }

    @GetMapping("/getdriver/{driverId}")
    public ResponseEntity<Driver> getDriverById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String driverId) {
        String email = userDetails.getUsername();

        // Check if the user has a booking with this driver
        boolean hasBooking = bookingService.hasBookingWithDriver(email, driverId);
        if (!hasBooking) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Driver driver = driverService.getDriverById(driverId);
        return ResponseEntity.ok(driver);
    }

    @PostMapping(value = "/createdriver",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createDriver(
            @RequestParam("driverName") String driverName,
            @RequestParam("email") String email,
            @RequestParam("driverLicense") String driverLicense,
            @RequestParam("phone") String phone,
            @RequestParam("password") String password,
            @RequestParam("hasOwnCar") boolean hasOwnCar,
            @RequestParam(value = "licensePlate", required = false) String licensePlate,
            @RequestParam(value = "model", required = false) String model,
            @RequestParam(value = "numberOfSeats", required = false) Integer numberOfSeats,
            @RequestParam(value = "baseRate", required = false) Double baseRate,
            @RequestParam(value = "driverRate", required = false) Double driverRate,
            @RequestParam(value = "carImage", required = false) MultipartFile carImage,
            @RequestParam(value = "profileImage") MultipartFile profileImage) {

        try {
            Driver driver = new Driver();
            driver.setName(driverName);
            driver.setEmail(email);
            driver.setLicenseNumber(driverLicense);
            driver.setPhoneNumber(phone);
            driver.setPassword(password);
            driver.setHasOwnVehicle(hasOwnCar);

            // if (profileImage != null && !profileImage.isEmpty()) {
            //     String profileImageUrl = cloudinaryService.uploadImage(profileImage);
            //     driver.setProfileImage(profileImageUrl);
            // }

            Vehicle vehicle = null;
            if (hasOwnCar) {
                vehicle = new Vehicle();
                vehicle.setNumber(licensePlate);
                vehicle.setModel(model);
                vehicle.setPassengers(numberOfSeats != null ? numberOfSeats : 4);
                vehicle.setBaseRate(baseRate != null ? baseRate : 0.0);
                vehicle.setDriverRate(driverRate != null ? driverRate : 0.0);
                if (carImage != null && !carImage.isEmpty()) {
                    String carImageUrl = cloudinaryService.uploadImage(carImage);
                    vehicle.setVehicleImageUrl(carImageUrl);
                }
            }

            return driverService.createDriver(driver, vehicle);

        } catch (Exception e) {
            log.error("Error creating driver: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating driver: " + e.getMessage());
        }
    }

    @PutMapping("/updatedriver/{driverId}")
    public ResponseEntity<Driver> updateDriver(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String driverId,
            @RequestBody Driver driver) {
        String email = userDetails.getUsername();
        log.info("Updating driver with ID: {} for email: {}", driverId, email);

        Driver updatedDriver = driverService.updateDriver(driverId, driver);
        return ResponseEntity.ok(updatedDriver);
    }

    @PutMapping("/{driverId}/availability")
    public ResponseEntity<Driver> updateAvailability(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String driverId,
            @RequestBody Map<String, Boolean> availability) {
        String email = userDetails.getUsername();
        log.info("Updating availability for driver: {} for email: {}", driverId, email);

        if (!availability.containsKey("availability")) {
            return ResponseEntity.badRequest().build();
        }

        Driver driver = driverService.updateAvailability(driverId, availability.get("availability"));
        return ResponseEntity.ok(driver);
    }

    @GetMapping("/{driverId}/bookings")
    public ResponseEntity<List<Booking>> getDriverBookings(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String driverId) {
        String email = userDetails.getUsername();
        log.info("Fetching bookings for driver: {} for email: {}", driverId, email);

        Driver driver = driverService.getDriverById(driverId);
        if (!email.equals(driver.getEmail())) {
            log.warn("Unauthorized access attempt by user: {}", email);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<Booking> bookings = driverService.getDriverBookings(driverId).stream()
                .map(booking -> {
                    Customer customer = customerRepository.findById(booking.getCustomerId())
                            .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
                    booking.setPassengerName(customer.getName());
                    return booking;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(bookings);
    }

    @DeleteMapping("/{driverId}")
    public ResponseEntity<Void> deleteDriver(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String driverId) {
        String email = userDetails.getUsername();
        log.info("Deleting driver with ID: {} for email: {}", driverId, email);

        driverService.deleteDriver(driverId);
        return ResponseEntity.noContent().build();
    }
}
