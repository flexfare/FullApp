package com.flexfare.android.model;

import java.io.Serializable;

/**
 * Created by Blessyn on 9/3/2017.
 */

public class ServerResponse implements Serializable {

    private String status;
    private Driver message;

    public ServerResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Driver getMessage() {
        return message;
    }

    public void setMessage(Driver message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ServerResponse{" +
                "status='" + status + '\'' +
                ", message=" + message +
                '}';
    }
}
