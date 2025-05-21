package org.example.demo.Services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Specializes;
import jakarta.transaction.Transactional;

@Specializes
@ApplicationScoped
public class SpecializedDealerService extends DealerService{

    @Override
    @Transactional
    public void purchaseVehicle(Long vehicleId, Long clientId) {
        System.out.println("\n" +
                " __   _____  _   _   _  _   ___   _____   ___ _   _ ___  ___ _  _   _   ___ ___ ___      _    __   _____ _  _ ___ ___ _    ___ \n" +
                " \\ \\ / / _ \\| | | | | || | /_\\ \\ / / __| | _ \\ | | | _ \\/ __| || | /_\\ / __| __|   \\    /_\\   \\ \\ / / __| || |_ _/ __| |  | __|\n" +
                "  \\ V / (_) | |_| | | __ |/ _ \\ V /| _|  |  _/ |_| |   / (__| __ |/ _ \\\\__ \\ _|| |) |  / _ \\   \\ V /| _|| __ || | (__| |__| _| \n" +
                "   |_| \\___/ \\___/  |_||_/_/ \\_\\_/ |___| |_|  \\___/|_|_\\\\___|_||_/_/ \\_\\___/___|___/  /_/ \\_\\   \\_/ |___|_||_|___\\___|____|___|\n" +
                "                                                                                                                               \n");
        super.purchaseVehicle(vehicleId, clientId);
    }
}
