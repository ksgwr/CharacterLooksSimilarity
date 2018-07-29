package jp.ksgwr.charlooksim;

import org.junit.Test;

import java.awt.Font;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class CharacterLooksSimilarityTest {

    /** download IPAGothic from https://ipafont.ipa.go.jp/old/ipafont/download.html */
    private final Font FONT = new Font("IPAGothic", Font.PLAIN, 12);

    @Test
    public void fontTest() {
        assertEquals("IPAゴシック", FONT.getFamily());
    }

    @Test
    public void printcreateCharacterLooksTest() {
        int[][] actual = CharacterLooksSimilarity.createCharacterLooks("あ", FONT);

        printCharacter(actual);
    }

    @Test
    public void createCharacterLooksTest() {
        int[][] actual = CharacterLooksSimilarity.createCharacterLooks("y", FONT);

        printCharacter(actual);

        int[][] expected = new int[][]{
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 1, 0},
                {0, 1, 0, 0, 1, 0},
                {0, 0, 1, 0, 1, 0},
                {0, 0, 1, 1, 0, 0},
                {0, 0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0, 0},
                {0, 1, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}
        };

        assertArrayEquals(expected, actual);

    }

    @Test
    public void calcSimilarityTest() {
        int[][] a = CharacterLooksSimilarity.createCharacterLooks("a", FONT);
        int[][] b = CharacterLooksSimilarity.createCharacterLooks("b", FONT);

        float sim = CharacterLooksSimilarity.calcSimilarity(a, b);

        assertEquals(0.83f, sim, 0.01f);

        int[][] l = CharacterLooksSimilarity.createCharacterLooks("l", FONT);
        int[][] i = CharacterLooksSimilarity.createCharacterLooks("i", FONT);

        sim = CharacterLooksSimilarity.calcSimilarity(l, i);

        assertEquals(0.94f, sim, 0.01f);

        int[][] I = CharacterLooksSimilarity.createCharacterLooks("I", FONT);

        sim = CharacterLooksSimilarity.calcSimilarity(l, I);

        assertEquals(0.94f, sim, 0.01f);

        sim = CharacterLooksSimilarity.calcSimilarity(l, l);

        assertEquals(1.0f, sim, 0.01f);

    }

    @Test
    public void countDifferenceTest() {
        int[][] a = new int[][]{
                {0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 1, 1, 1, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0},
        };
        int[][] ua = new int[][]{
                {1},
                {1},
                {1},
                {0},
                {0},
        };
        int[][] da = new int[][]{
                {0},
                {0},
                {1},
                {1},
                {1},
        };
        int[][] ra = new int[][]{
                {0, 0, 1, 1, 1},
        };
        int[][] la = new int[][]{
                {1, 1, 1, 0, 0},
        };

        int diff = CharacterLooksSimilarity.countDifference(a, ua, 0, 1);

        assertEquals(2, diff);

        diff = CharacterLooksSimilarity.countDifference(a, da, 0, -1);
        assertEquals(2, diff);

        diff = CharacterLooksSimilarity.countDifference(a, ra, -1, 0);
        assertEquals(2, diff);

        diff = CharacterLooksSimilarity.countDifference(a, la, 1, 0);
        assertEquals(2, diff);
    }

    private void printCharacter(int[][] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                System.out.print(a[i][j] + ",");
            }
            System.out.println();
        }
        System.out.println();
    }
}
