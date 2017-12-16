package com.qeweb.scm.basemodule.utils;

import com.qeweb.scm.basemodule.context.SpringContextUtils;
import com.qeweb.scm.basemodule.service.IMessageService;

public abstract class MsgServiceUtil {

    public static IMessageService getInstance() {
        return (IMessageService) SpringContextUtils.getBean("messageService");
    }
}
