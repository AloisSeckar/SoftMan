package elrh.softman.logic.interfaces;

public interface IDatabaseEntity {

    // get the artificial ID generated in DB
    long getId();

    // store current state into DB
    void persist();

}
