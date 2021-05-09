package ru.internetcloud.addressbook;

import java.util.UUID;

public class Contact {

    private long _id;
    private String name;
    private String phone;
    private String email;
    private String street;
    private String city;
    private String state;
    private String zip;
    private UUID uuid; // для сохранения фото-файла

    public Contact(long contactId) {
        this.uuid = UUID.randomUUID(); // для сохранения фото-файла
        this._id = contactId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhotoFilename() {
        return "IMG_" + getUuid().toString() + ".jpg";
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuidfromString(String uuidString) {
        this.uuid = UUID.fromString(uuidString);
    }
}
