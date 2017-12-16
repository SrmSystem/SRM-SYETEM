package com.qeweb.scm.basemodule.service;

/**
 * 系统消息服务
 * @author ALEX
 *
 */
public interface IMessageService {
    
    /**
     * 
     * @param messageId 
     */
    public void closeMessage(long messageId);

    /**
     * 发给特定用户
     *
     * @param moduleId
     * @param message
     * @param toUserCodes
     */
    public void sendToUsers(long moduleId, String message, String[] toUserCodes);
    
    /**
     * 发给特定用户
     *
     * @param moduleId
     * @param message
     * @param fromUserId
     * @param toUserCodes
     */
    public void sendToUsers(long moduleId, String message, long fromUserId, String[] toUserCodes);

    /**
     * 发给特定用户
     *
     * @param moduleId
     * @param message
     * @param toUserIds
     */
    public void sendToUsers(long moduleId, String message, Long[] toUserIds);
    
    /**
     * 发给特定用户
     *
     * @param moduleId
     * @param message
     * @param fromUserId
     * @param toUserIds
     */
    public void sendToUsers(long moduleId, String message, long fromUserId, Long[] toUserIds);

    /**
     * 发给特定组织下的所有用户
     *
     * @param moduleId
     * @param message
     * @param orgIds
     */
    public void sendToOrgUsers(long moduleId, String message, Long[] orgIds);
    
    /**
     * 发给特定组织下的所有用户
     *
     * @param moduleId
     * @param message
     * @param fromUserId
     * @param orgIds
     */
    public void sendToOrgUsers(long moduleId, String message, long fromUserId, Long[] orgIds);

}
