package com.android.apps.helloworld;

import java.util.Date;

/**
 * Created by pavan on 10/28/2015.
 */
public class UserAction {
    private Integer Id;
    private String ActionDate;
    private String ActionDesc;

    public UserAction(Integer id, String actionDate, String actionDesc) {
        Id = id;
        ActionDate = actionDate;
        ActionDesc = actionDesc;
    }

    public Integer getId() {
        return Id;
    }

    public String getActionDate() {
        return ActionDate;
    }

    public String getActionDesc() {
        return ActionDesc;
    }
}
