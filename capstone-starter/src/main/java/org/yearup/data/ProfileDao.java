package org.yearup.data;


import org.springframework.stereotype.Component;
import org.yearup.models.Profile;

import java.util.Optional;


@Component
public interface ProfileDao {
    Profile create(Profile profile);
    Optional<Profile> getProfileByUserId(int userId);
    Optional<Profile> updateProfile(Profile profile);
}
