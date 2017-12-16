package com.qeweb.scm.baseline.common.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.baseline.common.entity.RunAsEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

public interface UserRunAsDao  extends BaseRepository<RunAsEntity, Serializable>,JpaSpecificationExecutor<RunAsEntity>{
	
	@Query(value="delete from qeweb_runas where from_user_id=?1 and to_user_id=?2",nativeQuery=true)
    public void revokeRunAs(Long fromUserId, Long toUserId);

	@Query(value="select count(1) from qeweb_runas where from_user_id=?1 and to_user_id=?2",nativeQuery=true)
    public int exists(Long fromUserId, Long toUserId);

	@Query(value="select fromUser from RunAsEntity r where r.toUserId=?1")
    public List<UserEntity> findFromUserIds(Long toUserId);
	
	@Query(value="select toUser from RunAsEntity r where r.fromUserId=?1")
    public List<UserEntity> findToUserIds(Long fromUserId);

}
