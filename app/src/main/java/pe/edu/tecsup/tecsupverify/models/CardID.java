package pe.edu.tecsup.tecsupverify.models;

public class CardID {

    private Long id;

    private String dni;

    private Boolean active;

    private String expiration;

    private Boolean picture;

    private String porduct;

    private String schedule;

    private String prerequisite;

    private String condition;

    private String pictmpuri;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getPicture() {
        return picture;
    }

    public void setPicture(Boolean picture) {
        this.picture = picture;
    }

    public String getPictmpuri() {
        return pictmpuri;
    }

    public void setPictmpuri(String pictmpuri) {
        this.pictmpuri = pictmpuri;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getPorduct() {
        return porduct;
    }

    public void setPorduct(String porduct) {
        this.porduct = porduct;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getPrerequisite() {
        return prerequisite;
    }

    public void setPrerequisite(String prerequisite) {
        this.prerequisite = prerequisite;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "CardID{" +
                "id=" + id +
                ", dni='" + dni + '\'' +
                ", active=" + active +
                ", expiration='" + expiration + '\'' +
                ", picture=" + picture +
                ", porduct='" + porduct + '\'' +
                ", schedule='" + schedule + '\'' +
                ", prerequisite='" + prerequisite + '\'' +
                ", condition='" + condition + '\'' +
                ", pictmpuri='" + pictmpuri + '\'' +
                '}';
    }
}
