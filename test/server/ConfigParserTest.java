package server;

import core.gamemodel.TownName;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by Leonardo Arcari on 13/06/2016.
 */
@RunWith(value = Parameterized.class)
public class ConfigParserTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"test\\default_map.txt", new HashMap<TownName, List<TownName>>(){{ put(TownName.A, Arrays.asList(TownName.C, TownName.B));
                                                                                    put(TownName.B, Arrays.asList(TownName.D, TownName.E, TownName.A));
                                                                                    put(TownName.C, Arrays.asList(TownName.F, TownName.A));
                                                                                    put(TownName.D, Arrays.asList(TownName.B, TownName.G));
                                                                                    put(TownName.E, Arrays.asList(TownName.B, TownName.H));
                                                                                    put(TownName.F, Arrays.asList(TownName.C, TownName.I));
                                                                                    put(TownName.G, Arrays.asList(TownName.D, TownName.J));
                                                                                    put(TownName.H, Arrays.asList(TownName.E, TownName.J, TownName.M));
                                                                                    put(TownName.I, Arrays.asList(TownName.F, TownName.J, TownName.K));
                                                                                    put(TownName.J, Arrays.asList(TownName.I, TownName.G, TownName.H, TownName.L));
                                                                                    put(TownName.K, Arrays.asList(TownName.I, TownName.N));
                                                                                    put(TownName.L, Arrays.asList(TownName.J, TownName.O));
                                                                                    put(TownName.M, Arrays.asList(TownName.H, TownName.O));
                                                                                    put(TownName.N, Arrays.asList(TownName.K, TownName.O));
                                                                                    put(TownName.O, Arrays.asList(TownName.L, TownName.M, TownName.N));}} }
        });
    }

    private File input;
    private Map<TownName, List<TownName>> expectedMap;
    private ConfigParser parser;

    public ConfigParserTest(String filePath, Map<TownName, List<TownName>> expectedMap) {
        Path currentPath = Paths.get("");
        System.out.println(currentPath.toAbsolutePath().toString());
        input = new File(filePath);
        this.expectedMap = expectedMap;
    }

    @Before
    public void setUp() throws Exception {
        parser = new ConfigParser(input);
    }

    @Test
    public void getLinksFor() throws Exception {
        Arrays.asList(TownName.values()).forEach(townName -> {
            List<TownName> expectedList = expectedMap.get(townName);

            Iterator<TownName> parserIterator = parser.getLinksFor(townName);
            List<TownName> parserList = new ArrayList<>();
            parserIterator.forEachRemaining(parserList::add);

            assertTrue(expectedList.containsAll(parserList));
            assertTrue(parserList.containsAll(expectedList));
        });
    }

}