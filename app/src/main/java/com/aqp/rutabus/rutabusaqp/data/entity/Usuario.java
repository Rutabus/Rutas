package com.aqp.rutabus.rutabusaqp.data.entity;

public class Usuario {
    private String displayName;
    private String email;
    private String foto;
    private String uid;

    public Usuario() {
    }

    public Usuario(String displayName, String email, String foto, String uid) {
        this.displayName = displayName;
        this.email = email;
        this.foto = foto;
        this.uid = uid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
