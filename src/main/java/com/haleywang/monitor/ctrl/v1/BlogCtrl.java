package com.haleywang.monitor.ctrl.v1;

import com.haleywang.db.Blog;
import com.haleywang.monitor.common.mvc.BaseCtrl;
import com.haleywang.monitor.utils.JsonUtils;

import java.util.*;

/**
 * Created by haley on 2018/8/17.
 */
public class BlogCtrl extends BaseCtrl {


    static List<Blog> blogs = new ArrayList<>();
    static {
        Blog b = new Blog();
        b.setId(1L);
        b.setName("name1");
        blogs.add(b);

        Blog b1 = new Blog();
        b1.setId(2L);
        b1.setName("two");
        blogs.add(b1);

        Blog b3 = new Blog();
        b3.setId(3L);
        b3.setName("post 3");
        blogs.add(b3);
    }


    public String detail() {
        Long id = Long.parseLong(getUrlParam("id"));
        return JsonUtils.toJson(blogs.stream().filter(b -> b.getId() == id).findFirst().orElse(new Blog()));
    }

    public String list() {

        return JsonUtils.toJson(blogs);
    }

    public String cookie() {

        Map<String, String> map = new HashMap<>();
        map.put("aaaa", "bbbb");
        addCookie(map);

        return "{\"name\": \"abc\"}";
    }
}
