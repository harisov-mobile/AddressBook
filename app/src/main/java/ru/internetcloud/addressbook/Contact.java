package ru.internetcloud.addressbook;

public class Contact {

    // private UUID uuid; - попробую обойтись без uuid
    private long _id;
    private String name;
    private String phone;

    public Contact(long _id) {
        // uuid = UUID.randomUUID(); - попробую обойтись без uuid
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getId() {
        return _id;
    }

    public void setId(long _id) {
        this._id = _id;
    }

}
