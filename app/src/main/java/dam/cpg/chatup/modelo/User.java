package dam.cpg.chatup.modelo;

/**
 * Clase que define un usuario de la aplicación.
 *
 * @author Carlos Pérez on 12/06/18. *
 */
public class User {

    private String name;
    private String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
