package game.entities.structures.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import error.GlobalErrors;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * The StructLoader class enables building data to be loaded from a file.
 * 
 * @author Brandon Hua
 */

public class StructLoader {

  private static ArrayList<StructAssets> structs = new ArrayList<StructAssets>();

  /**
   * A method to load buildings from a structure_list file.
   */

  public static void loadStructs() {
    String workingDir = System.getProperty("user.dir") + "\\structures\\structure_list";
    try (BufferedReader br = new BufferedReader(new FileReader(workingDir))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] splitLine = line.split("\t");
        load(splitLine);
      }
    } catch (Exception e) {
      GlobalErrors.setError("Cannot read structure_list file");
    }
  }

  /**
   * A method to add a structure into the ArrayLists.
   * 
   * @param data The data to be used as the building parameters
   */

  public static void load(String[] data) {
    String name = data[0];
    String textureName = data[1];
    int xtiles = Integer.valueOf(data[2]);
    int ytiles = Integer.valueOf(data[3]);
    int xtileTotal = Integer.valueOf(data[4]);
    int ytileTotal = Integer.valueOf(data[5]);
    int layers = Integer.valueOf(data[6]);
    TextureRegion[] regions = getTextureRegions(textureName, layers, xtiles, ytiles, xtileTotal,
        ytileTotal);
    structs.add(new StructAssets(name, regions, layers));
  }

  /**
   * A method that splits up a texture into all of it's components and returns the
   * correctly adjusted TextureRegions.
   * 
   * @param fileName   The name of the texture file.
   * @param layers     The number of layers it has.
   * @param xtiles     The number of tiles on the x axis the image is split into.
   * @param ytiles     The number of tiles on the y axis the image is split into.
   * @param xtileCount The number of tiles each part uses on the x axis.
   * @param ytileCount The number of tiles each part uses on the y axis.
   * @return The TextureRegions of the building.
   */

  public static TextureRegion[] getTextureRegions(String fileName, int layers, int xtiles,
      int ytiles, int xtileCount, int ytileCount) {

    try {
      Texture texture = new Texture(fileName);
      TextureRegion[][] tmp = TextureRegion.split(texture,
          texture.getWidth() / (xtileCount / xtiles), texture.getHeight() / (ytileCount / ytiles));
      TextureRegion[] regions = new TextureRegion[layers];

      for (int i = 0; i < layers; i++) {
        int col = i % (xtileCount / xtiles);
        int row = i / (xtileCount / xtiles);
        regions[i] = tmp[row][col];
      }
      return regions;
    } catch (Exception e) {
      GlobalErrors.setError("Could not load structure: " + fileName);
    }
    return null;
  }

  /**
   * The StructAssets class holds all the assets and data required to render a
   * structure.
   * 
   * @author Brandon Hua
   */

  public static class StructAssets {

    private String name;
    private TextureRegion[] regions;
    private int layers;

    /**
     * The StructAssets initialises all parameters required for rendering.
     * 
     * @param name The name of the structure.
     * @param regions The TextureRegions of the structure.
     * @param layers The number layers a structure has.
     */
    
    public StructAssets(String name, TextureRegion[] regions, int layers) {
      this.name = name;
      this.regions = regions;
      this.layers = layers;
    }

    public String getName() {
      return name;
    }

    public TextureRegion[] getRegions() {
      return regions;
    }

    public int getLayers() {
      return layers;
    }
  }
  
  public static ArrayList<StructAssets> getStructs() {
    return structs;
  }

}
