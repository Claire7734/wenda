package com.project.model;

import java.util.Date;

/**
 * Created by Claire on 11/29/17.
 */
public class LoginTicket {

    private int ticketId;
    private int userId;
    private Date expired;
    private int status;// 0 有效，1 无效
    private String ticket;

    public LoginTicket() {
    }

    public LoginTicket(int userId) {
        this.userId = userId;
    }

    public LoginTicket(int userId, int status) {
        this.userId = userId;
        this.status = status;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
}
