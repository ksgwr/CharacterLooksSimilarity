package jp.ksgwr.charlooksim;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class CharacterLooksSimilarity {
    public static final int BUF_WIDTH = 100;

    public static final int BUF_HEIGHT = 24;

    public static final Font DEFAULT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);

    public static int[][] createCharacterLooks(String content) {
        return createCharacterLooks(content, DEFAULT_FONT);
    }

    public static int[][] createCharacterLooks(String content, Font font) {
        return createCharacterLooks(content, font, BUF_WIDTH, BUF_HEIGHT);
    }

    public static int[][] createCharacterLooks(String content, Font font, int bufWidth, int bufHeight) {
        BufferedImage bufferedImage = new BufferedImage(bufWidth, bufHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setFont(font);
        FontMetrics fontMetrics = graphics.getFontMetrics();

        int width = fontMetrics.stringWidth(content);
        int height = fontMetrics.getMaxAscent();
        int maxY = height + fontMetrics.getMaxDescent();

        graphics.drawString(content, 0, height);

        int[][] charLooks = new int[maxY][width];
        for (int i = 0; i < charLooks.length; i++) {
            for (int j = 0; j < charLooks[i].length; j++) {
                charLooks[i][j] = bufferedImage.getRGB(j, i) == -1 ? 1 : 0;
            }
        }
        return charLooks;
    }

    public static float calcSimilarity(int[][] a, int[][] b) {
        // lやIの境界条件を考慮してx軸方向で2つ探索して良い方を取る
        return calcSimilarity(a, b, 0, 2, 0, 1);
    }

    public static float calcSimilarity(int[][] a, int[][] b, int startInclusiveDeltaX, int endExclusiveDeltaX, int startInclusiveDeltaY, int endExclusiveDeltaY) {
        int maxX = Math.max(a[0].length, b[0].length);
        int maxY = Math.max(a.length, b.length);

        int minDiff = Integer.MAX_VALUE;
        for (int i=startInclusiveDeltaY;i<endExclusiveDeltaY;i++) {
            for (int j = startInclusiveDeltaX; j < endExclusiveDeltaX; j++) {
                int diff = countDifference(a, b, j, i);
                if (diff < minDiff) {
                    minDiff = diff;
                }
            }
        }

        return 1.0f - (float) minDiff / (maxX * maxY);
    }

    public static int countDifference(int[][] a, int[][] b) {
        return countDifference(a, b, 0, 0);
    }

    public static int countDifference(int[][] a, int[][] b, int deltaX, int deltaY) {
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
        int cxStartX = (lw[0].length - sw[0].length ) / 2 + deltaX;
        int cxEndX = (lw[0].length + sw[0].length ) / 2 + deltaX;
        int cxStartY, cxEndY;
        if (a.length < b.length) {
            maxY = b.length;
            cxStartY = (b.length - a.length ) / 2 + deltaY;
            cxEndY = (b.length + a.length ) / 2 + deltaY;
        } else {
            maxY = a.length;
            cxStartY = (a.length - b.length) / 2 + deltaY;
            cxEndY = (a.length + b.length) / 2 + deltaY;
        }

        int diff = 0;

        for (int i = 0; i < lw.length; i++) {
            for (int j = 0; j < lw[i].length; j++) {
                if (cxStartX <= j && j < cxEndX) {
                    if (cxStartY <= i && i < cxEndY) {
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
            for (int i = 0; i < cxStartY; i++) {
                for (int j = 0; j < sw[i].length; j++) {
                    if (sw[i][j] == 1) {
                        diff++;
                    }
                }
            }
            for (int i = cxEndY + 1; i < sw.length; i++) {
                for (int j = 0; j < sw[i].length; j++) {
                    if (sw[i][j] == 1) {
                        diff++;
                    }
                }
            }
        }

        return diff;
    }

}
           
      