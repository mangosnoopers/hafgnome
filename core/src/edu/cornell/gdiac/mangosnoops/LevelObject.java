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
    /** True if blocks should be stitched together randomly to form a level */
    private boolean randomSelect;
    /** Random seed */
    private int seed;
    /** Mapping between the level's song genres and its song mp3 files */
    private ObjectMap<Genre,String> songs;
    /** Array of enemies for the level */
    private Array<Gnome> gnomez;
    /** A queue of events for the level.
     *  The first element in the queue is the event to happen the soonest. */
    private Queue<Event> events;
    /** The car */
    private Car yonda;
    /** Inventory */
    // TODO
    /** An internal tracker for number of miles traversed so far in the level */
    private float localMiles;

    /** Speed constants TODO */
    private static final float VERY_SLOW_SPEED = 0.0f;
    private static final float SLOW_SPEED = 0.0f;
    private static final float NORMAL_SPEED = 0.0f;
    private static final float FAST_SPEED = 0.0f;
    private static final float VERY_FAST_SPEED = 0.0f;

    /** Padding constants. Converts each cell to a certain number of miles. */
    private static final float LESS_PADDING_MILES = 2.0f;
    private static final float NORMAL_PADDING_MILES = 5.0f;
    private static final float MORE_PADDING_MILES = 8.0f;
    /** Constant to help calculate x-coordinates of enemies */
    private static final int LANE_X_OFFSET = 2;
    /** A constant that gives the number of pixels per one in-game mile. */
    private static final float MILES_TO_PIXELS = 10.0f;

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
    /** Row and column of the cell containing random selection information */
    private static final int RANDOM_SELECT_ROW = 1;
    private static final int RANDOM_SELECT_COL = 3;

    /** Row and column of cell for total number of blocks */
    private static final int TOTAL_BLOCKS_ROW = 4;
    private static final int TOTAL_BLOCKS_COL = 0;
    /** Row and column of cell for num blocks to use */
    private static final int USE_BLOCKS_ROW = 4;
    private static final int USE_BLOCKS_COL = 1;
    /** Row and column of cell for enemy padding */
    private static final int PADDING_ROW = 4;
    private static final int PADDING_COL = 2;
    /** Row and column of the cell containing seed information */
    private static final int SEED_ROW = 4;
    private static final int SEED_COL = 3;

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
     * Return the seed used to randomly generate this level.
     */
    public int getSeed() { return seed; }

    /**
     * Return the songs for this level.
     */
    public ObjectMap<Genre,String> getSongs() { return songs; }

    /**
     * Return an array of enemies for this level.
     */
    public Array<Gnome> getGnomez() { return gnomez; }

    /**
     * Return a queue of events for this level.
     */
    public Queue<Event> getEvents() { return events; }

    /**
     * Return the car for this level.
     */
    public Car getCar() { return yonda; }

    /**
     * Loads in a file to create a Level Object.
     *
     * @param file name of JSON or Excel file with level information.
     * @throws IOException if one is raised while opening or closing the file
     * @throws InvalidFormatException if Excel input file format is invalid
     * @throws RuntimeException for invalid settings in the Excel level builder or unsupported file types
     */
    public LevelObject(String file) throws IOException, InvalidFormatException, RuntimeException {
        yonda = new Car();
        localMiles = 0.0f;

        // Initialize collections -- TODO: inventory
        songs = new ObjectMap<Genre, String>();
        gnomez = new Array<Gnome>();
        events = new Queue<Event>();

        // if Excel file
        String ext = file.substring(file.lastIndexOf('.') + 1);
        if (ext.equals("xlsx") || ext.equals("xls")) {
            parseExcel(file);
        }

        // if JSON file
        else if (ext.equals("json")) {
            parseJSON(file);
        }

        // not a supported file type
        else {
            throw new RuntimeException("Unsupported file type");
        }

    }

    // TODO: delete
    public LevelObject() {
        yonda = new Car();
        localMiles = 0.0f;
    }

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
    public void parseExcel(String file) throws IOException, InvalidFormatException, RuntimeException {
        try {
            FileInputStream f = new FileInputStream(new File("levels/" + file));

            // Create a workbook and get the first sheet
            Workbook wb = WorkbookFactory.create(f);
            Sheet sh = wb.getSheetAt(0);

            // Data formatter to help with parsing
            DataFormatter df = new DataFormatter();

            // Region information
            String regionStr = df.formatCellValue(sh.getRow(REGION_ROW).getCell(REGION_COL)).toLowerCase();
            if (regionStr.equals("the burbs"))
                region = Region.SUBURBS;
            else if (regionStr.equals("highway"))
                region = Region.HIGHWAY;
            else if (regionStr.equals("midwest"))
                region = Region.MIDWEST;
            else if (regionStr.equals("colorado"))
                region = Region.COLORADO;
            else
                throw new RuntimeException("Invalid region setting");

            // Speed information
            String speedStr = df.formatCellValue(sh.getRow(SPEED_ROW).getCell(SPEED_COL)).toLowerCase();
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

            // Number of lanes
            numLanes = Integer.parseInt(df.formatCellValue(sh.getRow(LANE_ROW).getCell(LANE_COL)));

            // Random selection of blocks
            String randomStr = df.formatCellValue(sh.getRow(RANDOM_SELECT_ROW).getCell(RANDOM_SELECT_COL)).toLowerCase();
            if (randomStr.equals("no"))
                randomSelect = false;
            else if (randomStr.equals("yes"))
                randomSelect = true;
            else
                throw new RuntimeException("Invalid random selection setting");

            // Total number of blocks and number of blocks to use
            totalBlocks = Integer.parseInt(df.formatCellValue(sh.getRow(TOTAL_BLOCKS_ROW).getCell(TOTAL_BLOCKS_COL)));
            useBlocks = Integer.parseInt(df.formatCellValue(sh.getRow(USE_BLOCKS_ROW).getCell(USE_BLOCKS_COL)));

            // Padding between enemies
            String pStr = df.formatCellValue(sh.getRow(PADDING_ROW).getCell(PADDING_COL)).toLowerCase();
            if (pStr.equals("less"))
                padding = LESS_PADDING_MILES;
            else if (pStr.equals("normal"))
                padding = NORMAL_PADDING_MILES;
            else if (pStr.equals("more"))
                padding = MORE_PADDING_MILES;
            else
                throw new RuntimeException("Invalid padding setting");

            // Seed
            seed = Integer.parseInt(df.formatCellValue(sh.getRow(SEED_ROW).getCell(SEED_COL)));

            // Iterate through cells for songs
            for (int i = SONGS_START_ROW; i <= SONGS_END_ROW; i++) {
                String songFile = df.formatCellValue(sh.getRow(i).getCell(SONG_FILE_COL));
                String genreStr = df.formatCellValue(sh.getRow(i).getCell(SONG_GENRE_COL)).toLowerCase();
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
                    throw new RuntimeException("Invalid song genre specified");
            }

            // Iterate through desired number of blocks in order if randomSelect is false
            if (!randomSelect) {
                int roadStartCol = ROAD_START_COL; // Always the first column of a block
                int blocksProcessed = 0;
                while (blocksProcessed < useBlocks) {
                    processExcelBlock(sh, roadStartCol);
                    // Move to the next block
                    blocksProcessed += 1;
                    roadStartCol += numLanes + 1;
                }
            }

            // Else randomly select desired number of blocks
            else {
                // TODO: extend to work w/ random blocks
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

    /** Process a block of enemies and events in an Excel level.
     *
     * @param sh the Excel sheet containing the level
     * @param roadStartCol The first column of the block
     */
    private void processExcelBlock(Sheet sh, int roadStartCol) {
        DataFormatter df = new DataFormatter();

        // Iterate through cells until "END" is reached in first column
        int roadCurrRow = ROAD_START_ROW;
        while (!df.formatCellValue(sh.getRow(roadCurrRow).getCell(roadStartCol)).toUpperCase().equals("END")) {
            // Convert miles into the y-coordinate for this block
            float y = localMiles * MILES_TO_PIXELS;

            // Read the events column - first column of the block
            String eventStr = df.formatCellValue(sh.getRow(roadCurrRow).getCell(roadStartCol)).toLowerCase();
            if (eventStr.equals("rear enemy")) {
                events.addLast(new Event(y, Event.EventType.REAR_ENEMY));
            } else if (eventStr.equals("sun")) {
                events.addLast(new Event(y, Event.EventType.SUN));
            } else if (eventStr.equals("ned wakes up")) {
                events.addLast(new Event(y, Event.EventType.NED_WAKES_UP));
            } else if (eventStr.equals("nosh wakes up")) {
                events.addLast(new Event(y, Event.EventType.NOSH_WAKES_UP));
            } else if (eventStr.equals("sat question")) {
                events.addLast(new Event(y, Event.EventType.SAT_QUESTION));
            } else {
                throw new RuntimeException("Invalid event specified");
            }

            // Starting x-coordinate for rightmost lane
            float x = 0.1f * (numLanes - LANE_X_OFFSET);
            // Check for enemies in each lane
            for (int i = 1; i <= numLanes; i++) {
                // Calculate the x-coordinate for this enemy - decrease by 0.1 for each lane left
                x -= 0.1f;
                String enemyStr = df.formatCellValue(sh.getRow(roadCurrRow).getCell(roadStartCol + i)).toLowerCase();
                if (enemyStr.equals("gnome"))
                    gnomez.add(new Gnome(x, y, Gnome.GnomeType.BASIC));
                else if (enemyStr.equals("flamingo"))
                    gnomez.add(new Gnome(x, y, Gnome.GnomeType.FLAMINGO));
                else if (enemyStr.equals("grill start"))
                    gnomez.add(new Gnome(x, y, Gnome.GnomeType.GRILL));
                    // TODO: grill end
                else
                    throw new RuntimeException("Invalid enemy type specified");
            }

            localMiles += padding;
            roadCurrRow += 1;
        }

    }
}
