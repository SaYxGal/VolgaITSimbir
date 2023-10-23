package com.volgait.simbirGo.Transport.controllers;

import com.volgait.simbirGo.Account.model.Account;
import com.volgait.simbirGo.Account.service.AccountService;
import com.volgait.simbirGo.Configuration.OpenAPI30Configuration;
import com.volgait.simbirGo.Transport.DTO.TransportCreateDto;
import com.volgait.simbirGo.Transport.DTO.TransportDto;
import com.volgait.simbirGo.Transport.DTO.TransportUpdateDto;
import com.volgait.simbirGo.Transport.model.Transport;
import com.volgait.simbirGo.Transport.service.TransportNotFoundException;
import com.volgait.simbirGo.Transport.service.TransportService;
import com.volgait.simbirGo.Util.ValidationException;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping(OpenAPI30Configuration.API_PREFIX + "/Transport")
public class TransportController {
    private final TransportService transportService;
    private final AccountService accountService;

    public TransportController(TransportService transportService, AccountService accountService) {
        this.transportService = transportService;
        this.accountService = accountService;
    }

    @GetMapping("/{id}")
    public TransportDto getTransport(@PathVariable Long id) {
        try {
            return new TransportDto(transportService.findTransport(id));
        } catch (TransportNotFoundException e) {
            return new TransportDto();
        }
    }

    @PostMapping("")
    public String makeTransport(@RequestBody TransportCreateDto transportDto) {
        try {
            Transport transport = transportService.createTransport(transportDto.isCanBeRented(),
                    transportDto.getTransportType(), transportDto.getModel(), transportDto.getColor(),
                    transportDto.getIdentifier(), transportDto.getDescription(), transportDto.getLatitude(),
                    transportDto.getLongitude(), transportDto.getMinutePrice(), transportDto.getDayPrice());
            return "Transport with id= " + transport.getId() + " was created";
        } catch (ValidationException e) {
            return e.getMessage();
        }
    }

    @PutMapping("/{id}")
    public String changeTransport(@PathVariable Long id, @RequestBody TransportUpdateDto updateDto) {
        try {
            Transport transport = transportService.findTransport(id);
            Account currentAccount = accountService.findCurrentAccount();
            if (!Objects.equals(transport.getAccount().getId(), currentAccount.getId())) {
                return "This transport has different owner";
            }
            transportService.updateTransport(id, updateDto.isCanBeRented(),
                    updateDto.getModel(), updateDto.getColor(), updateDto.getIdentifier(), updateDto.getDescription(),
                    updateDto.getLatitude(), updateDto.getLongitude(), updateDto.getMinutePrice(), updateDto.getDayPrice());
            return "Transport with id= " + transport.getId() + " was updated.";
        } catch (ValidationException e) {
            return e.getMessage();
        }
    }

    @DeleteMapping("/{id}")
    public String deleteTransport(@PathVariable Long id) {
        try {
            Transport transport = transportService.findTransport(id);
            Account currentAccount = accountService.findCurrentAccount();
            if (!Objects.equals(transport.getAccount().getId(), currentAccount.getId())) {
                return "This transport has different owner";
            }
            transportService.deleteTransport(id);
            return "Transport with id= " + transport.getId() + " was deleted";
        } catch (ValidationException | TransportNotFoundException e) {
            return e.getMessage();
        }
    }
}
