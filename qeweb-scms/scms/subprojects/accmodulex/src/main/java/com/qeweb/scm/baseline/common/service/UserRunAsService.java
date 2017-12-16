package com.qeweb.scm.baseline.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qeweb.scm.baseline.common.entity.RunAsEntity;
import com.qeweb.scm.baseline.common.repository.UserRunAsDao;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.repository.UserDao;

@Service
public class UserRunAsService {
    @Autowired
    private UserRunAsDao userRunAsDao;
    @Autowired
    private UserDao userDao;
    

    public void grantRunAs(Long fromUserId, Long toUserId) {
        RunAsEntity rs = new RunAsEntity();
        rs.setFromUser(userDao.findOne(fromUserId));
        rs.setToUser(userDao.findOne(toUserId));
        userRunAsDao.save(rs);
    }

    public void revokeRunAs(Long fromUserId, Long toUserId) {
        userRunAsDao.revokeRunAs(fromUserId, toUserId);
    }

    public boolean exists(Long fromUserId, Long toUserId) {
        return userRunAsDao.exists(fromUserId, toUserId)!=0;
    }

    public List<UserEntity> findFromUserIds(Long toUserId) {
        return userRunAsDao.findFromUserIds(toUserId);
    }

    public List<UserEntity> findToUserIds(Long fromUserId) {
        return userRunAsDao.findToUserIds(fromUserId);
    }
}
