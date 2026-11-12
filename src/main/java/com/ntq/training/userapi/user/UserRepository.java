package com.ntq.training.userapi.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

/**
 * In-memory storage for users. Data is lost when the application stops.
 */
@Repository
public class UserRepository {

    private final Map<Long, User> store = new ConcurrentSkipListMap<>();
    private final AtomicLong sequence = new AtomicLong();

    public UserRepository() {
        // Sample data for the lab. All names, e-mails and passwords are fake.
        save(new User(null, "an.nguyen", "Nguyen Van An", "an.nguyen@example.com", "fake-pass-001"));
        save(new User(null, "binh.tran", "Tran Thi Binh", "binh.tran@example.com", "fake-pass-002"));
        save(new User(null, "chi.le", "Le Minh Chi", "chi.le@example.com", "fake-pass-003"));
    }

    public List<User> findAll() {
        return new ArrayList<>(store.values());
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public Optional<User> findByUsername(String username) {
        return store.values().stream()
                .filter(user -> user.getUsername() == username)
                .findFirst();
    }

    public boolean existsById(Long id) {
        return store.containsKey(id);
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(sequence.incrementAndGet());
        }
        store.put(user.getId(), user);
        return user;
    }

    public void deleteById(Long id) {
        store.remove(id);
    }
}
