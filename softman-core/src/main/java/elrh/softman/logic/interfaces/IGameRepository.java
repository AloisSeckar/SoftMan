package elrh.softman.logic.interfaces;

import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.Result;

// whole-world save/load; the only persistence operation the rest of the app is allowed to see
public interface IGameRepository {

    boolean saveExists(String gameId);

    Result save(String gameId, AssociationManager world);

    Result load(String gameId, AssociationManager world);

}
