package jp.ksgwr.charlooksim;

import org.junit.Test;

public class CharacterLooksSimilarityTest {

    @Test
    public void createCharacterLooksTest() {
        int[][] actual = CharacterLooksSimilarity.createCharacterLooks("あ");

        for (int i=0;i<actual.length;i++) {
            for(int j=0;j<actual[i].length;j++) {
                System.out.print(actual[i][j]);
            }
            System.out.println();
        }
    }
}
