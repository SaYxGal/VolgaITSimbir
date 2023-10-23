package com.volgait.simbirGo.Transport.controllers;

import com.volgait.simbirGo.Account.model.AccountRole;
import com.volgait.simbirGo.Configuration.OpenAPI30Configuration;
import com.volgait.simbirGo.Transport.DTO.TransportAdminCreateUpdateDto;
import com.volgait.simbirGo.Transport.DTO.TransportDto;
import com.volgait.simbirGo.Transport.model.Transport;
import com.volgait.simbirGo.Transport.service.TransportNotFoundException;
import com.volgait.simbirGo.Transport.service.TransportService;
import com.volgait.simbirGo.Util.ValidationException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(OpenAPI30Configuration.API_PREFIX + "/Admin/Transport")
public class AdminTransportController {
    private final TransportService transportService;


    public AdminTransportController(TransportService transportService) {
        this.transportService = transportService;
    }

    @GetMapping("")
    @Secured(AccountRole.AsString.ADMIN)
    public List<TransportDto> getTransports(int start, int count, String transportType) {
        try {
            List<Transport> transports = transportService.findTransportsInRange(start, count, transportType);
            return transports.stream()
                    .map(TransportDto::new).toList();
        } catch (RuntimeException e) {
            return null;
        }
    }

    @GetMapping("/{id}")
    @Secured(AccountRole.AsString.ADMIN)
    public TransportDto getTransport(@PathVariable Long id) {
        try {
            return new TransportDto(transportService.findTransport(id));
        } catch (TransportNotFoundException e) {
            return new TransportDto();
        }
    }

    @PostMapping("")
    @Secured(AccountRole.AsString.ADMIN)
    public String makeTransport(@RequestBody TransportAdminCreateUpdateDto transportDto) {
        try {
            Transport transport = transportService.createTransport(transportDto.isCanBeRented(),
                    transportDto.getTransportType(), transportDto.getModel(), transportDto.getColor(),
                    transportDto.getIdentifier(), transportDto.getDescription(), transportDto.getLatitude(),
                    transportDto.getLongitude(), transportDto.getMinutePrice(), transportDto.getDayPrice(), transportDto.getOwnerId());
            if (transport == null) {
                return "Incorrect data";
            }
            return "Transport with id= " + transport.getId() + " was created";
        } catch (ValidationException e) {
            return e.getMessage();
        }
    }

    @PutMapping("/{id}")
    @Secured(AccountRole.AsString.ADMIN)
    public String changeTransport(@PathVariable Long id, @RequestBody TransportAdminCreateUpdateDto updateDto) {
        try {
            Transport transport = transportService.updateTransport(id, updateDto.isCanBeRented(), updateDto.getTransportType(),
                    updateDto.getModel(), updateDto.getColor(), updateDto.getIdentifier(), updateDto.getDescription(),
                    updateDto.getLatitude(), updateDto.getLongitude(), updateDto.getMinutePrice(),
                    updateDto.getDayPrice(), updateDto.getOwnerId());
            if (transport == null) {
                return "Incorrect data";
            }
            return "Transport with id= " + transport.getId() + " was updated.";
        } catch (ValidationException e) {
            return e.getMessage();
        }
    }

    @DeleteMapping("/{id}")
    @Secured(AccountRole.AsString.ADMIN)
    public String deleteTransport(@PathVariable Long id) {
        try {
            Transport transport = transportService.deleteTransport(id);
            return "Transport with id= " + transport.getId() + " was deleted";
        } catch (ValidationException | TransportNotFoundException e) {
            return e.getMessage();
        }
    }
}
