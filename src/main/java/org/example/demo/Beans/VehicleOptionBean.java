package org.example.demo.Beans;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.demo.Entity.VehicleOption;
import org.example.demo.Services.DealerService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Named
@SessionScoped
public class VehicleOptionBean implements Serializable {
    @Inject
    private DealerService dealerService;

    private VehicleOption vehicleOption = new VehicleOption();
    private List<Long> selectedOptionIds = new ArrayList<>();
    private Long vehicleId;

    public List<VehicleOption> getVehicleOptions() {
        return dealerService.getAllVehicleOptions();
    }

    public String saveVehicleOption() {
        try {
            dealerService.addVehicleOption(vehicleOption);
            vehicleOption = new VehicleOption();
            return "vehicleOptions?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            return "vehicleOptions?faces-redirect=true";
        }
    }

    public String assignOptionsToVehicle(Long vehicleId) {
        this.vehicleId = vehicleId;
        selectedOptionIds.clear();
        return "assign-options?faces-redirect=true";
    }

    public String saveVehicleOptions() {
        try {
            if (vehicleId != null && !selectedOptionIds.isEmpty()) {
                dealerService.assignOptionsToVehicle(vehicleId, selectedOptionIds);
            }
            return "vehicles?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            return "vehicles?faces-redirect=true";
        }
    }
}
