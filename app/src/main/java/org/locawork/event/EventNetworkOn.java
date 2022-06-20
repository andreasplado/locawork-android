package org.locawork.event;

public class EventNetworkOn {

    private Object data;

    public EventNetworkOn(Object data){
        this.data = data;
    }


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
