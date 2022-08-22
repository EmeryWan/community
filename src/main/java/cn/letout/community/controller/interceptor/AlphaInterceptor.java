package cn.letout.community.controller.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器 Interceptor 可以对用户的请求进行拦截处理
 * - 在请求到达 Controller 之前，通过拦截器执行一段代码
 * - 在 Controller 执行之后，执行一段代码（此时只是 Controller 执行完毕，视图还没有开始渲染）
 * - 整个请求执行结束后
 *
 * 作用：
 * - 日志记录：信息监控、信息统计、计算 PV(Page View) UV(Unique Visitor) DAU(Daily Active User)
 * - 权限检查：如登录检测
 * - 性能监控：记录请求事件
 * - 通用行为：如读取 Cookie 得到用户信息，将用户信息放入请求，方便后续流程使用
 */
@Slf4j
@Component
public class AlphaInterceptor implements HandlerInterceptor {

    // 在Controller之前执行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("preHandle: " + handler.toString());
        return true;
    }

    // 在Controller之后执行
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.debug("postHandle: " + handler.toString());
    }

    // 在TemplateEngine之后执行
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.debug("afterCompletion: " + handler.toString());
    }

}
