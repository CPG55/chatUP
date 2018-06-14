package dam.cpg.chatup.modelo;

/**
 * Clase que define un usuario de la aplicación.
 *
 * @author Carlos Pérez on 12/06/18.
 */
public class User {

    private String name;
    private String email;
    private String profilePictureURL;

    public User() {

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

    public String getProfilePictureURL() { return profilePictureURL; }

    public void setProfilePictureURL(String profilePictureURL) { this.profilePictureURL = profilePictureURL; }

}
