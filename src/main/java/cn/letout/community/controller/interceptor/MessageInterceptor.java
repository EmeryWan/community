package cn.letout.community.controller.interceptor;

import cn.letout.community.entity.User;
import cn.letout.community.service.MessageService;
import cn.letout.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 消息拦截器，为了在首页显示未读消息数量
 */
@Component
public class MessageInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private MessageService messageService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if (user != null && modelAndView != null) {
            int noticeUnreadCount = messageService.findNoticeUnreadCount(user.getId(), null);
            int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(), null);
            modelAndView.addObject("allUnreadCount", noticeUnreadCount + letterUnreadCount);
        }
    }

}
