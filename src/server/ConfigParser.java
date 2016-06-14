package server;

import core.gamemodel.TownName;

import java.io.*;
import java.util.*;

/**
 * Created by Leonardo Arcari on 13/06/2016.
 */
public class ConfigParser implements Closeable{
    private Scanner in;
    private Map<TownName, List<TownName>> links;


    public ConfigParser(File configFile) throws FileNotFoundException, SyntaxErrorException {
        in = new Scanner(configFile);
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

    public Iterator<TownName> getLinksFor(TownName town) {
        return links.get(town).iterator();
    }

    @Override
    public void close() throws IOException {
        in.close();
    }

    public class SyntaxErrorException extends Exception {
        public SyntaxErrorException() {
        }

        public SyntaxErrorException(String message) {
            super(message);
        }
    }
}
