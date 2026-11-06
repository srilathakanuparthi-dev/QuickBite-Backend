
package quickbite_backend.controller;

import quickbite_backend.model.SavedAddress;
import quickbite_backend.service.SavedAddressService;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/addresses")
@CrossOrigin(origins = "http://localhost:5174")
public class SavedAddressController {

    private final SavedAddressService addressService;

    public SavedAddressController(SavedAddressService addressService) {
        this.addressService = addressService;
    }

    // Get all saved addresses
    @GetMapping("/{userId}")
    public List<SavedAddress> getAddresses(@PathVariable Integer userId) {
        return addressService.getAddresses(userId);
    }

    // Add new address
    @PostMapping("/{userId}")
    public SavedAddress addAddress(
            @PathVariable Integer userId,
            @RequestBody Map<String, String> request) {

        return addressService.addAddress(
                userId, request.get("address"));
    }

    // Update address
    @PutMapping("/{userId}/{addressId}")
    public SavedAddress updateAddress(
            @PathVariable Integer userId,
            @PathVariable Integer addressId,
            @RequestBody Map<String, String> request) {

        return addressService.updateAddress(
                userId, addressId, request.get("address"));
    }

    // Delete address
    @DeleteMapping("/{userId}/{addressId}")
    public String deleteAddress(
            @PathVariable Integer userId,
            @PathVariable Integer addressId) {

        addressService.deleteAddress(userId, addressId);
        return "Address deleted successfully";
    }
}
