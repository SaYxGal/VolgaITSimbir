package com.volgait.simbirGo.Transport.service;

import com.volgait.simbirGo.Account.model.Account;
import com.volgait.simbirGo.Account.service.AccountNotFoundException;
import com.volgait.simbirGo.Account.service.AccountService;
import com.volgait.simbirGo.Transport.model.Transport;
import com.volgait.simbirGo.Transport.model.TransportType;
import com.volgait.simbirGo.Transport.repository.TransportRepository;
import com.volgait.simbirGo.Util.ValidatorUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TransportService {
    private final TransportRepository transportRepository;
    private final AccountService accountService;
    private final ValidatorUtil validatorUtil;

    public TransportService(TransportRepository transportRepository, AccountService accountService, ValidatorUtil validatorUtil) {
        this.transportRepository = transportRepository;
        this.accountService = accountService;
        this.validatorUtil = validatorUtil;
    }

    private boolean checkValidType(String type) {
        boolean flag = false;
        for (int i = 0; i < TransportType.values().length; ++i) {
            if (Objects.equals(type, TransportType.values()[i].toString())) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    @Transactional(readOnly = true)
    public Transport findTransport(Long id) {
        final Optional<Transport> transport = transportRepository.findById(id);
        return transport.orElseThrow(() -> new TransportNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<Transport> findAllByType(String transportType) {
        if (!checkValidType(transportType) && !Objects.equals(transportType, "All")) {
            return null;
        }
        if (Objects.equals(transportType, "All")) {
            return transportRepository.findAll();
        } else {
            return transportRepository.getTransportsFiltered(transportType);
        }
    }

    @Transactional(readOnly = true)
    public List<Transport> findTransportsInRange(int start, int count, String transportType) {
        if (!checkValidType(transportType) && !Objects.equals(transportType, "All")) {
            throw new RuntimeException("Некорректный тип транспорта");
        }
        if (Objects.equals(transportType, "All")) {
            return transportRepository.getTransportsInRange(start - 1, count);
        } else {
            return transportRepository.getTransportsInRangeFiltered(start - 1, count, transportType);
        }

    }

    @Transactional
    public Transport createTransport(boolean canBeRented, String transportType,
                                     String model, String color,
                                     String identifier, String description,
                                     double latitude, double longitude,
                                     double minutePrice, double dayPrice) {
        if (!checkValidType(transportType)) {
            return null;
        }
        Account currentAccount = accountService.findCurrentAccount();
        if (currentAccount != null) {
            return createTransport(canBeRented, transportType,
                    model, color, identifier, description, latitude,
                    longitude, minutePrice, dayPrice, currentAccount.getId());
        }
        return null;
    }

    @Transactional
    public Transport createTransport(boolean canBeRented, String transportType,
                                     String model, String color,
                                     String identifier, String description,
                                     double latitude, double longitude,
                                     double minutePrice, double dayPrice, Long ownerId) {
        if (!checkValidType(transportType)) {
            return null;
        }
        final Transport transport = new Transport(canBeRented, transportType, model, color, identifier,
                description, latitude, longitude, minutePrice, dayPrice);
        try {
            Account account = accountService.findAccount(ownerId);
            transport.setAccount(account);
        } catch (AccountNotFoundException e) {
            return null;
        }
        validatorUtil.validate(transport);
        return transportRepository.save(transport);
    }

    @Transactional
    public Transport updateTransport(Long id, boolean canBeRented,
                                     String model, String color,
                                     String identifier, String description,
                                     double latitude, double longitude,
                                     double minutePrice, double dayPrice) {
        Account currentAccount = accountService.findCurrentAccount();
        Transport currentTransport = findTransport(id);
        if (currentAccount != null) {
            return updateTransport(id, canBeRented, currentTransport.getTransportType(),
                    model, color, identifier, description, latitude,
                    longitude, minutePrice, dayPrice, currentAccount.getId());
        }
        return null;
    }

    @Transactional
    public Transport updateTransport(Long id, boolean canBeRented, String transportType,
                                     String model, String color,
                                     String identifier, String description,
                                     double latitude, double longitude,
                                     double minutePrice, double dayPrice, Long ownerId) {
        if (!checkValidType(transportType)) {
            return null;
        }
        final Transport transport = findTransport(id);
        try {
            Account account = accountService.findAccount(ownerId);
            transport.setAccount(account);
        } catch (AccountNotFoundException e) {
            return null;
        }
        transport.setCanBeRented(canBeRented);
        transport.setTransportType(transportType);
        transport.setModel(model);
        transport.setColor(color);
        transport.setIdentifier(identifier);
        transport.setDescription(description);
        transport.setLatitude(latitude);
        transport.setLongitude(longitude);
        transport.setMinutePrice(minutePrice);
        transport.setDayPrice(dayPrice);
        validatorUtil.validate(transport);
        return transportRepository.save(transport);
    }

    @Transactional
    public Transport updateTransportStatus(Long id, boolean canBeRented) {
        final Transport transport = findTransport(id);
        transport.setCanBeRented(canBeRented);
        validatorUtil.validate(transport);
        return transportRepository.save(transport);
    }

    @Transactional
    public Transport updateTransportPosition(Long id, double latitude, double longitude) {
        final Transport transport = findTransport(id);
        transport.setLatitude(latitude);
        transport.setLongitude(longitude);
        validatorUtil.validate(transport);
        return transportRepository.save(transport);
    }

    @Transactional
    public Transport deleteTransport(Long id) {
        final Transport currentTransport = findTransport(id);
        transportRepository.delete(currentTransport);
        return currentTransport;
    }
}
