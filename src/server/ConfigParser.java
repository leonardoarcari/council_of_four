package server;

import core.gamemodel.TownName;

import java.io.*;
import java.util.*;

/**
 * A <code>ConfigParser</code> class is responsible to parse a configuration file describing the topology of a game
 * map. The input file must be a text file having on each line with text one of the following regular expressions
 * <ul>
 *     <li>#{a,b,c,..}*
 *     <ul><li><i>Example: # This is a comment</i></li></ul>
 *     </li>
 *     <li>{a, b, ..., o, A, ..., O} - {a, b, ..., o, A, ..., O}
 *     <ul><li><i>Example: C - E (This means that town C is connected to E)</i></li></ul>
 *     </li>
 *     <li>{a, b, ..., o, A, ..., O} - {{a, b, ..., o, A, ..., O} - {a, b, ..., o, A, ..., O}}+
 *     <ul><li><i>Example: C - E - A (This means that town C is connected to E which is connected to A)</i></li></ul>
 *     </li>
 * </ul>
 */
public class ConfigParser implements Closeable{
    private Scanner in;
    private Map<TownName, List<TownName>> links;
    private final String fileName;

    /**
     * Initializes a <code>ConfigParser</code> by parsing <code>configFile</code> loading the map topology
     * @param configFile Text file to parse
     * @param fileName Name of the file without extension (for internal purpose)
     * @throws FileNotFoundException in case <code>configFile</code> could not be open
     * @throws SyntaxErrorException in case <code>configFile</code> does not follow the format described above
     */
    public ConfigParser(File configFile, String fileName) throws FileNotFoundException, SyntaxErrorException {
        in = new Scanner(configFile);
        this.fileName = fileName;
        links = new HashMap<>();
        Arrays.asList(TownName.values()).forEach(name -> links.put(name, new ArrayList<>()));
        buildMap();
    }

    private void buildMap() throws SyntaxErrorException {
        while (in.hasNextLine()) {
            String line = in.nextLine();
            parseLine(line);
        }
    }

    private void parseLine(String line) throws SyntaxErrorException {
        String toParse = line.trim().toUpperCase().replaceAll(" ", "");
        if (toParse.startsWith("#") || toParse.isEmpty()) return; // It was a comment or a black link. Ignore it.
        if (toParse.length() < 3) throw new SyntaxErrorException("Line: " + toParse + " has too few arguments");

        String link = toParse;
        while (link.length() >= 3) {
            if (link.charAt(1) != '-') throw new SyntaxErrorException("Link: " + link + " doesn't have a townName separator");
            char formerTown = link.charAt(0);
            char latterTown = link.charAt(2);
            if (!isValidTownName(formerTown) || !isValidTownName(latterTown)) throw new SyntaxErrorException("Invalid town name in link: " + link);
            TownName formerName = TownName.valueOf(String.valueOf(formerTown));
            TownName latterName = TownName.valueOf(String.valueOf(latterTown));
            addToNeighbours(formerName, latterName);
            addToNeighbours(latterName, formerName);
            link = link.substring(2);
        }
        if (toParse.lastIndexOf(link) != toParse.length() - 1) throw new SyntaxErrorException("Malformed line: " + toParse);
    }

    private boolean isValidTownName(char c) {
        TownName[] names = TownName.values();
        for (int i = 0; i < names.length; i++) {
            if (c == names[i].name().charAt(0)) return true;
        } return false;
    }

    private void addToNeighbours(TownName town, TownName neighbour) {
        List<TownName> neighbours = links.get(town);
        if (!neighbours.contains(neighbour)) neighbours.add(neighbour);
    }

    /**
     * Provides to the caller the towns the input town is linked with.
     * @param town Town to returns nearby towns
     * @return An iterator of towns <code>town</code> is connected to
     */
    public Iterator<TownName> getLinksFor(TownName town) {
        return links.get(town).iterator();
    }

    @Override
    public void close() throws IOException {
        in.close();
    }

    /**
     * @return The config file name (without extension)
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Signals that a configuration file passed as argument to a {@link ConfigParser ConfigParser} does not follow the
     * accepted format. This excpetion will be thrown by the ConfigParse itself at initialization.
     */
    public class SyntaxErrorException extends Exception {
        public SyntaxErrorException() {
        }

        public SyntaxErrorException(String message) {
            super(message);
        }
    }
}
