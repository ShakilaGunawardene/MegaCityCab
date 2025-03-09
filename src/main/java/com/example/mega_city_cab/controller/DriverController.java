package com.example.mega_city_cab.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.mega_city_cab.entity.Booking;
import com.example.mega_city_cab.entity.Driver;
import com.example.mega_city_cab.entity.Vehicle;
import com.example.mega_city_cab.service.CloudinaryService;
import com.example.mega_city_cab.service.DriverService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth/driver")
@CrossOrigin(origins = "*")
@Slf4j
public class DriverController {
    @Autowired
    private DriverService driverService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping("/getalldrivers")
    public List<Driver> getAllDrivers() {
        return driverService.getAllDrivers();
    }

    @GetMapping("/{driverId}")
    public Driver getDriverById(@PathVariable String driverId) {
        return driverService.getDriverById(driverId);
    }

    @PostMapping(value = "/createdriver",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createDriver(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("licenseNumber") String licenseNumber,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("password") String password,
            @RequestParam("hasOwnVehicle") boolean hasOwnVehicle,
            @RequestParam(value = "model", required = false) String model,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "passengers", required = false) Integer passengers,
            @RequestParam(value = "number", required = false) String number,
            @RequestParam(value = "baseRate", required = false) Double baseRate,
            @RequestParam(value = "driverRate", required = false) Double driverRate,
            @RequestParam(value = "vehicleImage", required = false) MultipartFile vehicleImage
            ) {

        try{
            Driver driver = new Driver();
            driver.setName(name);
            driver.setLicenseNumber(licenseNumber);
            driver.setEmail(email);
            driver.setPhoneNumber(phoneNumber);
            driver.setPassword(password);
            driver.setHasOwnVehicle(hasOwnVehicle);

           
            Vehicle vehicle = null;
            if(hasOwnVehicle){
                vehicle = new Vehicle();
                vehicle.setNumber(number);
                vehicle.setModel(model);
                vehicle.setCategory(category);

                if (passengers != null) {
                    vehicle.setPassengers(passengers);
                } else {
                    
                    vehicle.setPassengers(4); 
                }

                if (baseRate != null) {
                    vehicle.setBaseRate(baseRate);
                }

                if (driverRate != null) {
                    vehicle.setDriverRate(driverRate);
                }

                if(vehicleImage != null && !vehicleImage.isEmpty()){
                    String vehicleImgUrl = cloudinaryService.uploadImage(vehicleImage);
                    vehicle.setVehicleImageUrl(vehicleImgUrl);
                }
            }

            return driverService.createDriver(driver, vehicle);

        }catch (Exception e){
            log.error("Error creating driver: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating driver: " + e.getMessage());
        }
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

        List<Booking> bookings = driverService.getDriverBookings(driverId);
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

    @PutMapping("/updateDriver/{driverId}")
    public ResponseEntity<Driver> updateDriver(@PathVariable String driverId, @RequestBody Driver driver) {
        log.info("Updating driver with ID: {}", driverId);
        return ResponseEntity.ok(driverService.updateDriver(driverId, driver));
    }
}
