package org.example.demo.REST;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.example.demo.Entity.Vehicle;
import org.example.demo.Services.DealerService;

import java.util.List;

@Path("/vehicles")
@Produces("application/json")
public class VehicleResource {
    @Inject
    private DealerService dealerService;

    @GET
    public Response getAllVehicles() {
        List<Vehicle> vehicles = dealerService.getAllVehicles();
        return Response.ok(vehicles).build();
    }

    @GET
    @Path("/{id}")
    public Response getVehicleById(@PathParam("id") Long id) {
        Vehicle vehicle = dealerService.getVehicleById(id);
        if (vehicle == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(vehicle).build();
    }

    @POST
    public Response createVehicle(Vehicle vehicle) {
        try {
            dealerService.addVehicle(vehicle);
            return Response.status(Response.Status.CREATED).entity(vehicle).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateVehicle(@PathParam("id") Long id, Vehicle vehicle) {
        try {
            Vehicle existingVehicle = dealerService.getVehicleById(id);
            if (existingVehicle == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            existingVehicle.setManufacturer(vehicle.getManufacturer());
            existingVehicle.setModel(vehicle.getModel());
            existingVehicle.setYear(vehicle.getYear());
            existingVehicle.setPrice(vehicle.getPrice());

            dealerService.addVehicle(existingVehicle);
            return Response.ok(existingVehicle).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error updating vehicle: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteVehicle(@PathParam("id") Long id) {
        try {
            Vehicle vehicle = dealerService.getVehicleById(id);
            if (vehicle == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            dealerService.deleteVehicle(id);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error deleting vehicle: " + e.getMessage())
                    .build();
        }
    }
}
