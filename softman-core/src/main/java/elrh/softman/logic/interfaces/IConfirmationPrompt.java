package elrh.softman.logic.interfaces;

// asks the driver a yes/no question; the desktop client shows a dialog, a server would answer from stored orders
@FunctionalInterface
public interface IConfirmationPrompt {

    boolean confirm(String question);

}
