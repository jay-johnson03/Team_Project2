package project2.classes;

public class user {
    private int id;
    private String name;
    private String email;
    private Boolean isProfessor;

    public user(int id, String name, String email, Boolean isProfessor) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.isProfessor = isProfessor;
    }
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public Boolean getIsProfessor() {
        return isProfessor;
    }

}
