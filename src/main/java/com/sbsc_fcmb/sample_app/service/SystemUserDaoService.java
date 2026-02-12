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

    public SystemUser findByCode(String code)  {
        return repository.findByCode(code).map(entity -> modelMapper.map(entity, SystemUser.class)).orElse(null);
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

    public String userCode(String userName) {
        return userName + LocalDateTime.now().getNano();
    }
}
