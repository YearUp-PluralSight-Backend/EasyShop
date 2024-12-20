package org.yearup.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@RequestMapping("/profile")
@CrossOrigin
public class ProfilesController {
    private static final Logger log = LoggerFactory.getLogger(ProfilesController.class);
    private final ProfileDao profileDao;
    private final UserDao userDao;

    @Autowired
    public ProfilesController(ProfileDao profileDao, UserDao userDao) {
        this.profileDao = profileDao;
        this.userDao = userDao;
    }


    @GetMapping("")
    public ResponseEntity<Profile> getProfile(Principal principal) {

        // get the currently logged in username
        String userName = principal.getName();
        // find database user by userId
        User user = userDao.getByUserName(userName);
        int userId = user.getId();
        return ResponseEntity.ok(profileDao.getProfileByUserId(userId).orElse(null));
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Profile> getById(@PathVariable int id) {
        Profile profile = profileDao.getProfileByUserId(id).orElse(null);

        if (profile == null) {
            return ResponseEntity.notFound().build();
        }
        log.info("Profile found: {}", profile);
        return ResponseEntity.ok(profile);
    }


    @PutMapping("")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Profile> updateProfile(Principal principal, @RequestBody Profile profile) {
        // get the currently logged in username
        String userName = principal.getName();
        // find database user by userId
        User user = userDao.getByUserName(userName);
        int userId = user.getId();


        Profile updatedProfile = profileDao.updateProfile(userId, profile).orElse(null);

        if (updatedProfile == null) {
            return ResponseEntity.notFound().build();
        }
        log.info("Profile updated: {}", updatedProfile);
        return ResponseEntity.ok(updatedProfile);
    }

}

