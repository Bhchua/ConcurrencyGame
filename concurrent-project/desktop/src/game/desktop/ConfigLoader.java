package game.desktop;

import game.CameraVariables;
import game.Core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The config loader loads a given .cfg file and initialises several game
 * variables such as resolution and zoom.
 * 
 * @author Brandon Hua
 */

public class ConfigLoader {

  StringRule[] rules;

  /**
   * The constructor initialises the rules list.
   */

  public ConfigLoader() {
    rules = new StringRule[] { new WidthRule(), new HeightRule(), new ZoomRule(),
        new StructResRule() };
  }

  /**
   * A method the reads lines from a file and uses the StringParser method to
   * interpret them. This method also creates a default config file via the
   * createConfig() method if one is not found.
   * 
   * @param fileName The name of the config file.
   * @throws IOException If the config file cannot be read.
   */

  public void loadConfig(String fileName) throws IOException {
    String workingDir = System.getProperty("user.dir") + "\\config\\" + fileName;
    try (BufferedReader br = new BufferedReader(new FileReader(workingDir))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] splitLine = line.split("\t");
        stringParser(splitLine);
      }
    } catch (FileNotFoundException f) {
      createConfig(workingDir);
      loadConfig(fileName);
    }
  }

  /**
   * A method to create a config file if one does not exist.
   * 
   * @param dir The filepath of the config file.
   * @return A boolean value representing if a file has been made.
   * @throws IOException If the file cannot be made.
   */

  public boolean createConfig(String dir) throws IOException {
    File file = new File(dir);
    if (file.createNewFile()) {
      System.out.println("File is created!");
      FileWriter writer = new FileWriter(file);
      writer.write("[Window size]\n");
      writer.write("window_width\t800\n");
      writer.write("window_height\t600\n");
      writer.write("zoom\t2\t1\t1\t1\n");
      writer.write("s_res\t1\n");
      writer.close();
      return true;
    } else {
      System.out.println("File already exists.");
    }
    return false;
  }

  /**
   * A method to iterate through the StringRules and perform the appropriate
   * action when a certain string is found.
   * 
   * @param line The lines of the file.
   */

  public void stringParser(String[] line) {
    for (StringRule r : rules) {
      if (r.ruleCondition(line[0])) {
        try {
          r.action(line);
        } catch (Exception e) {
          System.out
              .println("Invalid data in the config file, please delete it and restart the game");
        }
      }
    }
  }

  // ===============================================================================================
  // Rules for different string inputs from the file
  // ===============================================================================================

  /**
   * The StringRule interface provides methods to compare the currently read
   * string and perform an action based on it.
   * 
   * @author Brandon Hua
   */

  public interface StringRule {

    /**
     * Whether or not the string is the same as the name of the rule.
     * 
     * @param comparison The string being read.
     * @return A boolean for whether or not it is the same.
     */
    public boolean ruleCondition(String comparison);

    /**
     * The action of the rule.
     * 
     * @param data The parameters to perform the action with.
     */
    public void action(String[] data);
  }

  /**
   * A method that sets the initial width of the Core.
   * 
   * @author Brandon Hua
   */

  public class WidthRule implements StringRule {

    @Override
    public boolean ruleCondition(String comparison) {
      return comparison.equals("window_width");
    }

    @Override
    public void action(String[] data) {
      int width = Integer.valueOf(data[1]);
      Core.setWidth(width);
    }

  }

  /**
   * A method that sets the initial height of the Core.
   * 
   * @author Brandon Hua
   */

  public class HeightRule implements StringRule {

    @Override
    public boolean ruleCondition(String comparison) {
      return comparison.equals("window_height");
    }

    @Override
    public void action(String[] data) {
      int height = Integer.valueOf(data[1]);
      Core.setHeight(height);
    }

  }

  /**
   * A method that sets the different zoom level of the CameraVariables.
   * 
   * @author Brandon Hua
   */

  public class ZoomRule implements StringRule {

    @Override
    public boolean ruleCondition(String comparison) {
      return comparison.equals("zoom");
    }

    @Override
    public void action(String[] data) {
      CameraVariables.zoomConfig[0] = Integer.valueOf(data[1]);
      CameraVariables.zoomConfig[1] = Integer.valueOf(data[2]);
      CameraVariables.zoomConfig[2] = Integer.valueOf(data[3]);
      CameraVariables.zoomConfig[3] = Integer.valueOf(data[4]);
    }

  }

  /**
   * A StringRule that sets the resolution of structures.
   * 
   * @author Brandon Hua
   */

  public class StructResRule implements StringRule {

    @Override
    public boolean ruleCondition(String comparison) {
      return comparison.equals("s_res");
    }

    @Override
    public void action(String[] data) {
      CameraVariables.sres = Integer.valueOf(data[1]);
    }

  }

}
