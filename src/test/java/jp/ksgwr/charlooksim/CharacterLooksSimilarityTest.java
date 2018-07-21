package jp.ksgwr.charlooksim;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterLooksSimilarityTest {

    @Test
    public void createCharacterLooksTest() {
        int[][] actual = CharacterLooksSimilarity.createCharacterLooks("y");

        for (int i = 0; i < actual.length; i++) {
            for (int j = 0; j < actual[i].length; j++) {
                System.out.print(actual[i][j]);
            }
            System.out.println();
        }
    }

    @Test
    public void calcSimilarityTest() {
        int[][] a = CharacterLooksSimilarity.createCharacterLooks("a");
        int[][] b = CharacterLooksSimilarity.createCharacterLooks("b");

        float sim = CharacterLooksSimilarity.calcSimilarity(a, b);

        assertEquals(sim, 0.89f, 0.01f);

        int[][] l = CharacterLooksSimilarity.createCharacterLooks("l");
        int[][] i = CharacterLooksSimilarity.createCharacterLooks("i");

        sim = CharacterLooksSimilarity.calcSimilarity(l, i);

        assertEquals(sim, 0.98f, 0.01f);

        int[][] I = CharacterLooksSimilarity.createCharacterLooks("I");

        sim = CharacterLooksSimilarity.calcSimilarity(l, I);

        assertEquals(sim, 1.0f, 0.01f);

        sim =  CharacterLooksSimilarity.calcSimilarity(l, l);

        assertEquals(sim, 1.0f, 0.01f);

    }
}
