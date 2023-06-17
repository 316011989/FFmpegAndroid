package com.frank.live.util;

/**
 * Tool of transforming YUV format
 * Created by frank on 2018/7/1.
 */

public class YUVUtil {

    public static void rgbaToYUV420p(int[] argb, byte[] yuv, int width, int height) {
        final int frameSize = width * height;
        int index = 0;
        int yIndex = 0;
        int uIndex = frameSize;
        int vIndex = frameSize * 5 / 4;
        int R, G, B, Y, U, V;

        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                R = (argb[index] & 0xff0000) >> 16;
                G = (argb[index] & 0xff00) >> 8;
                B = (argb[index] & 0xff);

                // RGB to YUV algorithm
                Y = ((66 * R + 129 * G + 25 * B + 128) >> 8) + 16;

                // I420(YUV420p) -> YYYYYYYY UU VV
                yuv[yIndex++] = (byte) Y;
                if (j % 2 == 0 && i % 2 == 0) {
                    U = ((-38 * R - 74 * G + 112 * B + 128) >> 8) + 128;
                    V = ((112 * R - 94 * G - 18 * B + 128) >> 8) + 128;
                    yuv[uIndex++] = (byte) U;
                    yuv[vIndex++] = (byte) V;
                }
                index++;
            }
        }
    }

    private static int yuv2argb(int y, int u, int v) {
        int r, g, b;

        r = y + (int) (1.402f * u);
        g = y - (int) (0.344f * v + 0.714f * u);
        b = y + (int) (1.772f * v);
        r = r > 255 ? 255 : Math.max(r, 0);
        g = g > 255 ? 255 : Math.max(g, 0);
        b = b > 255 ? 255 : Math.max(b, 0);
        return 0xff000000 | (r << 16) | (g << 8) | b;
    }

    private static void yuv420pToARGB(byte[] yuv, int[] argb, int width, int height) {
        int size = width * height;
        int offset = size;
        int u, v, y1, y2, y3, y4;

        for (int i = 0, k = 0; i < size; i += 2, k += 2) {
            y1 = yuv[i] & 0xff;
            y2 = yuv[i + 1] & 0xff;
            y3 = yuv[width + i] & 0xff;
            y4 = yuv[width + i + 1] & 0xff;

            u = yuv[offset + k] & 0xff;
            v = yuv[offset + k + 1] & 0xff;
            u = u - 128;
            v = v - 128;

            argb[i] = yuv2argb(y1, u, v);
            argb[i + 1] = yuv2argb(y2, u, v);
            argb[width + i] = yuv2argb(y3, u, v);
            argb[width + i + 1] = yuv2argb(y4, u, v);

            if (i != 0 && (i + 2) % width == 0)
                i += width;
        }
    }

    /**
     * 旋转90°,但是方向错误
     *
     * @param dst
     * @param src
     * @param width
     * @param height
     */
    public static void YUV420pRotate90(byte[] dst, byte[] src, int width, int height) {
        int n = 0;
        int wh = width * height;
        int halfWidth = width / 2;
        int halfHeight = height / 2;

        // y
        for (int j = 0; j < width; j++) {
            for (int i = height - 1; i >= 0; i--) {
                dst[n++] = src[width * i + j];
            }
        }
        // u
        for (int i = 0; i < halfWidth; i++) {
            for (int j = 1; j <= halfHeight; j++) {
                dst[n++] = src[wh + ((halfHeight - j) * halfWidth + i)];
            }
        }
        // v
        for (int i = 0; i < halfWidth; i++) {
            for (int j = 1; j <= halfHeight; j++) {
                dst[n++] = src[wh + wh / 4 + ((halfHeight - j) * halfWidth + i)];
            }
        }
    }

    /**
     * 旋转270°,但是镜像反的
     */
    public static byte[] YUV420pRotate270(byte[] src, int width, int height) {
        byte[] dst = new byte[width * height * 3 / 2];
        int pos = 0;
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        //旋转Y
        int k = 0;
        for (int i = width - 1; i >= 0; i--) {
            for (int j = 0; j < height; j++) {
                dst[k++] = src[j * width + i];
            }
        }
        //旋转U
        pos = width * height;
        for (int i = halfWidth - 1; i >= 0; i--) {
            for (int j = 0; j < halfHeight; j++) {
                dst[k++] = src[pos + j * halfWidth + i];
            }
        }

        //旋转V
        pos = width * height * 5 / 4;
        for (int i = halfWidth - 1; i >= 0; i--) {
            for (int j = 0; j < halfHeight; j++) {
                dst[k++] = src[pos + j * halfWidth + i];
            }
        }

        return dst;
    }


    public static byte[] rotateYUV420Degree180(byte[] src, int width, int height) {
        byte[] yuv = new byte[width * height * 3 / 2];
        int i = 0;
        int count = 0;
        for (i = width * height - 1; i >= 0; i--) {
            yuv[count] = src[i];
            count++;
        }
//        i = width * height * 3 / 2 - 1;
        for (i = width * height * 3 / 2 - 1; i >= width
                * height; i -= 2) {
            yuv[count++] = src[i - 1];
            yuv[count++] = src[i];
        }
        return yuv;
    }

}
