package cn.letout.community.controller;

import cn.letout.community.entity.DiscussPost;
import cn.letout.community.entity.Page;
import cn.letout.community.entity.User;
import cn.letout.community.service.DiscussPostService;
import cn.letout.community.service.LikeService;
import cn.letout.community.service.UserService;
import cn.letout.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController implements CommunityConstant {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @GetMapping(path = "/")
    public String root(){
        return "forward:/index";
    }

    // ---

    @GetMapping(path = "/index")
    public String getIndexPage(Model model, Page page, @RequestParam(name = "orderMode", defaultValue = "1") int orderMode) {
        // 方法调用前 SpringMVC会自动实例化Model和Page 并将Page注入Model 所以在thymeleaf中可以直接访问Page对象中的数据.
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index?orderMode=" + orderMode);

        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit(),orderMode);
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (list != null) {
            for (DiscussPost post : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);
                User user = userService.findUserById(post.getUserId());
                map.put("user", user);
                // 点赞数量
                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
                map.put("likeCount", likeCount);

                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);
        model.addAttribute("orderMode", orderMode);

        return "/index";
    }

    @GetMapping(path = "/error")
    public String getErrorPage() {
        return "/error/500";
    }

    // 拒绝访问的提示页面
    @GetMapping(path = {"/denied"})
    public String getDeniedPage() {
        return "/error/404";
    }

}
