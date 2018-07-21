package jp.ksgwr.charlooksim;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class CharacterLooksSimilarity {
    public static final int BUF_WIDTH = 100;

    public static final int BUF_HEIGHT = 24;

    public static int[][] createCharacterLooks(String content) {
        return createCharacterLooks(content, null);
    }

    public static int[][] createCharacterLooks(String content, Font font) {
        return createCharacterLooks(content, font, BUF_WIDTH, BUF_HEIGHT);
    }

    public static int[][] createCharacterLooks(String content, Font font, int bufWidth, int bufHeight) {
        BufferedImage bufferedImage = new BufferedImage(bufWidth, bufHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bufferedImage.createGraphics();
        if (font != null) {
            graphics.setFont(font);
        }
        FontMetrics fontMetrics = graphics.getFontMetrics();

        int width = fontMetrics.stringWidth(content);
        int height = fontMetrics.getMaxAscent();
        int startX = (bufWidth - width) / 2;
        int maxY = height + fontMetrics.getMaxDescent();

        graphics.drawString(content, startX, height);

        int[][] charLooks = new int[maxY][width];
        for (int i = 0; i < charLooks.length; i++) {
            for (int j = 0; j < charLooks[i].length; j++) {
                charLooks[i][j] = bufferedImage.getRGB(j + startX, i) == -1 ? 1 : 0;
            }
        }
        return charLooks;
    }

}
           
      