package edu.cornell.gdiac.mangosnoops;

import com.badlogic.gdx.utils.*;
import edu.cornell.gdiac.mangosnoops.hudentity.Radio.*;
import edu.cornell.gdiac.mangosnoops.roadentity.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import edu.cornell.gdiac.mangosnoops.GameplayController.*;


public class LevelObject {
    /** Region that the level takes place in */
    private Region region;
    /** Level speed */
    private float speed;
    /** Number of lanes in the level. */
    private int numLanes;
    /** Total number of blocks defined in the level file */
    private int totalBlocks;
    /** Number of blocks to select from the total number of blocks */
    private int useBlocks;
    /** Padding between enemies */
    private float padding;
    /** Mapping between the level's song genres and its song mp3 files */
    private ObjectMap<Genre,String> songs;
    /** Array of enemies for the level */
    private Array<Gnome> gnomez;

    /** The car */
    private Car yonda;
    /** Inventory */
    // TODO

    /** Speed constants TODO */
    private static final float VERY_SLOW_SPEED = 0.0f;
    private static final float SLOW_SPEED = 0.0f;
    private static final float NORMAL_SPEED = 0.0f;
    private static final float FAST_SPEED = 0.0f;
    private static final float VERY_FAST_SPEED = 0.0f;

    /** Padding constants TODO */
    private static final float LESS_PADDING = 0.0f;
    private static final float NORMAL_PADDING = 0.0f;
    private static final float MORE_PADDING = 0.0f;
    private static final float LANE_SIZE = 0.0f;
    private static final float ROAD_MIDDLE = 0.0f;
    private static final float CONVERT_TO_PIXELS = 0.0f;

    /** Excel spreadsheet constants */
    /** Row and column of the cell containing region information */
    private static final int REGION_ROW = 1;
    private static final int REGION_COL = 0;
    /** Row and column of cell containing speed information */
    private static final int SPEED_ROW = 1;
    private static final int SPEED_COL = 1;
    /** Row and column of the cell containing lane number information */
    private static final int LANE_ROW = 1;
    private static final int LANE_COL = 2;

    /** Row and column of cell for total number of blocks */
    private static final int TOTAL_BLOCKS_ROW = 4;
    private static final int TOTAL_BLOCKS_COL = 0;
    /** Row and column of cell for num blocks to use */
    private static final int USE_BLOCKS_ROW = 4;
    private static final int USE_BLOCKS_COL = 1;
    /** Row and column of cell for enemy padding */
    private static final int PADDING_ROW = 4;
    private static final int PADDING_COL = 2;

    /** Row and column of cells containing song information */
    private static final int SONGS_START_ROW = 8;
    private static final int SONGS_END_ROW = 12;
    private static final int SONG_GENRE_COL = 0;
    private static final int SONG_FILE_COL = 1;

    /** Start of road layout data */
    private static final int ROAD_START_ROW = 1;
    private static final int ROAD_START_COL = 4;

    /**
     * Return the region for this level.
     */
    public Region getRegion() { return region; }

    /**
     * Return the speed for this level.
     */
    public float getSpeed() { return speed; }

    /**
     * Get the number of lanes for this level
     */
    public int getNumLanes() { return numLanes; }

    /**
     * Get the total number of blocks defined in this level file.
     */
    public int getTotalBlocks() { return totalBlocks; }

    /**
     * Get the number of blocks to select to build this level.
     */
    public int getUseBlocks() { return useBlocks; }

    /**
     * Get the padding between enemies for this level.
     */
    public float getPadding() { return padding; }

    /**
     * Return the songs for this level.
     */
    public ObjectMap<Genre,String> getSongs() { return songs; }

    /**
     * Return an array of enemies for this level.
     */
    public Array<Gnome> getGnomez() { return gnomez; }

    /**
     * Return the car for this level.
     */
    public Car getCar() { return yonda; }

    /**
     * Loads in a file to create a Level Object.
     *
     * @param file name of JSON or Excel file with level information.
     * @throws Exception if one is raised while parsing the file
     */
    public LevelObject(String file) throws Exception {
        yonda = new Car();

        // if Excel file -- TODO
        if (true) {
            parseExcel(file);
            // TODO: also make inventory empty
        }

        // if JSON file -- TODO
        else if (true) {
            parseJSON(file);
        }
    }

    public LevelObject() { yonda = new Car(); }

    /**
     * Parse a JSON file for information about a level.
     * @param file name of a JSON file
     */
    public void parseJSON(String file) {

    }

    /**
     * Parse an Excel file for information about a level.
     * @param file name of an Excel file (xlsx or xls)
     * @throws RuntimeException if an invalid setting was given in the level
     * @throws IOException if there is an error in opening/closing file
     * @throws InvalidFormatException if not a proper Excel file
     */
    public void parseExcel(String file) throws Exception {
        try {
            FileInputStream f = new FileInputStream(new File("levels/" + file));

            // Create a workbook and get the first sheet
            Workbook wb = WorkbookFactory.create(f);
            Sheet sh = wb.getSheetAt(0);

            // Region information
            String regionStr = sh.getRow(REGION_ROW).getCell(REGION_COL).getStringCellValue().toLowerCase();
            if (regionStr.equals("the burbs"))
                region = Region.SUBURBS;
            else if (regionStr.equals("highway"))
                region = Region.HIGHWAY;
            else if (regionStr.equals("midwest"))
                region = Region.MIDWEST;
            else if (regionStr.equals("colorado"))
                region = Region.COLORADO;
            else
                throw new RuntimeException("Invalid region");

            // Speed information
            String speedStr = sh.getRow(SPEED_ROW).getCell(SPEED_COL).getStringCellValue().toLowerCase();
            if (speedStr.equals("very slow"))
                speed = VERY_SLOW_SPEED;
            else if (speedStr.equals("slow"))
                speed = SLOW_SPEED;
            else if (speedStr.equals("normal"))
                speed = NORMAL_SPEED;
            else if (speedStr.equals("fast"))
                speed = FAST_SPEED;
            else if (speedStr.equals("very fast"))
                speed = VERY_FAST_SPEED;
            else
                throw new RuntimeException("Invalid speed setting");

            // Number of lanes - throws an IllegalStateException or NumberFormatException if error
            numLanes = (int) sh.getRow(LANE_ROW).getCell(LANE_COL).getNumericCellValue();

            // Total number of blocks and number of blocks to use
            totalBlocks = (int) sh.getRow(TOTAL_BLOCKS_ROW).getCell(TOTAL_BLOCKS_COL).getNumericCellValue();
            useBlocks = (int) sh.getRow(USE_BLOCKS_ROW).getCell(USE_BLOCKS_COL).getNumericCellValue();

            // Padding between enemies
            String pStr = sh.getRow(PADDING_ROW).getCell(PADDING_COL).getStringCellValue().toLowerCase();
            if (pStr.equals("less"))
                padding = LESS_PADDING;
            else if (pStr.equals("normal"))
                padding = NORMAL_PADDING;
            else if (pStr.equals("more"))
                padding = MORE_PADDING;
            else
                throw new RuntimeException("Invalid padding setting");

            // Iterate through cells for songs
            for (int i = SONGS_START_ROW; i <= SONGS_END_ROW; i++) {
                String songFile = sh.getRow(i).getCell(SONG_FILE_COL).getStringCellValue().toLowerCase();
                String genreStr = sh.getRow(i).getCell(SONG_GENRE_COL).getStringCellValue();
                if (genreStr.equals("pop"))
                    songs.put(Genre.POP, songFile);
                else if (genreStr.equals("creepy"))
                    songs.put(Genre.CREEPY, songFile);
                else if (genreStr.equals("dance"))
                    songs.put(Genre.DANCE, songFile);
                else if (genreStr.equals("action"))
                    songs.put(Genre.ACTION, songFile);
                else if (genreStr.equals("jazz"))
                    songs.put(Genre.JAZZ, songFile);
                else if (genreStr.equals("thug"))
                    songs.put(Genre.THUG, songFile);
                else if (genreStr.equals("comedy"))
                    songs.put(Genre.COMEDY, songFile);
                else
                    throw new RuntimeException("Invalid song genre");
            }


            // Iterate through each block
            int roadRow = ROAD_START_ROW;
            int roadCol = ROAD_START_COL; // Always the first column of a block
            int blocksProcessed = 0;
            float miles = 0.0f;

            while (blocksProcessed < totalBlocks) {
                // Iterate through cells for a block until "END" is reached in first column

                while (!sh.getRow(roadRow).getCell(roadCol).getStringCellValue().toUpperCase().equals("END")) {
                    // Convert miles into the y-coordinate for this block
                    float y = miles * CONVERT_TO_PIXELS;

                    // Read the events column - first column of the block TODO
                    String eventStr = sh.getRow(roadRow).getCell(roadCol).getStringCellValue();

                    // Check for enemies in each lane
                    for (int i = 0; i < numLanes; i++) {
                        float x = 0.0f; // TODO
                        String enemyStr = sh.getRow(roadRow).getCell(roadCol + i).getStringCellValue().toLowerCase();
                        if (enemyStr.equals("gnome"))
                            gnomez.add(new Gnome(x, y, Gnome.GnomeType.BASIC));
                        else if (enemyStr.equals("flamingo"))
                            gnomez.add(new Gnome(x, y, Gnome.GnomeType.FLAMINGO));
                        else if (enemyStr.equals("grill"))
                            gnomez.add(new Gnome(x, y, Gnome.GnomeType.GRILL));
                        else
                            throw new RuntimeException("Invalid enemy type");
                    }

                    miles += padding;
                    roadRow += 1;
                }

                // Move to the next block
                blocksProcessed += 1;
                roadCol += numLanes + 1;
            }

            // Close the file
            f.close();

        } catch (IOException e) {
            throw new IOException("Error in closing the file");
        } catch (InvalidFormatException e) {
            throw new InvalidFormatException("Input file format invalid");
        } catch (RuntimeException e) {
            throw e;
        }

    }
}
