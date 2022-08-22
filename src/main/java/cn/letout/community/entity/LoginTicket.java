package cn.letout.community.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 登录凭证
 */
@Data
public class LoginTicket implements Serializable {

    private static final long serialVersionUID = 5042638559588347785L;

    private int id;

    private int userId;

    private String ticket;

    private int status;

    private Date expired;

}
