package com.volgait.simbirGo.Rent.controllers;

import com.volgait.simbirGo.Configuration.OpenAPI30Configuration;
import com.volgait.simbirGo.Rent.DTO.RentDto;
import com.volgait.simbirGo.Rent.model.Rent;
import com.volgait.simbirGo.Rent.service.RentService;
import com.volgait.simbirGo.Transport.DTO.TransportDto;
import com.volgait.simbirGo.Transport.model.Transport;
import com.volgait.simbirGo.Util.ValidationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(OpenAPI30Configuration.API_PREFIX + "/Rent")
public class RentController {
    private final RentService rentService;

    public RentController(RentService rentService) {
        this.rentService = rentService;
    }

    @GetMapping("/Transport")
    public List<TransportDto> getAvailableTransport(double latitude, double longitude, double radius, String type) {
        List<Transport> transports = rentService.findAvailableTransport(latitude, longitude, radius, type);
        if (transports != null) {
            return transports.stream().map(TransportDto::new).toList();
        }
        return null;
    }

    @GetMapping("/{rentId}")
    public RentDto getRent(@PathVariable Long rentId) {
        try {
            return new RentDto(rentService.findRent(rentId));
        } catch (Exception e) {
            return new RentDto();
        }
    }

    @GetMapping("/MyHistory")
    public List<RentDto> getCurrentAccountHistory() {
        return rentService.findCurrentAccountRents().stream().map(RentDto::new).toList();
    }

    @GetMapping("/TransportHistory/{transportId}")
    public List<RentDto> getTransportRents(@PathVariable Long transportId) {
        try {
            return rentService.findTransportRents(transportId).stream().map(RentDto::new).toList();
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping("/New/{transportId}")
    public String rentTransport(@PathVariable Long transportId, String priceType) {
        try {
            Rent rent = rentService.createRent(transportId, priceType);
            if (rent == null) {
                return "Incorrect data or transport was already rented";
            }
            return "Rent with id= " + rent.getId() + " was created";
        } catch (ValidationException e) {
            return e.getMessage();
        }
    }

    @PostMapping("/End/{rentId}")
    public String endRent(@PathVariable Long rentId) {
        Rent rent = rentService.endRent(rentId);
        if (rent == null) {
            return "Incorrect data, not allowed or already ended";
        }
        return "Rent with id= " + rent.getId() + " was ended";
    }
}
