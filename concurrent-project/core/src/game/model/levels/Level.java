package game.model.levels;

import game.entities.Spawner;
import game.entities.Wall;
import game.entities.structures.Structure;
import game.entities.tiles.VegetationTile;
import game.model.ShooterGame;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The Level class holds all data related to a level, such as enemies, walls and
 * obstacles.
 * 
 * @author Brandon Hua
 */

public class Level {

  private ArrayList<Wall> walls;
  private ArrayList<VegetationTile> vtiles;
  private ArrayList<Structure> strcts;
  private ArrayList<Spawner> spawners;
  private String bgFileName;

  private int height;
  private int width;
  private int xcenter;
  private int ycenter;

  /**
   * The constructor of a level.
   * 
   * @param fileName The name of the file which holds the level data.
   * @throws FileNotFoundException If the level file cannot be found.
   * @throws IOException           If the data held in the level file is invalid.
   */

  public Level(String fileName) throws FileNotFoundException, IOException {

    walls = new ArrayList<Wall>();
    vtiles = new ArrayList<VegetationTile>();
    strcts = new ArrayList<Structure>();
    spawners = new ArrayList<Spawner>();

    String workingDir = System.getProperty("user.dir") + "\\levels\\" + fileName;

    try (BufferedReader br = new BufferedReader(new FileReader(workingDir))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] splitLine = line.split("\\s");
        stringParser(splitLine);
      }
    }
  }

  // ===============================================================================================
  // Rules for different string inputs from the file
  // ===============================================================================================

  /**
   * The StringRule interface provides a way to add different rules to Strings,
   * dictating how a level is loaded.
   * 
   * @author Brandon Hua
   */

  public interface StringRule {
    /**
     * A method to check if the given string matches the condition.
     * 
     * @param comparison The string to compare.
     * @return A boolean for whether or not it matches.
     */
    public boolean ruleCondition(String comparison);

    /**
     * A method to perform the action of the rule.
     * 
     * @param data The string of the loaded data.
     */
    public void action(String[] data);
  }

  /**
   * The StringRule for a wall object.
   * 
   * @author Brandon Hua
   */

  public class WallRule implements StringRule {

    @Override
    public boolean ruleCondition(String comparison) {
      return comparison.equals("wall");
    }

    @Override
    public void action(String[] data) {
      int wallx = Integer.valueOf(data[1]);
      int wally = Integer.valueOf(data[2]);
      int wallWidth = Integer.valueOf(data[3]);
      int wallHeight = Integer.valueOf(data[4]);
      walls.add(new Wall(wallx, wally, wallWidth, wallHeight));
    }
  }

  /**
   * The StringRule for a VegitationTile object.
   * 
   * @author Brandon Hua
   */

  public class VegRule implements StringRule {

    @Override
    public boolean ruleCondition(String comparison) {
      return comparison.equals("veg");
    }

    @Override
    public void action(String[] data) {
      int vegx = Integer.valueOf(data[1]);
      int vegy = Integer.valueOf(data[2]);
      int vegWidth = Integer.valueOf(data[3]);
      int vegHeight = Integer.valueOf(data[4]);
      String vegType = data[5];
      vtiles.add(new VegetationTile(vegx, vegy, vegWidth, vegHeight, vegType));
    }
  }

  /**
   * The StringRule for a Structure object.
   * 
   * @author Brandon Hua
   */

  public class StructRule implements StringRule {

    @Override
    public boolean ruleCondition(String comparison) {
      return comparison.equals("struct");
    }

    @Override
    public void action(String[] data) {
      int structx = Integer.valueOf(data[1]);
      int structy = Integer.valueOf(data[2]);
      int structWidth = Integer.valueOf(data[3]);
      int structHeight = Integer.valueOf(data[4]);
      String structType = data[5];
      Boolean structCol = Boolean.valueOf(data[6]);
      strcts.add(new Structure(structx, structy, structWidth, structHeight, structType, structCol));
    }
  }

  /**
   * The StringRule for the background.
   * 
   * @author Brandon Hua
   */

  public class BgRule implements StringRule {

    @Override
    public boolean ruleCondition(String comparison) {
      return comparison.equals("background");
    }

    @Override
    public void action(String[] data) {
      bgFileName = data[1];
    }
  }

  /**
   * The StringRule for the center of the level.
   * 
   * @author Brandon Hua
   */

  public class CenterRule implements StringRule {

    @Override
    public boolean ruleCondition(String comparison) {
      return comparison.equals("center");
    }

    @Override
    public void action(String[] data) {
      xcenter = Integer.valueOf(data[1]);
      ycenter = Integer.valueOf(data[2]);
    }
  }

  /**
   * The StringRule for the size of the map.
   * 
   * @author Brandon Hua
   */

  public class SizeRule implements StringRule {

    @Override
    public boolean ruleCondition(String comparison) {
      return comparison.equals("size");
    }

    @Override
    public void action(String[] data) {
      width = Integer.valueOf(data[1]);
      height = Integer.valueOf(data[2]);
    }
  }

  /**
   * The StringRule for a Spawner object.
   * 
   * @author Brandon Hua
   */

  public class SpawnerRule implements StringRule {

    @Override
    public boolean ruleCondition(String comparison) {
      return comparison.equals("spawner");
    }

    @Override
    public void action(String[] data) {
      int xpos = Integer.valueOf(data[1]);
      int ypos = Integer.valueOf(data[2]);
      int interval = Integer.valueOf(data[3]);
      spawners.add(new Spawner(xpos, ypos, interval));
    }
  }

  private ArrayList<StringRule> rules = new ArrayList<StringRule>(Arrays.asList(
      new BgRule(), new CenterRule(), new SizeRule(), new WallRule(), 
      new VegRule(), new StructRule(), new SpawnerRule()));

  // ===============================================================================================
  // Rule interfaces end
  // ===============================================================================================

  /**
   * A method that takes a String array and iterates through the StringRules.
   * 
   * @param splitLine The String to interpret.
   */

  public void stringParser(String[] splitLine) {
    for (StringRule r : rules) {
      if (r.ruleCondition(splitLine[0])) {
        r.action(splitLine);
      }
    }
  }

  /**
   * Get the name of the background file.
   * 
   * @return The name of the background file.
   */

  public String getBgFileName() {
    return bgFileName;
  }

  /**
   * Get the walls that are contained in the level.
   * 
   * @return The walls of the level.
   */

  public ArrayList<Wall> getWalls() {
    return walls;
  }

  public ArrayList<VegetationTile> getVtiles() {
    return vtiles;
  }

  public ArrayList<Structure> getStructures() {
    return strcts;
  }

  public int getXPos() {
    return xcenter;
  }

  public int getYPos() {
    return ycenter;
  }

  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }

  /**
   * A method to get the edges of a level.
   * 
   * @return The bounds in order of left, right, bottom, top.
   */

  public int[] getBounds() {
    int[] bounds = new int[4];
    bounds[0] = (int) (xcenter - (width / 2));
    bounds[1] = (int) (xcenter + (width / 2));
    bounds[2] = (int) (ycenter - (height / 2));
    bounds[3] = (int) (ycenter + (height / 2));
    return bounds;
  }

  /**
   * A method that updates the spawner objects.
   * 
   * @param gameSpeed The speed of the game.
   * @param game      The game that the spawners effect.
   */

  public void spawnerUpdate(float gameSpeed, ShooterGame game) {
    for (Spawner s : spawners) {
      s.update(gameSpeed, game);
    }
  }

}
