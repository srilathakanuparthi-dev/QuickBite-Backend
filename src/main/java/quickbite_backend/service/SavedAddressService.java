
package quickbite_backend.service;

import quickbite_backend.model.SavedAddress;
import quickbite_backend.model.User;
import quickbite_backend.repository.SavedAddressRepository;
import quickbite_backend.repository.UserRepository;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SavedAddressService {

    private final SavedAddressRepository addressRepository;
    private final UserRepository userRepository;

    public SavedAddressService(
            SavedAddressRepository addressRepository,
            UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    public List<SavedAddress> getAddresses(Integer userId) {
        return addressRepository.findByUser_UserId(userId);
    }

    public SavedAddress addAddress(Integer userId, String address) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        SavedAddress savedAddress = new SavedAddress();
        savedAddress.setUser(user);
        savedAddress.setAddress(address);

        return addressRepository.save(savedAddress);
    }

    public SavedAddress updateAddress(
            Integer userId, Integer addressId, String address) {

        SavedAddress savedAddress = addressRepository
                .findByAddressIdAndUser_UserId(addressId, userId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        savedAddress.setAddress(address);

        return addressRepository.save(savedAddress);
    }

    public void deleteAddress(Integer userId, Integer addressId) {

        SavedAddress savedAddress = addressRepository
                .findByAddressIdAndUser_UserId(addressId, userId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        addressRepository.delete(savedAddress);
    }
}
