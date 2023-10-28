package com.volgait.simbirGo.Rent.controllers;

import com.volgait.simbirGo.Account.model.AccountRole;
import com.volgait.simbirGo.Configuration.OpenAPI30Configuration;
import com.volgait.simbirGo.Rent.DTO.RentAdminCreateUpdateDto;
import com.volgait.simbirGo.Rent.DTO.RentDto;
import com.volgait.simbirGo.Rent.model.Rent;
import com.volgait.simbirGo.Rent.service.RentNotFoundException;
import com.volgait.simbirGo.Rent.service.RentService;
import com.volgait.simbirGo.Util.ValidationException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(OpenAPI30Configuration.API_PREFIX + "/Admin/Rent")
public class AdminRentController {
    private final RentService rentService;

    public AdminRentController(RentService rentService) {
        this.rentService = rentService;
    }

    @GetMapping("/{rentId}")
    @Secured(AccountRole.AsString.ADMIN)
    public RentDto getRent(@PathVariable Long rentId) {
        try {
            return new RentDto(rentService.findRent(rentId));
        } catch (Exception e) {
            return new RentDto();
        }
    }

    @GetMapping("/UserHistory/{userId}")
    @Secured(AccountRole.AsString.ADMIN)
    public List<RentDto> getAccountRents(@PathVariable Long userId) {
        return rentService.findAccountRents(userId).stream().map(RentDto::new).toList();
    }

    @GetMapping("/TransportHistory/{transportId}")
    @Secured(AccountRole.AsString.ADMIN)
    public List<RentDto> getTransportRents(@PathVariable Long transportId) {
        return rentService.findTransportRents(transportId).stream().map(RentDto::new).toList();
    }

    @PostMapping("")
    @Secured(AccountRole.AsString.ADMIN)
    public String makeRent(@RequestBody RentAdminCreateUpdateDto createDto) {
        try {
            Rent rent = rentService.createRent(createDto.getTransportId(),
                    createDto.getUserId(), createDto.getTimeStart(), createDto.getTimeEnd(),
                    createDto.getPriceOfUnit(), createDto.getPriceType(), createDto.getFinalPrice());
            if (rent == null) {
                return "Incorrect data or transport was already rented";
            }
            return "Rent with id= " + rent.getId() + " was created";
        } catch (ValidationException e) {
            return e.getMessage();
        }
    }

    @PostMapping("/End/{rentId}")
    @Secured(AccountRole.AsString.ADMIN)
    public String endRent(@PathVariable Long rentId, double latitude, double longitude) {
        Rent rent = rentService.endRent(rentId, latitude, longitude);
        if (rent == null) {
            return "Incorrect data";
        }
        return "Rent with id= " + rent.getId() + " was ended";
    }

    @PutMapping("/{id}")
    @Secured(AccountRole.AsString.ADMIN)
    public String changeRent(@PathVariable Long id, @RequestBody RentAdminCreateUpdateDto updateDto) {
        try {
            Rent rent = rentService.updateRent(id, updateDto.getTransportId(), updateDto.getUserId(),
                    updateDto.getTimeStart(), updateDto.getTimeEnd(), updateDto.getPriceOfUnit(),
                    updateDto.getPriceType(), updateDto.getFinalPrice());
            if (rent == null) {
                return "Incorrect data";
            }
            return "Rent with id= " + rent.getId() + " was updated.";
        } catch (ValidationException e) {
            return e.getMessage();
        }
    }

    @DeleteMapping("/{id}")
    @Secured(AccountRole.AsString.ADMIN)
    public String deleteRent(@PathVariable Long id) {
        try {
            Rent rent = rentService.deleteRent(id);
            return "Rent with id= " + rent.getId() + " was deleted";
        } catch (ValidationException | RentNotFoundException e) {
            return e.getMessage();
        }
    }
}
