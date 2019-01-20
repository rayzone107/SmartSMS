package com.rachitgoyal.smartsms.model;

/**
 * Created by Rachit Goyal on 15/01/19.
 */
public class Message extends Model {

    private String _id;
    private String person;
    private String address;
    private String body;
    private String date;
    private String read;
    private String seen;

    public Message(String _id, String person, String address, String body, String date, String
            read, String seen) {
        this._id = _id;
        this.person = person;
        this.address = address;
        this.body = body;
        this.date = date;
        this.read = read;
        this.seen = seen;
    }

    public Message(String person, String address, String body, String date, String read, String seen) {
        this.person = person;
        this.address = address;
        this.body = body;
        this.date = date;
        this.read = read;
        this.seen = seen;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }
}
