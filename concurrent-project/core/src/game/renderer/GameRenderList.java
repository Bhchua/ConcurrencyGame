package game.renderer;

import game.model.ShooterGame;
import java.util.ArrayList;

/**
 * The GameRenderList holds an array of Renderers that represent entities within
 * the game model such as the player and enemies.
 * 
 * @author Brandon Hua
 */

public class GameRenderList {

  private ArrayList<Renderer> renderArray;

  /**
   * The GameRenderList creates Renderers for each entity.
   * 
   * @param game The game to make renderers of
   */
  
  public GameRenderList(ShooterGame game) {
    renderArray = new ArrayList<Renderer>();
    LevelRenderer levelRend = new LevelRenderer(game.getLevel());
    renderArray.add(levelRend);
    VegetationRenderer vegRend = new VegetationRenderer(game.getLevel().getVtiles());
    renderArray.add(vegRend);
    if (game.isNetworked()) {
      PlayerRenderer netPlayerRend = new PlayerRenderer(game.getNetPlayer());
      renderArray.add(netPlayerRend);
    }
    PlayerRenderer playerRend = new PlayerRenderer(game.getPlayer());
    renderArray.add(playerRend);
    EnemyRenderer enemRend = new EnemyRenderer(game.getEnemies());
    renderArray.add(enemRend);
    BulletRenderer bullRend = new BulletRenderer(game.getBullets());
    renderArray.add(bullRend);
    StructureRenderer strucRend = new StructureRenderer(game.getLevel().getStructures());
    renderArray.add(strucRend);
    HealthRenderer healthRend = new HealthRenderer(game.getPlayer());
    renderArray.add(healthRend);
  }

  public ArrayList<Renderer> getRenderArray() {
    return renderArray;
  }

}
