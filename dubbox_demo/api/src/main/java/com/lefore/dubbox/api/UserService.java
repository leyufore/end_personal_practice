package com.lefore.dubbox.api;

import com.lefore.dubbox.entity.User;
import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * 注意：如果消费端想要访问提供者的rest服务，这里需要加上JAX-RS的Annotation
 * <p>
 * author: lefore
 * date: 2017/8/27
 * email: 862080515@qq.com
 */

@Service("userService")
@Path("user")
@Produces({"application/json;charset=UTF-8", "text/xml;charset=UTF-8"})
public interface UserService {

    @GET
    @Path("/getUserByPhone/{phone}/")
    User getUserByPhone(@PathParam("phone") String phone);

    @GET
    @Path("/getUserByName/{name}/")
    User getUserByName(@PathParam("name") String name);

}
