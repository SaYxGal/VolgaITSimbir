package com.volgait.simbirGo.Rent.service;

import com.volgait.simbirGo.Account.model.Account;
import com.volgait.simbirGo.Account.service.AccountNotFoundException;
import com.volgait.simbirGo.Account.service.AccountService;
import com.volgait.simbirGo.Rent.model.PriceType;
import com.volgait.simbirGo.Rent.model.Rent;
import com.volgait.simbirGo.Rent.repository.RentRepository;
import com.volgait.simbirGo.Transport.model.Transport;
import com.volgait.simbirGo.Transport.service.TransportNotFoundException;
import com.volgait.simbirGo.Transport.service.TransportService;
import com.volgait.simbirGo.Util.ValidatorUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RentService {
    private final RentRepository rentRepository;
    private final AccountService accountService;
    private final TransportService transportService;
    private final ValidatorUtil validatorUtil;

    public RentService(RentRepository rentRepository, AccountService accountService, TransportService transportService, ValidatorUtil validatorUtil) {
        this.rentRepository = rentRepository;
        this.accountService = accountService;
        this.transportService = transportService;
        this.validatorUtil = validatorUtil;
    }

    private boolean checkValidType(String type) {
        boolean flag = false;
        for (int i = 0; i < PriceType.values().length; ++i) {
            if (Objects.equals(type, PriceType.values()[i].toString())) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    @Transactional(readOnly = true)
    public Rent findRent(Long id) {
        Account currentAccount = accountService.findCurrentAccount();
        final Optional<Rent> rent = rentRepository.findById(id);
        if (rent.isPresent() && currentAccount != null) {
            if (Objects.equals(currentAccount.getId(), rent.get().getAccount().getId()) ||
                    Objects.equals(currentAccount.getId(), rent.get().getTransport().getAccount().getId())
                    || currentAccount.isAdmin()) {
                return rent.orElseThrow(() -> new RentNotFoundException(id));
            }
        }
        return null;
    }

    @Transactional(readOnly = true)
    public List<Rent> findCurrentAccountRents() {
        Account currentAccount = accountService.findCurrentAccount();
        if (currentAccount != null) {
            return findAccountRents(currentAccount.getId());
        }
        return null;
    }

    @Transactional(readOnly = true)
    public List<Rent> findAccountRents(Long userId) {
        Account account = accountService.findAccount(userId);
        if (account != null) {
            return rentRepository.getAccountRents(account);
        }
        return null;
    }

    @Transactional(readOnly = true)
    public List<Rent> findTransportRents(Long transportId) {
        Transport transport = transportService.findTransport(transportId);
        Account currentAccount = accountService.findCurrentAccount();
        if (transport != null && currentAccount != null) {
            if (Objects.equals(currentAccount.getId(), transport.getAccount().getId()) || currentAccount.isAdmin()) {
                return rentRepository.getTransportRents(transport);
            }
            return null;
        }
        return null;
    }

    public Rent createRent(Long transportId, String priceType) {
        Account currentAccount = accountService.findCurrentAccount();
        Transport transport;
        try {
            transport = transportService.findTransport(transportId);
        } catch (TransportNotFoundException e) {
            return null;
        }

        if (!checkValidType(priceType) || currentAccount == null) {
            return null;
        }
        return createRent(transportId, currentAccount.getId(), ZonedDateTime.now(), null,
                priceType.equals(PriceType.Days.toString()) ? transport.getDayPrice() : transport.getMinutePrice(),
                priceType, 0);
    }

    @Transactional
    public Rent createRent(Long transportId, Long userId, ZonedDateTime timeStart, ZonedDateTime timeEnd,
                           double priceOfUnit, String priceType, double finalPrice) {
        Transport transport;
        try {
            transport = transportService.findTransport(transportId);
        } catch (TransportNotFoundException e) {
            return null;
        }

        if (Objects.equals(transport.getAccount().getId(), userId) || !checkValidType(priceType)) {
            return null;
        }
        final Rent rent = new Rent(timeStart, timeEnd, priceOfUnit, priceType, finalPrice);
        if (!transport.isCanBeRented()) {
            return null;
        }
        transport = transportService.updateTransportStatus(transport.getId(), false);
        rent.setTransport(transport);
        try {
            Account account = accountService.findAccount(userId);
            rent.setAccount(account);
        } catch (AccountNotFoundException e) {
            return null;
        }
        return rentRepository.save(rent);
    }

    @Transactional
    public Rent updateRent(Long id, Long transportId, Long userId, ZonedDateTime timeStart,
                           ZonedDateTime timeEnd, double priceOfUnit, String priceType, double finalPrice) {
        if (!checkValidType(priceType)) {
            return null;
        }
        final Rent rent = findRent(id);
        try {
            Account account = accountService.findAccount(userId);
            Transport transport = transportService.findTransport(transportId);
            if (!transport.isCanBeRented()) {
                return null;
            }
            transportService.updateTransportStatus(rent.getTransport().getId(), true);
            rent.setAccount(account);
            rent.setTransport(transport);
            transportService.updateTransportStatus(transport.getId(), false);
        } catch (AccountNotFoundException | TransportNotFoundException e) {
            return null;
        }
        rent.setTimeStart(timeStart);
        rent.setTimeEnd(timeEnd);
        rent.setPriceOfUnit(priceOfUnit);
        rent.setPriceType(priceType);
        rent.setFinalPrice(finalPrice);
        validatorUtil.validate(rent);
        return rentRepository.save(rent);
    }

    @Transactional
    public Rent deleteRent(Long id) {
        final Rent rent = findRent(id);
        rentRepository.delete(rent);
        transportService.updateTransportStatus(rent.getTransport().getId(), true);
        return rent;
    }

    @Transactional
    public Rent endRent(Long rentId) {
        try {
            Rent rent = findRent(rentId);
            Account currentAccount = accountService.findCurrentAccount();
            if ((!rent.getAccount().equals(currentAccount) && !currentAccount.isAdmin()) ||
                    rent.getTimeEnd() != null) {
                return null;
            }
            ZonedDateTime timeEnd = ZonedDateTime.now();
            double finalPrice;
            if (Objects.equals(rent.getPriceType(), "Days")) {
                long days = ChronoUnit.DAYS.between(rent.getTimeStart(), timeEnd);
                finalPrice = rent.getPriceOfUnit() * days;
                rent.setFinalPrice(finalPrice);
            } else {
                long minutes = ChronoUnit.MINUTES.between(rent.getTimeStart(), timeEnd);
                finalPrice = rent.getPriceOfUnit() * minutes;
                rent.setFinalPrice(finalPrice);
            }
            accountService.updateAccount(currentAccount.getId(), currentAccount.getUsername(),
                    currentAccount.getPassword(), currentAccount.isAdmin(),
                    currentAccount.getBalance() - finalPrice);
            rent.setTimeEnd(timeEnd);
            transportService.updateTransportStatus(rent.getTransport().getId(), true);
            validatorUtil.validate(rent);
            return rentRepository.save(rent);
        } catch (RentNotFoundException e) {
            return null;
        }
    }

    @Transactional(readOnly = true)
    public List<Transport> findAvailableTransport(double latitude, double longitude, double radius, String type) {
        List<Transport> transports = transportService.findAllByType(type);
        if (transports == null) {
            return null;
        }
        return transports.stream().filter(transport -> {
            double range = Math.sqrt(Math.pow(Math.abs(transport.getLatitude() - latitude), 2) +
                    Math.pow(Math.abs(transport.getLongitude() - longitude), 2));
            return range <= radius && transport.isCanBeRented();
        }).toList();
    }
}
