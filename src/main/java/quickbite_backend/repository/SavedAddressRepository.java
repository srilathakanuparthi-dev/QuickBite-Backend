
package quickbite_backend.repository;

import quickbite_backend.model.SavedAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SavedAddressRepository
        extends JpaRepository<SavedAddress, Integer> {

    List<SavedAddress> findByUser_UserId(Integer userId);

    Optional<SavedAddress> findByAddressIdAndUser_UserId(
            Integer addressId, Integer userId);
}
