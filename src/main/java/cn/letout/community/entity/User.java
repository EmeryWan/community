package cn.letout.community.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable {

    private static final long serialVersionUID = -6055301435110209507L;

    private int id;

    private String username;

    private String password;

    private String salt;

    private String email;

    private int type;

    private int status;

    /**
     * 注册激活码
     */
    private String activationCode;

    private String headerUrl;

    private Date createTime;

}
