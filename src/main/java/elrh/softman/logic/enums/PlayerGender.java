package elrh.softman.logic.enums;

public enum PlayerGender {

    M,
    F;

    @Override
    public String toString() {
        if (this == M) {
            return "m";
        } else {
            return "f";
        }
    }

}
