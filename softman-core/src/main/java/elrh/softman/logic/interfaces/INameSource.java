package elrh.softman.logic.interfaces;

import elrh.softman.logic.enums.PlayerGender;

// supplies name pools to the player factory; backed by the read-only sources DB
public interface INameSource {

    String getRandomFirstName(PlayerGender gender);

    String getRandomLastName();

}
