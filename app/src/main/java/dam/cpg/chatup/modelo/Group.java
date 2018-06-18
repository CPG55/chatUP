package dam.cpg.chatup.modelo;

/**
 * @author Carlos Pérez on 14/06/18.
 */
public class Group {

    private String groupUID;
    private User oppositeUser;

    public Group () {

    }

    public Group(String groupUID, User oppositeUser) {
        this.groupUID = groupUID;
        this.oppositeUser = oppositeUser;
    }

    public String getGroupUID() {
        return groupUID;
    }

    public User getOppositeUser() {
        return oppositeUser;
    }

    public void setGroupUID(String groupUID) {
        this.groupUID = groupUID;
    }

    public void setOppositeUser(User oppositeUser) {
        this.oppositeUser = oppositeUser;
    }

}
