package com.example.demo.service;

import com.example.demo.dtos.request.RegisterRequest;
import com.example.demo.dtos.request.users.UpdateUserRequest;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.jwt.JwtTokenUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.InvalidKeyException;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    @Transactional
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public Optional<User> findUserById(Long id){
        return userRepository.findById(id);
    }


    @Override
    @Transactional
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    @Transactional
    public User createUser(RegisterRequest registerRequest) {
        String email = registerRequest.getEmail();
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()){
            throw new BadRequestException("email adready existed");
        }
        Role role = roleRepository.findById(4L)
                .orElseThrow(
                        () -> new NotFoundException("role not found")
                );

        User user = User.builder()
                .name(registerRequest.getName())
                .gender(registerRequest.getGender())
                .email(registerRequest.getEmail())
                .phone(registerRequest.getPhone())
                .address(registerRequest.getAddress())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .isActive(1)
                .build();

        user.setRole(role);

        return userRepository.save(user);
    }


    @Override
    @Transactional
    public String login(String email, String password) throws InvalidKeyException {
        Optional<User> optionalUser = findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new BadRequestException("Invalid email/password");
        }

        User existingUser = optionalUser.get();

        if (!passwordEncoder.matches(password, existingUser.getPassword())) {
            throw new BadRequestException("invalid email/password");
        }
        // authenticate
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        password,
                        existingUser.getAuthorities()
                )
        );

        return jwtTokenUtils.generateToken(existingUser);
    }

    // send Mail
    @Override
    public void sendMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    @Override
    public String generateToken(User user) throws InvalidKeyException {
        return jwtTokenUtils.generateToken(user);
    }

    @Override
    @Transactional
    public User resetPassword(String email, String newPassword){
        User user = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new NotFoundException("Invalid email")
                );
        String encodePassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodePassword);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(User user, UpdateUserRequest updateUserRequest) {
        user.setName(updateUserRequest.getName());
        user.setAddress(updateUserRequest.getAddress());
        user.setPhone(updateUserRequest.getPhone());
        user.setAvatar(updateUserRequest.getAvatar());
        user.setGender(updateUserRequest.getGender());
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }


}
