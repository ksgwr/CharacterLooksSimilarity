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

    public static float calcSimilarity(int[][] a, int[][] b) {
        int maxX;
        // set long width array and short width array
        int[][] lw, sw;
        if (a[0].length < b[0].length) {
            maxX = b[0].length;
            lw = b;
            sw = a;
        } else {
            maxX = a[0].length;
            lw = a;
            sw = b;
        }

        // set short array centering
        int maxY;
        int cxStartX = (lw[0].length - sw[0].length) / 2;
        int cxEndX = (lw[0].length + sw[0].length) / 2;
        int cxStartY, cxEndY;
        if (a.length < b.length) {
            maxY = b.length;
            cxStartY = (b.length - a.length) / 2;
            cxEndY = (b.length + a.length) / 2;
        } else {
            maxY = a.length;
            cxStartY = (a.length - b.length) / 2;
            cxEndY = (a.length + b.length) / 2;
        }

        int diff = 0;

        for (int i=0;i<lw.length;i++) {
            for(int j=0;j<lw[i].length;j++) {
                if (cxStartX <= j && j <= cxEndX) {
                    if (cxStartY <= i && i <= cxEndY) {
                        // cross array
                        if (lw[i][j] != sw[i - cxStartY][j - cxStartX]) {
                            diff++;
                        }
                    } else {
                        // out of short height array as 0
                        if (lw[i][j] == 1) {
                            diff++;
                        }
                    }
                } else {
                    // out of short width array as 0
                    if (lw[i][j] == 1) {
                        diff++;
                    }
                }
            }
        }
        if (lw.length < sw.length) {
            // out of short height array as 0
            for (int i=0;i<cxStartY;i++) {
                for(int j=0;j<sw[i].length;j++) {
                    if (sw[i][j] == 1) {
                        diff++;
                    }
                }
            }
            for (int i=cxEndY+1;i<sw.length;i++) {
                for(int j=0;j<sw[i].length;j++) {
                    if (sw[i][j] == 1) {
                        diff++;
                    }
                }
            }
        }

        return 1.0f - (float) diff / (maxX * maxY);
    }

}
           
      