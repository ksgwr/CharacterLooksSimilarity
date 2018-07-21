package jp.ksgwr.charlooksim;

import javafx.scene.text.Font;
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

        assertEquals(0.85f, sim, 0.01f);

        int[][] l = CharacterLooksSimilarity.createCharacterLooks("l");
        int[][] i = CharacterLooksSimilarity.createCharacterLooks("i");

        sim = CharacterLooksSimilarity.calcSimilarity(l, i);

        assertEquals(0.94f, sim, 0.01f);

        int[][] I = CharacterLooksSimilarity.createCharacterLooks("I");

        sim = CharacterLooksSimilarity.calcSimilarity(l, I);

        assertEquals(0.99f, sim, 0.01f);

        sim =  CharacterLooksSimilarity.calcSimilarity(l, l);

        assertEquals(1.0f, sim, 0.01f);

    }
}
