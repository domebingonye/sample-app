package com.sbsc_fcmb.sample_app.service;

import com.querydsl.core.types.Predicate;
import com.sbsc_fcmb.sample_app.dto.BaseResponse;
import com.sbsc_fcmb.sample_app.dto.SystemUser;
import com.sbsc_fcmb.sample_app.enums.ResponseCodes;
import com.sbsc_fcmb.sample_app.enums.UserRoleType;
import com.sbsc_fcmb.sample_app.model.SystemUserEntity;
import com.sbsc_fcmb.sample_app.repository.SystemUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SystemUserDaoService {
    private final ModelMapper modelMapper;
    private final SystemUserRepository repository;

    public SystemUser findByUsername(String userName) {
        return repository.findByUsername(userName).map(systemUserEntity -> modelMapper.map(systemUserEntity, SystemUser.class)).orElse(null);
    }

    public SystemUser findByEmail(String email) {
        return repository.findByEmail(email).map(systemUserEntity -> modelMapper.map(systemUserEntity, SystemUser.class)).orElse(null);
    }


    public SystemUser save(SystemUser systemUser) {
        var response = repository.save(modelMapper.map(systemUser, SystemUserEntity.class));
        log.info("SAVING USER INFO::: ::: " + systemUser);

        return modelMapper.map(response, SystemUser.class);
    }

    public SystemUser findById(Long id) {
        return repository.findById(id).map(entity -> modelMapper.map(entity, SystemUser.class)).orElse(null);
    }

    public SystemUser findByCode(String code)  {
        return repository.findByCode(code).map(entity -> modelMapper.map(entity, SystemUser.class)).orElse(null);
    }

    public List<SystemUser> getAllUsers() {
        return repository.findAll().stream().map(entity -> modelMapper.map(entity, SystemUser.class)).collect(Collectors.toList());
    }

    public SystemUser update(String code, SystemUser systemUser) {
        SystemUser user = findByCode(code);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user  with code " + code + " does not exists");
        }
        systemUser.setId(user.getId());
        modelMapper.map(systemUser, user);
        BeanUtils.copyProperties(systemUser, user, "password");

        return save(user);
    }

    public SystemUser updateByUsername(String username, SystemUser systemUser) {
        SystemUser user = findByUsername(username);
        if (ObjectUtils.isEmpty(user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user  with username " + username + " does not exists");
        }
        systemUser.setId(user.getId());
        systemUser.setCode(user.getCode());
        systemUser.setPassword(user.getPassword());
        systemUser.setUserRoleType(!ObjectUtils.isEmpty(systemUser.getUserRoleType()) ? systemUser.getUserRoleType() : UserRoleType.USER);
        modelMapper.map(systemUser, user);
        return save(user);
    }

    public String updateRole(String code, UserRoleType roleType) {
        var user = findByCode(code);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user  with code " + code + " does not exists");
        }
        user.setId(user.getId());
        user.setUserRoleType(roleType);
        update(code, user);

        return user.getUsername() + " has updated to " + roleType + " successfully";
    }

    public String delete(String code) {

        var user = findByCode(code);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user  with code " + code + " does not exists");
        }
        repository.delete(modelMapper.map(user, SystemUserEntity.class));

        return user.getUsername() + " Deleted Successfully";
    }

    public String userCode(String userName) {
        return userName + LocalDateTime.now().getNano();
    }


    public Page<SystemUser> findAll(final Predicate predicate, final PageRequest pageRequest) {
        return repository.findAll(predicate, pageRequest).map(entity -> modelMapper.map(entity, SystemUser.class));
    }

    public SystemUser findByUsernameOrEmail(String emailOrUsername) {
        return repository.findByUsernameOrEmail(emailOrUsername, emailOrUsername).map(systemUserEntity -> modelMapper.map(systemUserEntity, SystemUser.class)).orElse(null);
    }

    public BaseResponse updateUserStatus(String username, boolean active){
        SystemUser systemUser = Optional.ofNullable(findByUsername(username)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No record found for user with username: " + username));
        systemUser.setActive(active);
        save(systemUser);
        return BaseResponse.builder()
                .code(ResponseCodes.SUCCESS.getCode())
                .message(ResponseCodes.SUCCESS.getMessage())
                .build();
    }

    public boolean validateUserNameOrEmail(String userName, String email){
        return repository.existsByUsernameOrEmail(userName, email);
    }
}
