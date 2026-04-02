package com.lib.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import com.Utils.AICameraDeviceUtils;
import com.Utils.PrinterModel;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.lib.Beans.FitTransparentBean;
import com.lib.blueUtils.BluetoothOrder;
import com.lib.blueUtils.PrintDataUtils;
import com.lib.blueUtils.PrinterModelUtils;
import com.minilzo.MiniLZO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.Deflater;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.pdfbox.pdmodel.documentinterchange.taggedpdf.PDLayoutAttributeObject;
import org.apache.pdfbox.pdmodel.interactive.measurement.PDNumberFormatDictionary;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;

public abstract class ImageDisposeUtil {
    static int[] histogram = new int[256];
    public static int level = 14;

    public enum MergeDirection {
        HORIZONTAL,
        VERTICAL
    }

    private static int clampQuality(int i) {
        if (i < 1) {
            return 1;
        }
        if (i > 100) {
            return 100;
        }
        return i;
    }

    public static Bitmap getShowBitmap(Context context, Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        return bitmap;
    }

    public static Bitmap getShowBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        return bitmap;
    }

    public static Bitmap getShowBitmap(Bitmap bitmap, boolean z) {
        if (bitmap == null) {
            return null;
        }
        return bitmap;
    }

    public static Bitmap toGray(Bitmap bitmap) {
        return ImageUtils.toGray(bitmap);
    }

    public static Bitmap clip(Bitmap bitmap, int i, int i2, int i3, int i4) {
        if (bitmap != null) {
            try {
                if (bitmap.getWidth() != 0) {
                    if (bitmap.getHeight() != 0) {
                        return ImageUtils.clip(bitmap, i, i2, i3, i4);
                    }
                }
            } catch (Exception e) {
                LogUtils.e(e);
            }
        }
        return null;
    }

    public static Bitmap cropCircleFromBitmap(Bitmap bitmap, float f, float f2, float f3) {
        int i = (int) (2.0f * f3);
        Bitmap createBitmap = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Rect rect = new Rect((int) (f - f3), (int) (f2 - f3), (int) (f + f3), (int) (f2 + f3));
        if (rect.left < 0) {
            rect.left = 0;
        }
        if (rect.top < 0) {
            rect.top = 0;
        }
        if (rect.right > bitmap.getWidth()) {
            rect.right = bitmap.getWidth();
        }
        if (rect.bottom > bitmap.getHeight()) {
            rect.bottom = bitmap.getHeight();
        }
        new Rect(0, 0, createBitmap.getWidth(), createBitmap.getHeight());
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        BitmapShader bitmapShader = new BitmapShader(bitmap, tileMode, tileMode);
        Matrix matrix = new Matrix();
        matrix.setTranslate((float) (-rect.left), (float) (-rect.top));
        bitmapShader.setLocalMatrix(matrix);
        paint.setShader(bitmapShader);
        canvas.drawCircle(f3, f3, f3, paint);
        return createBitmap;
    }

    public static Bitmap clip(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        return ImageUtils.clip(bitmap, 1, 2, bitmap.getWidth() - 1, bitmap.getHeight() - 2);
    }

    public static Bitmap fitBitmap(Bitmap bitmap, int i) {
        return fitBitmap(bitmap, i, false, false);
    }

    public static Bitmap fitBitmap(Bitmap bitmap, int i, boolean z) {
        return fitBitmap(bitmap, i, false, z);
    }

    public static Bitmap fitBitmap(Bitmap bitmap, int i, boolean z, boolean z2) {
        if (bitmap == null) {
            try {
                if (bitmap.isRecycled()) {
                    return null;
                }
            } catch (Exception e) {
                LogUtils.e(e);
                return null;
            }
        }
        int width = bitmap.getWidth();
        float f = (float) i;
        int height = (int) (((float) bitmap.getHeight()) * (f / ((float) width)));
        PrinterModel.DataBean printerModel = PrinterModelUtils.getPrinterModel();
        if (printerModel.getSize() == 8) {
            String string = SPUtils.getInstance().getString(Code.paperWidth, "");
            if ((printerModel.isA4XII() && (StringUtils.isEmpty(string) || Code.m210.equals(string))) || Code.EIGHT_A4.equals(SPUtils.getInstance().getString(Code.EIGHTPAPER))) {
                int eightImageMaxHigh = PrinterModelUtils.getEightImageMaxHigh(printerModel);
                if (height > eightImageMaxHigh) {
                    i = (int) (f * (((float) eightImageMaxHigh) / ((float) height)));
                    height = eightImageMaxHigh;
                }
                return mergeBitmap(Bitmap.createScaledBitmap(bitmap, i, height, true), PrinterModelUtils.getPrintImgSize(), eightImageMaxHigh);
            }
        } else if (printerModel.isLabelDevice() && printerModel.getLabelDeviceSize() == 0.5f) {
            Bitmap rotate = rotate(bitmap, 1);
            int width2 = rotate.getWidth();
            int height2 = (int) (((float) rotate.getHeight()) * (f / ((float) width2)));
            if (width2 != i) {
                return Bitmap.createScaledBitmap(rotate, i, height2, true);
            }
            return bitmap;
        }
        return width != i ? Bitmap.createScaledBitmap(bitmap, i, height, true) : bitmap;
    }

    public static Bitmap fitBitmap1(Bitmap bitmap, int i) {
        if (bitmap == null) {
            try {
                if (bitmap.isRecycled()) {
                    return null;
                }
            } catch (Exception e) {
                LogUtils.e(e);
                return bitmap;
            }
        }
        int width = bitmap.getWidth();
        return width != i ? Bitmap.createScaledBitmap(bitmap, i, (int) (((float) bitmap.getHeight()) * (((float) i) / ((float) width))), true) : bitmap;
    }

    public static Bitmap fitBitmap(Bitmap bitmap, int i, int i2) {
        if (bitmap != null) {
            try {
                if (!bitmap.isRecycled()) {
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    if (width <= i && height <= i2) {
                        return bitmap;
                    }
                    float f = (float) width;
                    float f2 = (float) height;
                    float min = Math.min(((float) i) / f, ((float) i2) / f2);
                    return Bitmap.createScaledBitmap(bitmap, Math.round(f * min), Math.round(f2 * min), true);
                }
            } catch (Exception e) {
                LogUtils.e(e);
            }
        }
        return null;
    }

    public static Bitmap fitBitmap1(Bitmap bitmap, int i, int i2) {
        if (bitmap == null) {
            try {
                if (bitmap.isRecycled()) {
                    return null;
                }
            } catch (Exception e) {
                LogUtils.e(e);
                return null;
            }
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width <= i && height <= i2) {
            return bitmap;
        }
        float f = (float) width;
        float f2 = ((float) i) / f;
        float f3 = (float) height;
        float f4 = ((float) i2) / f3;
        if (f2 <= f4) {
            return Bitmap.createScaledBitmap(bitmap, (int) (f * f4), i2, true);
        }
        return Bitmap.createScaledBitmap(bitmap, i, (int) (f3 * f2), true);
    }

    public static Bitmap fitLabelBitmap(Bitmap bitmap, int i) {
        if (bitmap == null) {
            try {
                if (bitmap.isRecycled()) {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float f = (float) i;
        float f2 = (float) width;
        float f3 = f / f2;
        if (f3 > 1.0f) {
            f3 = f2 / f;
        }
        return width != i ? Bitmap.createScaledBitmap(bitmap, i, (int) (((float) height) * f3), true) : bitmap;
    }

    public static Bitmap fitBitmapToWidthAndHeight(Bitmap bitmap, int i, int i2) {
        if (bitmap == null) {
            try {
                if (bitmap.isRecycled()) {
                    return null;
                }
            } catch (Exception e) {
                LogUtils.e(e);
                return null;
            }
        }
        float width = (float) bitmap.getWidth();
        float f = ((float) i) / width;
        float height = (float) bitmap.getHeight();
        float f2 = ((float) i2) / height;
        if (f >= f2) {
            return Bitmap.createScaledBitmap(bitmap, (int) (width * f2), i2, true);
        }
        return Bitmap.createScaledBitmap(bitmap, i, (int) (height * f), true);
    }

    public static Bitmap fitHtmlBitmap(Bitmap bitmap, int i) {
        if (bitmap == null) {
            return null;
        }
        try {
            int width = bitmap.getWidth();
            if (bitmap.getWidth() != i) {
                int height = (int) (((float) bitmap.getHeight()) * ComputeUtils.div(i, width, 10));
                if (width != i) {
                    return Bitmap.createScaledBitmap(bitmap, i, height, true);
                }
            }
            return bitmap;
        } catch (Exception e) {
            LogUtils.e(e);
            return bitmap;
        }
    }

    public static Bitmap fitBitmapHeight(Bitmap bitmap, int i) {
        if (bitmap == null) {
            return null;
        }
        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int eightImageMaxHigh = PrinterModelUtils.getEightImageMaxHigh();
            if (height <= eightImageMaxHigh) {
                return bitmap;
            }
            Matrix matrix = new Matrix();
            float f = ((float) eightImageMaxHigh) / ((float) height);
            matrix.postScale(f, f);
            Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
            Bitmap createBitmap2 = Bitmap.createBitmap(i, createBitmap.getHeight(), Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(createBitmap2);
            Paint paint = new Paint();
            canvas.drawColor(-1);
            canvas.drawBitmap(createBitmap2, 0.0f, 0.0f, paint);
            return ImageUtils.addImageWatermark(createBitmap2, createBitmap, (i - createBitmap.getWidth()) / 2, 0, 255);
        } catch (Exception e) {
            LogUtils.e(e);
            return null;
        }
    }

    public static int getPrintHeight(Bitmap bitmap) {
        if (bitmap == null) {
            return 0;
        }
        try {
            String string = SPUtils.getInstance().getString(Code.paperWidth, "");
            if (PrinterModelUtils.getPrinterModel().isA4XII()) {
                if (StringUtils.isEmpty(string)) {
                    return 30;
                }
                if (Code.m210.equals(string)) {
                    return 30;
                }
            }
            if (bitmap.getWidth() != PrinterModelUtils.getPrintSize()) {
                bitmap = fitBitmap(bitmap, PrinterModelUtils.getPrintSize());
            }
            if (bitmap == null) {
                return 0;
            }
            return (int) Math.ceil((((double) bitmap.getHeight()) / ((double) PrinterModelUtils.getoneLength())) / 10.0d);
        } catch (Exception e) {
            LogUtils.e(e);
            return 0;
        }
    }

    public static int getPrintHeight(List<Bitmap> list) {
        if (list != null) {
            try {
                if (list.size() != 0) {
                    int i = 0;
                    for (Bitmap printHeight : list) {
                        i += getPrintHeight(printHeight);
                    }
                    return i;
                }
            } catch (Exception e) {
                LogUtils.e(e);
            }
        }
        return 0;
    }

    public static int getPrintHeight(double d, int i) {
        try {
            String string = SPUtils.getInstance().getString(Code.paperWidth, "");
            if (!PrinterModelUtils.getPrinterModel().isA4XII() || (!StringUtils.isEmpty(string) && !Code.m210.equals(string))) {
                return (int) Math.ceil(((d / ((double) PrinterModelUtils.getoneLength())) / 10.0d) * ((double) i));
            }
            return i * 30;
        } catch (Exception e) {
            LogUtils.e(e);
            return 0;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, int i) {
        if (bitmap != null) {
            try {
                if (!bitmap.isRecycled()) {
                    return rotate(bitmap, i * 90, (float) (bitmap.getWidth() / 2), (float) (bitmap.getHeight() / 2));
                }
            } catch (Exception e) {
                LogUtils.e(e);
            }
        }
        return bitmap;
    }

    public static Bitmap printText(Bitmap bitmap) {
        return printText(bitmap, false);
    }

    public static Bitmap printText(Bitmap bitmap, boolean z) {
        if (bitmap == null) {
            return null;
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap.Config config = Bitmap.Config.ARGB_8888;
            options.inPreferredConfig = config;
            Bitmap decodeByteArray = BitmapFactory.decodeByteArray(byteArrayOutputStream.toByteArray(), 0, byteArrayOutputStream.size(), options);
            Mat mat = new Mat();
            Mat mat2 = new Mat();
            Utils.bitmapToMat(decodeByteArray, mat);
            Imgproc.cvtColor(mat, mat2, 7);
            Imgproc.adaptiveThreshold(mat2, mat2, 255.0d, 1, 0, 51, 18.0d);
            Bitmap createBitmap = Bitmap.createBitmap(mat2.cols(), mat2.rows(), config);
            Utils.matToBitmap(mat2, createBitmap);
            return createBitmap;
        } catch (UnsatisfiedLinkError e) {
            LogUtils.e(e);
            return bitmap;
        } catch (NullPointerException unused) {
            return bitmap;
        }
    }

    public static Bitmap applyAdaptiveThresholdFilter(Bitmap bitmap) {
        try {
            Mat mat = new Mat();
            Utils.bitmapToMat(bitmap, mat);
            Mat mat2 = new Mat();
            Imgproc.cvtColor(mat, mat2, 6);
            Mat mat3 = new Mat();
            Imgproc.bilateralFilter(mat2, mat3, 5, 75.0d, 75.0d);
            int max = Math.max(3, 3);
            Mat mat4 = new Mat();
            double d = (double) max;
            Imgproc.GaussianBlur(mat3, mat4, new Size(d, d), 0.0d);
            Mat mat5 = new Mat(mat4.size(), CvType.CV_8UC1);
            Imgproc.adaptiveThreshold(mat4, mat5, 255.0d, 0, 0, 11, 2.0d);
            Bitmap createBitmap = Bitmap.createBitmap(mat5.cols(), mat5.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mat5, createBitmap);
            return createBitmap;
        } catch (Exception e) {
            LogUtils.e(e);
            return bitmap;
        }
    }

    public static Bitmap printText(Bitmap bitmap, int i, int i2) {
        if (bitmap == null) {
            return null;
        }
        try {
            Mat mat = new Mat();
            Mat mat2 = new Mat();
            Utils.bitmapToMat(bitmap, mat);
            Imgproc.cvtColor(mat, mat, 7);
            Imgproc.adaptiveThreshold(mat, mat2, 255.0d, 1, 0, i, (double) i2);
            Bitmap createBitmap = Bitmap.createBitmap(mat2.cols(), mat2.rows(), Bitmap.Config.RGB_565);
            Utils.matToBitmap(mat2, createBitmap);
            LogUtils.e("文字打印");
            return createBitmap;
        } catch (UnsatisfiedLinkError e) {
            LogUtils.e(e);
            return null;
        } catch (NullPointerException unused) {
            return null;
        }
    }

    public static Bitmap getBitmap(CharSequence charSequence, int i, boolean z) {
        return getBitmap(charSequence, i, z, true, false, false, false, false);
    }

    public static Bitmap getBitmap(CharSequence charSequence, int i, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6) {
        Layout.Alignment alignment;
        try {
            TextPaint textPaint = new TextPaint();
            textPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
            textPaint.setStrokeWidth(1.0f);
            textPaint.setTextSize((float) (PrinterModelUtils.getPrintSize() / i));
            textPaint.setUnderlineText(z);
            textPaint.setFakeBoldText(z3);
            if (z4) {
                textPaint.setTextSkewX(-0.25f);
            } else {
                textPaint.setTextSkewX(0.0f);
            }
            if (z5) {
                alignment = Layout.Alignment.ALIGN_CENTER;
            } else if (z6) {
                alignment = Layout.Alignment.ALIGN_OPPOSITE;
            } else {
                alignment = Layout.Alignment.ALIGN_NORMAL;
            }
            StaticLayout staticLayout = new StaticLayout(charSequence, textPaint, PrinterModelUtils.getPrintSize(), alignment, 1.0f, 0.0f, false);
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            Bitmap createBitmap = Bitmap.createBitmap(PrinterModelUtils.getPrintSize(), (int) Math.ceil((double) (((float) Math.ceil((double) (fontMetrics.descent - fontMetrics.ascent))) * ((float) staticLayout.getLineCount()))), Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(createBitmap);
            canvas.translate(1.0f, 3.0f);
            canvas.drawColor(-1);
            staticLayout.draw(canvas);
            canvas.save();
            canvas.restore();
            return z2 ? textBitmap(createBitmap) : createBitmap;
        } catch (RuntimeException unused) {
            return null;
        }
    }

    public static ArrayList<Bitmap> getBitmaps(Context context, CharSequence charSequence) {
        return getBitmaps(context, charSequence, 17, false);
    }

    public static ArrayList<Bitmap> getBitmaps(Context context, CharSequence charSequence, boolean z) {
        return getBitmaps(context, charSequence, 17, z);
    }

    public static ArrayList<Bitmap> getBitmaps(Context context, CharSequence charSequence, int i, boolean z) {
        try {
            TextPaint textPaint = new TextPaint();
            textPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
            textPaint.setStrokeWidth(1.0f);
            textPaint.setTextSize((float) (PrinterModelUtils.getPrintSize() / i));
            textPaint.setUnderlineText(z);
            textPaint.setTypeface(Typeface.createFromAsset(context.getAssets(), "siyuansongti.otf"));
            StaticLayout staticLayout = new StaticLayout(charSequence, textPaint, PrinterModelUtils.getPrintSize(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            int ceil = (int) Math.ceil((double) (((float) Math.ceil((double) (fontMetrics.descent - fontMetrics.ascent))) * ((float) staticLayout.getLineCount())));
            LogUtils.e(Integer.valueOf(ceil));
            Bitmap createBitmap = Bitmap.createBitmap(PrinterModelUtils.getPrintSize(), ceil, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(createBitmap);
            canvas.translate(1.0f, 3.0f);
            canvas.drawColor(-1);
            staticLayout.draw(canvas);
            canvas.save();
            canvas.restore();
            Bitmap textBitmap = textBitmap(createBitmap);
            ArrayList<Bitmap> arrayList = new ArrayList<>();
            int height = textBitmap.getHeight();
            if (height > 9000) {
                int i2 = 0;
                while (height >= i2) {
                    LogUtils.e(Integer.valueOf(i2));
                    Bitmap cropBitmapCustom = cropBitmapCustom(textBitmap, 0, i2, textBitmap.getWidth(), 9000, false);
                    i2 += 9000;
                    arrayList.add(cropBitmapCustom);
                }
                return arrayList;
            }
            arrayList.add(textBitmap);
            return arrayList;
        } catch (RuntimeException unused) {
            return null;
        }
    }

    public static Bitmap cropBitmapCustom(Bitmap bitmap, int i, int i2, int i3, int i4, boolean z) {
        try {
            Log.d("danxx", "cropBitmapRight before w : " + bitmap.getWidth());
            Log.d("danxx", "cropBitmapRight before h : " + bitmap.getHeight());
            if (i + i3 > bitmap.getWidth()) {
                i3 = bitmap.getWidth() - i;
            }
            if (i2 + i4 > bitmap.getHeight()) {
                i4 = bitmap.getHeight() - i2;
            }
            Bitmap createBitmap = Bitmap.createBitmap(bitmap, i, i2, i3, i4);
            Log.d("danxx", "cropBitmapRight after w : " + createBitmap.getWidth());
            Log.d("danxx", "cropBitmapRight after h : " + createBitmap.getHeight());
            return createBitmap;
        } catch (Exception e) {
            LogUtils.e(e);
            return null;
        }
    }

    public static Bitmap newBitmap(Bitmap bitmap, Bitmap bitmap2) {
        int width = bitmap.getWidth();
        if (bitmap2.getWidth() != width) {
            int height = (bitmap2.getHeight() * width) / bitmap2.getWidth();
            Bitmap createBitmap = Bitmap.createBitmap(width, bitmap.getHeight() + height, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(createBitmap);
            Bitmap resizeBitmap = resizeBitmap(bitmap2, width, height);
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
            canvas.drawBitmap(resizeBitmap, 0.0f, (float) bitmap.getHeight(), (Paint) null);
            return createBitmap;
        }
        Bitmap createBitmap2 = Bitmap.createBitmap(width, bitmap.getHeight() + bitmap2.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas2 = new Canvas(createBitmap2);
        canvas2.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
        canvas2.drawBitmap(bitmap2, 0.0f, (float) bitmap.getHeight(), (Paint) null);
        return createBitmap2;
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, int i, int i2) {
        Bitmap bitmap2;
        float f = (float) i;
        try {
            Matrix matrix = new Matrix();
            matrix.postScale(f / ((float) bitmap.getWidth()), ((float) i2) / ((float) bitmap.getHeight()));
            bitmap2 = bitmap;
            try {
                return Bitmap.createBitmap(bitmap2, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            } catch (Exception e) {
                e = e;
                LogUtils.e(e);
                return bitmap2;
            }
        } catch (Exception e2) {
            e = e2;
            bitmap2 = bitmap;
            LogUtils.e(e);
            return bitmap2;
        }
    }

    public static Bitmap textBitmap(Bitmap bitmap) {
        Bitmap bitmap2 = bitmap;
        if (bitmap2 == null) {
            return null;
        }
        try {
            int width = bitmap2.getWidth();
            int height = bitmap2.getHeight();
            int[] iArr = new int[(width * height)];
            for (int i = 0; i < height; i++) {
                for (int i2 = 0; i2 < width; i2++) {
                    int pixel = bitmap2.getPixel(i2, i);
                    if (!(pixel == -1 || i2 == 0 || i == 0 || i2 == width - 1 || i == height - 1)) {
                        int i3 = i2 - 1;
                        int i4 = (i - 1) * width;
                        int i5 = iArr[i3 + i4];
                        int i6 = i * width;
                        int i7 = iArr[i3 + i6];
                        int i8 = (i + 1) * width;
                        if (((iArr[i3 + i8] + 255) & (i7 + 255) & i5 & 255) == 0) {
                            int i9 = iArr[i2 + 2 + i6] & 255;
                            int i10 = iArr[i2 + 1 + i6] & 255;
                            int i11 = i7 & 255;
                            int i12 = iArr[(i2 - 2) + i6] & 255;
                            if (i10 == 0 && i11 == 0 && !(i9 == 0 && i12 == 0)) {
                                int i13 = iArr[i2 + i6];
                                pixel = -1;
                            }
                        }
                        int i14 = iArr[i2 + i4];
                        if (((i14 + 255) & i5 & (iArr[i2 + 1 + i4] + 255) & 255) == 0) {
                            int i15 = i14 & 255;
                            if ((iArr[i8 + i2] & 255) == 255 && i15 == 0) {
                                LogUtils.e("tmppixel---" + -1 + "---pixel---" + iArr[i6 + i2]);
                                pixel = -1;
                            }
                        }
                    }
                    iArr[(i * width) + i2] = pixel;
                }
            }
            return Bitmap.createBitmap(iArr, width, height, Bitmap.Config.RGB_565);
        } catch (Exception e) {
            LogUtils.e(e);
            return bitmap2;
        }
    }

    public static Bitmap convertGreyImgByFloyd(Context context, Bitmap bitmap) {
        return convertGreyImgByFloyd(context, bitmap, SPUtils.getInstance().getFloat(Code.highThreshold, 0.2f), SPUtils.getInstance().getFloat(Code.lowThreshold, 0.2f), SPUtils.getInstance().getFloat(Code.thresholdScale, 0.46f), SPUtils.getInstance().getFloat(Code.imageSharpness, 1.1f));
    }

    /* JADX WARNING: Removed duplicated region for block: B:41:0x0122  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0125  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x0128  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x012a  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x0132  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0142  */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x0165 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.graphics.Bitmap convertGreyImgByFloyd(android.content.Context r18, android.graphics.Bitmap r19, float r20, float r21, float r22, float r23) {
        /*
            r0 = r20
            r1 = r21
            r2 = r22
            r3 = r23
            if (r19 != 0) goto L_0x000c
            r0 = 0
            return r0
        L_0x000c:
            android.graphics.Bitmap r4 = com.blankj.utilcode.util.ImageUtils.toGray(r19)
            jp.co.cyberagent.android.gpuimage.filter.GPUImageSharpenFilter r5 = new jp.co.cyberagent.android.gpuimage.filter.GPUImageSharpenFilter
            r5.<init>(r3)
            jp.co.cyberagent.android.gpuimage.GPUImage r6 = new jp.co.cyberagent.android.gpuimage.GPUImage
            r7 = r18
            r6.<init>(r7)
            r6.setImage((android.graphics.Bitmap) r4)
            r6.setFilter(r5)
            android.graphics.Bitmap r7 = r6.getBitmapWithFilterApplied()
            int r10 = r7.getWidth()
            int r14 = r7.getHeight()
            int r4 = r10 * r14
            int[] r8 = new int[r4]
            r11 = 0
            r12 = 0
            r9 = 0
            r13 = r10
            r7.getPixels(r8, r9, r10, r11, r12, r13, r14)
            int[] r5 = new int[r4]
            r6 = 256(0x100, float:3.59E-43)
            int[] r7 = new int[r6]
            float r4 = (float) r4
            float r9 = r4 * r1
            int r9 = (int) r9
            float r4 = r4 * r0
            int r4 = (int) r4
            r12 = 0
        L_0x0046:
            if (r12 >= r14) goto L_0x0062
            r13 = 0
        L_0x0049:
            if (r13 >= r10) goto L_0x005f
            int r15 = r10 * r12
            int r15 = r15 + r13
            r16 = r8[r15]
            int r16 = android.graphics.Color.red(r16)
            r5[r15] = r16
            r15 = r7[r16]
            int r15 = r15 + 1
            r7[r16] = r15
            int r13 = r13 + 1
            goto L_0x0049
        L_0x005f:
            int r12 = r12 + 1
            goto L_0x0046
        L_0x0062:
            r15 = 0
            r16 = 0
        L_0x0065:
            if (r15 >= r6) goto L_0x0077
            r6 = r7[r15]
            long r11 = (long) r6
            long r16 = r16 + r11
            long r11 = (long) r9
            int r6 = (r16 > r11 ? 1 : (r16 == r11 ? 0 : -1))
            if (r6 <= 0) goto L_0x0072
            goto L_0x0078
        L_0x0072:
            int r15 = r15 + 1
            r6 = 256(0x100, float:3.59E-43)
            goto L_0x0065
        L_0x0077:
            r15 = 0
        L_0x0078:
            r6 = 255(0xff, float:3.57E-43)
            r12 = 0
        L_0x007c:
            if (r6 <= 0) goto L_0x0091
            r9 = r7[r6]
            r11 = r5
            r18 = r6
            long r5 = (long) r9
            long r12 = r12 + r5
            long r5 = (long) r4
            int r5 = (r12 > r5 ? 1 : (r12 == r5 ? 0 : -1))
            if (r5 <= 0) goto L_0x008d
            r6 = r18
            goto L_0x0093
        L_0x008d:
            int r6 = r18 + -1
            r5 = r11
            goto L_0x007c
        L_0x0091:
            r11 = r5
            r6 = 0
        L_0x0093:
            com.blankj.utilcode.util.SPUtils r4 = com.blankj.utilcode.util.SPUtils.getInstance()
            java.lang.String r5 = com.lib.Utils.Code.imageLowValue
            r7 = 70
            int r4 = r4.getInt(r5, r7)
            com.blankj.utilcode.util.SPUtils r5 = com.blankj.utilcode.util.SPUtils.getInstance()
            java.lang.String r7 = com.lib.Utils.Code.imageHighValue
            r9 = 185(0xb9, float:2.59E-43)
            int r5 = r5.getInt(r7, r9)
            if (r15 <= r4) goto L_0x00ae
            r15 = r4
        L_0x00ae:
            if (r6 >= r5) goto L_0x00b1
            r6 = r5
        L_0x00b1:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r9 = "highThreshold---"
            r7.append(r9)
            r7.append(r0)
            java.lang.String r9 = "---lowThreshold---"
            r7.append(r9)
            r7.append(r1)
            java.lang.String r9 = "---scale---"
            r7.append(r9)
            r7.append(r2)
            java.lang.String r9 = "---sharpness---"
            r7.append(r9)
            r7.append(r3)
            java.lang.String r3 = "---lowValue---"
            r7.append(r3)
            r7.append(r4)
            java.lang.String r3 = "---highValue---"
            r7.append(r3)
            r7.append(r5)
            java.lang.String r3 = r7.toString()
            java.lang.Object[] r3 = new java.lang.Object[]{r3}
            com.blankj.utilcode.util.LogUtils.e(r3)
            r3 = 0
        L_0x00f2:
            if (r3 >= r14) goto L_0x016b
            r4 = 0
        L_0x00f5:
            if (r4 >= r10) goto L_0x0168
            int r5 = r10 * r3
            int r5 = r5 + r4
            r7 = r11[r5]
            if (r7 > r15) goto L_0x0102
            float r7 = (float) r7
            float r7 = r7 * r2
        L_0x0100:
            int r7 = (int) r7
            goto L_0x011c
        L_0x0102:
            r9 = 1065353216(0x3f800000, float:1.0)
            if (r7 < r6) goto L_0x010f
            float r12 = (float) r7
            int r7 = 255 - r7
            float r7 = (float) r7
            float r9 = r9 - r2
            float r7 = r7 * r9
            float r12 = r12 + r7
            int r7 = (int) r12
            goto L_0x011c
        L_0x010f:
            float r7 = (float) r7
            float r12 = (float) r15
            float r12 = r12 * r2
            float r7 = r7 - r12
            float r12 = r1 * r2
            float r13 = r9 - r2
            float r13 = r13 * r0
            float r12 = r12 + r13
            float r9 = r9 - r12
            float r7 = r7 / r9
            goto L_0x0100
        L_0x011c:
            r11[r5] = r7
            r9 = 128(0x80, float:1.794E-43)
            if (r7 < r9) goto L_0x0125
            int r12 = r7 + -255
            goto L_0x0126
        L_0x0125:
            r12 = r7
        L_0x0126:
            if (r7 < r9) goto L_0x012a
            r7 = -1
            goto L_0x012c
        L_0x012a:
            r7 = -16777216(0xffffffffff000000, float:-1.7014118E38)
        L_0x012c:
            r8[r5] = r7
            int r7 = r10 + -1
            if (r4 >= r7) goto L_0x013e
            int r9 = r5 + 1
            r13 = r11[r9]
            int r16 = r12 * 7
            int r16 = r16 / 16
            int r13 = r13 + r16
            r11[r9] = r13
        L_0x013e:
            int r9 = r14 + -1
            if (r3 >= r9) goto L_0x0165
            int r5 = r5 + r10
            r9 = r11[r5]
            int r13 = r12 * 5
            int r13 = r13 / 16
            int r9 = r9 + r13
            r11[r5] = r9
            if (r4 <= 0) goto L_0x015a
            int r9 = r5 + -1
            r13 = r11[r9]
            int r16 = r12 * 3
            int r16 = r16 / 16
            int r13 = r13 + r16
            r11[r9] = r13
        L_0x015a:
            if (r4 >= r7) goto L_0x0165
            int r5 = r5 + 1
            r7 = r11[r5]
            int r12 = r12 / 16
            int r7 = r7 + r12
            r11[r5] = r7
        L_0x0165:
            int r4 = r4 + 1
            goto L_0x00f5
        L_0x0168:
            int r3 = r3 + 1
            goto L_0x00f2
        L_0x016b:
            android.graphics.Bitmap$Config r0 = android.graphics.Bitmap.Config.RGB_565
            android.graphics.Bitmap r0 = android.graphics.Bitmap.createBitmap(r10, r14, r0)
            r12 = 0
            r13 = 0
            r11 = r10
            r10 = 0
            r15 = r14
            r14 = r11
            r9 = r8
            r8 = r0
            r8.setPixels(r9, r10, r11, r12, r13, r14, r15)
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.lib.Utils.ImageDisposeUtil.convertGreyImgByFloyd(android.content.Context, android.graphics.Bitmap, float, float, float, float):android.graphics.Bitmap");
    }

    public static Bitmap convertGreyImgByFloyds(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        Bitmap gray = ImageUtils.toGray(bitmap);
        int width = gray.getWidth();
        int height = gray.getHeight();
        int i = width * height;
        int[] iArr = new int[i];
        gray.getPixels(iArr, 0, width, 0, 0, width, height);
        int[] iArr2 = new int[i];
        for (int i2 = 0; i2 < height; i2++) {
            for (int i3 = 0; i3 < width; i3++) {
                int i4 = (width * i2) + i3;
                iArr2[i4] = Color.red(iArr[i4]);
            }
        }
        int i5 = 0;
        while (i5 < height) {
            boolean z = i5 < height + -1;
            int i6 = 0;
            while (i6 < width) {
                boolean z2 = i6 > 0;
                boolean z3 = i6 < width + -1;
                int i7 = width * i5;
                int i8 = i7 + i6;
                int i9 = iArr2[i8];
                if (i9 >= 128) {
                    iArr[i8] = -1;
                    i9 -= 255;
                } else {
                    iArr[i8] = -16777216;
                }
                if (z3) {
                    int i10 = i6 + 1 + i7;
                    iArr2[i10] = iArr2[i10] + ((i9 * 7) / 16);
                }
                if (z2 && z) {
                    int i11 = (i6 - 1) + ((i5 + 1) * width);
                    iArr2[i11] = iArr2[i11] + ((i9 * 3) / 16);
                }
                if (z) {
                    int i12 = ((i5 + 1) * width) + i6;
                    iArr2[i12] = iArr2[i12] + ((i9 * 5) / 16);
                }
                if (z3 && z) {
                    int i13 = i6 + 1 + ((i5 + 1) * width);
                    iArr2[i13] = iArr2[i13] + (i9 / 16);
                }
                i6++;
            }
            i5++;
        }
        int i14 = width;
        int[] iArr3 = iArr;
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        createBitmap.setPixels(iArr3, 0, i14, 0, 0, i14, height);
        return createBitmap;
    }

    public static Bitmap toGrayScale(Bitmap bitmap) {
        int i;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] iArr = new int[(width * height)];
        for (int i2 = 0; i2 < height; i2++) {
            for (int i3 = 0; i3 < width; i3++) {
                int pixel = (int) ((((double) ((float) ((bitmap.getPixel(i3, i2) & 16711680) >> 16))) * 0.3d) + (((double) ((float) ((bitmap.getPixel(i3, i2) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8))) * 0.59d) + (((double) ((float) (bitmap.getPixel(i3, i2) & 255))) * 0.11d));
                if (pixel > 0 && pixel < 130) {
                    i = (pixel * 85) / 130;
                } else if (pixel <= 130 || pixel >= 150) {
                    i = (((pixel - 150) * 105) / 105) + 150;
                } else {
                    i = ((65 * (pixel - 130)) / 20) + 85;
                }
                iArr[(width * i2) + i3] = i;
            }
        }
        return convertGreyImgByFloyd(iArr, width, height);
    }

    public static Bitmap convertGreyImgByFloyd(int[] iArr, int i, int i2) {
        int[] iArr2 = new int[(i * i2)];
        int i3 = 0;
        while (i3 < i2) {
            boolean z = i3 < i2 + -1;
            int i4 = 0;
            while (i4 < i) {
                boolean z2 = i4 > 0;
                boolean z3 = i4 < i + -1;
                int i5 = i * i3;
                int i6 = i5 + i4;
                int i7 = iArr[i6];
                if (i7 >= 128) {
                    iArr2[i6] = -1;
                    i7 -= 255;
                } else {
                    iArr2[i6] = -16777216;
                }
                if (z3) {
                    int i8 = i4 + 1 + i5;
                    iArr[i8] = iArr[i8] + ((i7 * 7) / 16);
                }
                if (z2 && z) {
                    int i9 = (i4 - 1) + ((i3 + 1) * i);
                    iArr[i9] = iArr[i9] + ((i7 * 3) / 16);
                }
                if (z) {
                    int i10 = ((i3 + 1) * i) + i4;
                    iArr[i10] = iArr[i10] + ((i7 * 5) / 16);
                }
                if (z3 && z) {
                    int i11 = i4 + 1 + ((i3 + 1) * i);
                    iArr[i11] = iArr[i11] + (i7 / 16);
                }
                i4++;
            }
            i3++;
        }
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.RGB_565);
        createBitmap.setPixels(iArr2, 0, i, 0, 0, i, i2);
        return createBitmap;
    }

    public static Bitmap ImageEnhancement(Bitmap bitmap) {
        Mat mat = new Mat();
        Mat mat2 = new Mat();
        Utils.bitmapToMat(bitmap, mat);
        Imgproc.cvtColor(mat, mat, 7);
        CLAHE createCLAHE = Imgproc.createCLAHE();
        createCLAHE.setClipLimit(4.0d);
        createCLAHE.setTilesGridSize(new Size(8.0d, 8.0d));
        createCLAHE.apply(mat, mat2);
        Bitmap createBitmap = Bitmap.createBitmap(mat2.cols(), mat2.rows(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(mat2, createBitmap);
        return createBitmap;
    }

    public static int getImageRotationAngle(String str) {
        try {
            int attributeInt = new ExifInterface(str).getAttributeInt("Orientation", 0);
            if (attributeInt == 3) {
                return 180;
            }
            if (attributeInt == 6) {
                return 90;
            }
            if (attributeInt != 8) {
                return 0;
            }
            return SubsamplingScaleImageView.ORIENTATION_270;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static Bitmap view2Bitmap1(View view) {
        if (view == null) {
            return null;
        }
        try {
            if (view.getWidth() != 0) {
                if (view.getHeight() != 0) {
                    Bitmap createBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(createBitmap);
                    Drawable background = view.getBackground();
                    if (background != null) {
                        background.draw(canvas);
                    } else {
                        canvas.drawColor(-1);
                    }
                    view.draw(canvas);
                    return createBitmap;
                }
            }
            return null;
        } catch (Exception e) {
            LogUtils.e(e);
            return null;
        }
    }

    public static String encodeImage(Bitmap bitmap) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 40, byteArrayOutputStream);
            return Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
        } catch (Exception e) {
            LogUtils.e(e);
            return "";
        }
    }

    public static String encodeImage(String str, int i) {
        return encodeImage(ImageUtils.getBitmap(str), i);
    }

    public static String encodeImage(Bitmap bitmap, int i) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, i, byteArrayOutputStream);
            return Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
        } catch (Exception e) {
            LogUtils.e(e);
            return "";
        }
    }

    public static Bitmap base64ToBitmap(String str) {
        try {
            byte[] decode = Base64.decode(str, 0);
            return BitmapFactory.decodeByteArray(decode, 0, decode.length);
        } catch (Exception e) {
            LogUtils.e(e);
            return null;
        }
    }

    public static ArrayList<Bitmap> corpLandImg(Bitmap bitmap) {
        boolean z;
        ArrayList<Bitmap> arrayList = new ArrayList<>();
        try {
            int i = PrinterModelUtils.getoneLength() * 100;
            Bitmap fitBitmap = fitBitmap(bitmap, i);
            int printSize = PrinterModelUtils.getPrintSize();
            LogUtils.e("landScape----" + i + "h----" + printSize);
            int width = fitBitmap.getWidth() + -15;
            while (true) {
                if (width >= fitBitmap.getWidth()) {
                    break;
                }
                for (int i2 = 0; i2 < fitBitmap.getHeight(); i2++) {
                    fitBitmap.setPixel(width, i2, -1);
                }
                width++;
            }
            boolean z2 = true;
            int i3 = printSize;
            int i4 = 0;
            while (z2) {
                if (i4 < fitBitmap.getHeight() - i3) {
                    int i5 = 0;
                    while (true) {
                        if (i5 >= fitBitmap.getWidth()) {
                            z = true;
                            break;
                        } else if (fitBitmap.getPixel(i5, i3 + i4) != -1) {
                            i3--;
                            if (i3 < printSize / 2) {
                                z = true;
                                i3 = printSize;
                            } else {
                                z = false;
                            }
                        } else {
                            i5++;
                        }
                    }
                    if (z) {
                        Bitmap clip = clip(fitBitmap, 0, i4, fitBitmap.getWidth(), i3);
                        boolean z3 = false;
                        for (int i6 = 0; i6 < clip.getWidth(); i6++) {
                            int i7 = 0;
                            while (true) {
                                if (i7 >= clip.getHeight()) {
                                    break;
                                } else if (clip.getPixel(i6, i7) != -1) {
                                    z3 = true;
                                    break;
                                } else {
                                    i7++;
                                }
                            }
                        }
                        if (z3) {
                            Bitmap rotate = rotate(clip, 90, (float) (clip.getWidth() / 2), (float) (clip.getHeight() / 2));
                            if (rotate.getWidth() != PrinterModelUtils.getPrintSize()) {
                                rotate = mergeBitmap(rotate);
                            }
                            arrayList.add(rotate);
                        }
                        i4 += i3;
                        i3 = printSize;
                    }
                } else {
                    Bitmap clip2 = clip(fitBitmap, 0, i4, fitBitmap.getWidth(), fitBitmap.getHeight() - i4);
                    boolean z4 = false;
                    for (int i8 = 0; i8 < clip2.getWidth(); i8++) {
                        int i9 = 0;
                        while (true) {
                            if (i9 >= clip2.getHeight()) {
                                break;
                            } else if (clip2.getPixel(i8, i9) != -1) {
                                z4 = true;
                                break;
                            } else {
                                i9++;
                            }
                        }
                    }
                    if (z4) {
                        Bitmap rotate2 = rotate(clip2, 90, (float) (clip2.getWidth() / 2), (float) (clip2.getHeight() / 2));
                        if (rotate2.getWidth() != PrinterModelUtils.getPrintSize()) {
                            rotate2 = mergeBitmap(rotate2);
                        }
                        arrayList.add(rotate2);
                    }
                    i4 += i3;
                    z2 = false;
                }
            }
        } catch (Exception unused) {
        }
        return arrayList;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0043, code lost:
        r8 = r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.ArrayList<android.graphics.Bitmap> cropBitmap(android.graphics.Bitmap r13) {
        /*
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            int r1 = com.lib.blueUtils.PrinterModelUtils.getoneLength()     // Catch:{ Exception -> 0x0071 }
            int r1 = r1 * 100
            android.graphics.Bitmap r13 = fitBitmap(r13, r1)     // Catch:{ Exception -> 0x0071 }
            int r1 = com.lib.blueUtils.PrinterModelUtils.getPrintSize()     // Catch:{ Exception -> 0x0071 }
            int r2 = r13.getWidth()     // Catch:{ Exception -> 0x0071 }
            int r3 = r13.getHeight()     // Catch:{ Exception -> 0x0071 }
            double r4 = (double) r3     // Catch:{ Exception -> 0x0071 }
            double r6 = (double) r1     // Catch:{ Exception -> 0x0071 }
            double r4 = r4 / r6
            double r4 = java.lang.Math.ceil(r4)     // Catch:{ Exception -> 0x0071 }
            int r4 = (int) r4     // Catch:{ Exception -> 0x0071 }
            int r4 = r4 + 1
            r5 = 0
            r6 = r5
            r7 = r6
        L_0x0028:
            if (r6 >= r4) goto L_0x0071
            int r8 = r7 + r1
            int r8 = r8 + -1
        L_0x002e:
            int r9 = r1 / 2
            int r9 = r9 + r7
            if (r8 < r9) goto L_0x0043
            if (r8 < r3) goto L_0x0036
            goto L_0x0043
        L_0x0036:
            boolean r9 = isRowBlank(r13, r8, r2)     // Catch:{ Exception -> 0x0071 }
            if (r9 != 0) goto L_0x0040
            int r8 = r8 - r7
            int r8 = r8 + 1
            goto L_0x0044
        L_0x0040:
            int r8 = r8 + -1
            goto L_0x002e
        L_0x0043:
            r8 = r1
        L_0x0044:
            android.graphics.Bitmap r9 = android.graphics.Bitmap.createBitmap(r13, r5, r7, r2, r8)     // Catch:{ Exception -> 0x0071 }
            int r10 = r9.getWidth()     // Catch:{ Exception -> 0x0071 }
            int r10 = r10 / 2
            float r10 = (float) r10     // Catch:{ Exception -> 0x0071 }
            int r11 = r9.getHeight()     // Catch:{ Exception -> 0x0071 }
            int r11 = r11 / 2
            float r11 = (float) r11     // Catch:{ Exception -> 0x0071 }
            r12 = 90
            android.graphics.Bitmap r9 = rotate(r9, r12, r10, r11)     // Catch:{ Exception -> 0x0071 }
            int r10 = r9.getWidth()     // Catch:{ Exception -> 0x0071 }
            int r11 = com.lib.blueUtils.PrinterModelUtils.getPrintSize()     // Catch:{ Exception -> 0x0071 }
            if (r10 == r11) goto L_0x006a
            android.graphics.Bitmap r9 = mergeBitmap(r9)     // Catch:{ Exception -> 0x0071 }
        L_0x006a:
            r0.add(r9)     // Catch:{ Exception -> 0x0071 }
            int r7 = r7 + r8
            int r6 = r6 + 1
            goto L_0x0028
        L_0x0071:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.lib.Utils.ImageDisposeUtil.cropBitmap(android.graphics.Bitmap):java.util.ArrayList");
    }

    private static boolean isRowBlank(Bitmap bitmap, int i, int i2) {
        for (int i3 = 0; i3 < i2; i3++) {
            if (bitmap.getPixel(i3, i) != -1) {
                return false;
            }
        }
        return true;
    }

    public static ArrayList<Bitmap> corpImg(Bitmap bitmap, int i) {
        return corpImgs(fitBitmap1(bitmap, PrinterModelUtils.getPrintSize()), i);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0034, code lost:
        r6 = true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.ArrayList<android.graphics.Bitmap> corpImgs(android.graphics.Bitmap r12, int r13) {
        /*
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            int r1 = r12.getHeight()     // Catch:{ Exception -> 0x00b9 }
            if (r1 >= r13) goto L_0x000f
            r0.add(r12)     // Catch:{ Exception -> 0x00b9 }
            return r0
        L_0x000f:
            r1 = 1
            r2 = 0
            r5 = r13
            r3 = r1
            r4 = r2
        L_0x0014:
            if (r3 == 0) goto L_0x00b9
            int r6 = r12.getHeight()     // Catch:{ Exception -> 0x00b9 }
            int r6 = r6 - r5
            r7 = -1
            if (r4 >= r6) goto L_0x0078
            r6 = r2
        L_0x001f:
            int r8 = r12.getWidth()     // Catch:{ Exception -> 0x00b9 }
            if (r6 >= r8) goto L_0x0034
            int r8 = r5 + r4
            int r8 = r12.getPixel(r6, r8)     // Catch:{ Exception -> 0x00b9 }
            if (r8 == r7) goto L_0x0038
            int r5 = r5 + -1
            int r6 = r13 / 2
            if (r5 >= r6) goto L_0x0036
            r5 = r13
        L_0x0034:
            r6 = r1
            goto L_0x003b
        L_0x0036:
            r6 = r2
            goto L_0x003b
        L_0x0038:
            int r6 = r6 + 1
            goto L_0x001f
        L_0x003b:
            if (r6 == 0) goto L_0x0014
            int r6 = r12.getWidth()     // Catch:{ Exception -> 0x00b9 }
            android.graphics.Bitmap r6 = clip(r12, r2, r4, r6, r5)     // Catch:{ Exception -> 0x00b9 }
            r8 = r2
            r9 = r8
        L_0x0047:
            int r10 = r6.getWidth()     // Catch:{ Exception -> 0x00b9 }
            if (r8 >= r10) goto L_0x0062
            r10 = r2
        L_0x004e:
            int r11 = r6.getHeight()     // Catch:{ Exception -> 0x00b9 }
            if (r10 >= r11) goto L_0x005f
            int r11 = r6.getPixel(r8, r10)     // Catch:{ Exception -> 0x00b9 }
            if (r11 == r7) goto L_0x005c
            r9 = r1
            goto L_0x005f
        L_0x005c:
            int r10 = r10 + 1
            goto L_0x004e
        L_0x005f:
            int r8 = r8 + 1
            goto L_0x0047
        L_0x0062:
            if (r9 == 0) goto L_0x0075
            int r7 = r6.getWidth()     // Catch:{ Exception -> 0x00b9 }
            int r8 = com.lib.blueUtils.PrinterModelUtils.getPrintSize()     // Catch:{ Exception -> 0x00b9 }
            if (r7 == r8) goto L_0x0072
            android.graphics.Bitmap r6 = mergeBitmap(r6)     // Catch:{ Exception -> 0x00b9 }
        L_0x0072:
            r0.add(r6)     // Catch:{ Exception -> 0x00b9 }
        L_0x0075:
            int r4 = r4 + r5
            r5 = r13
            goto L_0x0014
        L_0x0078:
            int r3 = r12.getWidth()     // Catch:{ Exception -> 0x00b9 }
            int r6 = r12.getHeight()     // Catch:{ Exception -> 0x00b9 }
            int r6 = r6 - r4
            android.graphics.Bitmap r3 = clip(r12, r2, r4, r3, r6)     // Catch:{ Exception -> 0x00b9 }
            r6 = r2
            r8 = r6
        L_0x0087:
            int r9 = r3.getWidth()     // Catch:{ Exception -> 0x00b9 }
            if (r6 >= r9) goto L_0x00a2
            r9 = r2
        L_0x008e:
            int r10 = r3.getHeight()     // Catch:{ Exception -> 0x00b9 }
            if (r9 >= r10) goto L_0x009f
            int r10 = r3.getPixel(r6, r9)     // Catch:{ Exception -> 0x00b9 }
            if (r10 == r7) goto L_0x009c
            r8 = r1
            goto L_0x009f
        L_0x009c:
            int r9 = r9 + 1
            goto L_0x008e
        L_0x009f:
            int r6 = r6 + 1
            goto L_0x0087
        L_0x00a2:
            if (r8 == 0) goto L_0x00b5
            int r6 = r3.getWidth()     // Catch:{ Exception -> 0x00b9 }
            int r7 = com.lib.blueUtils.PrinterModelUtils.getPrintSize()     // Catch:{ Exception -> 0x00b9 }
            if (r6 == r7) goto L_0x00b2
            android.graphics.Bitmap r3 = mergeBitmap(r3)     // Catch:{ Exception -> 0x00b9 }
        L_0x00b2:
            r0.add(r3)     // Catch:{ Exception -> 0x00b9 }
        L_0x00b5:
            int r4 = r4 + r5
            r3 = r2
            goto L_0x0014
        L_0x00b9:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.lib.Utils.ImageDisposeUtil.corpImgs(android.graphics.Bitmap, int):java.util.ArrayList");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0038, code lost:
        r6 = true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.ArrayList<android.graphics.Bitmap> corpHtmlImg(android.graphics.Bitmap r12, int r13) {
        /*
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            if (r12 != 0) goto L_0x0009
            goto L_0x00b1
        L_0x0009:
            int r1 = r12.getHeight()
            if (r1 >= r13) goto L_0x0013
            r0.add(r12)
            return r0
        L_0x0013:
            r1 = 1
            r2 = 0
            r5 = r13
            r3 = r1
            r4 = r2
        L_0x0018:
            if (r3 == 0) goto L_0x00b1
            int r6 = r12.getHeight()     // Catch:{ Exception -> 0x003c }
            int r6 = r6 - r5
            r7 = -1
            if (r4 >= r6) goto L_0x0071
            r6 = r2
        L_0x0023:
            int r8 = r12.getWidth()     // Catch:{ Exception -> 0x003c }
            if (r6 >= r8) goto L_0x0038
            int r8 = r5 + r4
            int r8 = r12.getPixel(r6, r8)     // Catch:{ Exception -> 0x003c }
            if (r8 == r7) goto L_0x003f
            int r5 = r5 + -1
            int r6 = r13 / 2
            if (r5 >= r6) goto L_0x003a
            r5 = r13
        L_0x0038:
            r6 = r1
            goto L_0x0042
        L_0x003a:
            r6 = r2
            goto L_0x0042
        L_0x003c:
            r12 = move-exception
            goto L_0x00a4
        L_0x003f:
            int r6 = r6 + 1
            goto L_0x0023
        L_0x0042:
            if (r6 == 0) goto L_0x0018
            int r6 = r12.getWidth()     // Catch:{ Exception -> 0x003c }
            android.graphics.Bitmap r6 = clip(r12, r2, r4, r6, r5)     // Catch:{ Exception -> 0x003c }
            r8 = r2
            r9 = r8
        L_0x004e:
            int r10 = r6.getWidth()     // Catch:{ Exception -> 0x003c }
            if (r8 >= r10) goto L_0x0069
            r10 = r2
        L_0x0055:
            int r11 = r6.getHeight()     // Catch:{ Exception -> 0x003c }
            if (r10 >= r11) goto L_0x0066
            int r11 = r6.getPixel(r8, r10)     // Catch:{ Exception -> 0x003c }
            if (r11 == r7) goto L_0x0063
            r9 = r1
            goto L_0x0066
        L_0x0063:
            int r10 = r10 + 1
            goto L_0x0055
        L_0x0066:
            int r8 = r8 + 1
            goto L_0x004e
        L_0x0069:
            if (r9 == 0) goto L_0x006e
            r0.add(r6)     // Catch:{ Exception -> 0x003c }
        L_0x006e:
            int r4 = r4 + r5
            r5 = r13
            goto L_0x0018
        L_0x0071:
            int r3 = r12.getWidth()     // Catch:{ Exception -> 0x003c }
            int r6 = r12.getHeight()     // Catch:{ Exception -> 0x003c }
            int r6 = r6 - r4
            android.graphics.Bitmap r3 = clip(r12, r2, r4, r3, r6)     // Catch:{ Exception -> 0x003c }
            r6 = r2
            r8 = r6
        L_0x0080:
            int r9 = r3.getWidth()     // Catch:{ Exception -> 0x003c }
            if (r6 >= r9) goto L_0x009b
            r9 = r2
        L_0x0087:
            int r10 = r3.getHeight()     // Catch:{ Exception -> 0x003c }
            if (r9 >= r10) goto L_0x0098
            int r10 = r3.getPixel(r6, r9)     // Catch:{ Exception -> 0x003c }
            if (r10 == r7) goto L_0x0095
            r8 = r1
            goto L_0x0098
        L_0x0095:
            int r9 = r9 + 1
            goto L_0x0087
        L_0x0098:
            int r6 = r6 + 1
            goto L_0x0080
        L_0x009b:
            if (r8 == 0) goto L_0x00a0
            r0.add(r3)     // Catch:{ Exception -> 0x003c }
        L_0x00a0:
            int r4 = r4 + r5
            r3 = r2
            goto L_0x0018
        L_0x00a4:
            java.lang.String r13 = "corpHtmlImg"
            java.lang.String r12 = r12.getMessage()
            java.lang.Object[] r12 = new java.lang.Object[]{r13, r12}
            com.blankj.utilcode.util.LogUtils.e(r12)
        L_0x00b1:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.lib.Utils.ImageDisposeUtil.corpHtmlImg(android.graphics.Bitmap, int):java.util.ArrayList");
    }

    public static ArrayList<Bitmap> corpImg(Bitmap bitmap) {
        boolean z;
        ArrayList<Bitmap> arrayList = new ArrayList<>();
        try {
            Bitmap fitBitmap = fitBitmap(bitmap, PrinterModelUtils.getPrintSize(), true);
            int i = 0;
            boolean z2 = true;
            loop0:
            while (true) {
                int i2 = 2261;
                while (z2) {
                    if (i < fitBitmap.getHeight() - i2) {
                        int i3 = 0;
                        while (true) {
                            if (i3 >= fitBitmap.getWidth()) {
                                z = true;
                                break;
                            } else if (fitBitmap.getPixel(i3, i2 + i) != -1) {
                                i2--;
                                if (i2 < 1130) {
                                    z = true;
                                    i2 = 2261;
                                } else {
                                    z = false;
                                }
                            } else {
                                i3++;
                            }
                        }
                        if (z) {
                            Bitmap clip = ImageUtils.clip(fitBitmap, 0, i, fitBitmap.getWidth(), i2);
                            boolean z3 = false;
                            for (int i4 = 0; i4 < clip.getWidth(); i4++) {
                                int i5 = 0;
                                while (true) {
                                    if (i5 >= clip.getHeight()) {
                                        break;
                                    } else if (clip.getPixel(i4, i5) != -1) {
                                        z3 = true;
                                        break;
                                    } else {
                                        i5++;
                                    }
                                }
                            }
                            if (z3) {
                                if (clip.getWidth() != PrinterModelUtils.getPrintSize()) {
                                    clip = mergeBitmap(clip);
                                }
                                arrayList.add(clip);
                            }
                            i += i2;
                        }
                    } else {
                        Bitmap clip2 = ImageUtils.clip(fitBitmap, 0, i, fitBitmap.getWidth(), fitBitmap.getHeight() - i);
                        boolean z4 = false;
                        for (int i6 = 0; i6 < clip2.getWidth(); i6++) {
                            int i7 = 0;
                            while (true) {
                                if (i7 >= clip2.getHeight()) {
                                    break;
                                } else if (clip2.getPixel(i6, i7) != -1) {
                                    z4 = true;
                                    break;
                                } else {
                                    i7++;
                                }
                            }
                        }
                        if (z4) {
                            if (clip2.getWidth() != PrinterModelUtils.getPrintSize()) {
                                clip2 = mergeBitmap(clip2);
                            }
                            arrayList.add(clip2);
                        }
                        i += i2;
                        z2 = false;
                    }
                }
                break loop0;
            }
            if (fitBitmap == null && !fitBitmap.isRecycled()) {
                fitBitmap.recycle();
            }
        } catch (Exception unused) {
        }
        return arrayList;
    }

    public static Bitmap mergeBitmap(Bitmap bitmap) {
        try {
            int printImgSize = PrinterModelUtils.getPrintImgSize();
            Bitmap createBitmap = Bitmap.createBitmap(printImgSize, bitmap.getHeight(), Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(createBitmap);
            Paint paint = new Paint();
            canvas.drawColor(-1);
            canvas.drawBitmap(createBitmap, 0.0f, 0.0f, paint);
            return ImageUtils.addImageWatermark(createBitmap, bitmap, (printImgSize - bitmap.getWidth()) / 2, 0, 255);
        } catch (Exception e) {
            LogUtils.e(e);
            return bitmap;
        }
    }

    public static Bitmap mergeBitmapZero(Bitmap bitmap) {
        try {
            Bitmap createBitmap = Bitmap.createBitmap(PrinterModelUtils.getPrintImgSize(), bitmap.getHeight(), Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(createBitmap);
            Paint paint = new Paint();
            canvas.drawColor(-1);
            canvas.drawBitmap(createBitmap, 0.0f, 0.0f, paint);
            return ImageUtils.addImageWatermark(createBitmap, bitmap, 0, 0, 255);
        } catch (Exception e) {
            LogUtils.e(e);
            return bitmap;
        }
    }

    public static Bitmap mergeBitmapCenter(Bitmap bitmap, int i) {
        try {
            int printImgSize = PrinterModelUtils.getPrintImgSize();
            if (i < bitmap.getHeight()) {
                i = bitmap.getHeight();
            }
            Bitmap createBitmap = Bitmap.createBitmap(printImgSize, i, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(createBitmap);
            Paint paint = new Paint();
            canvas.drawColor(-1);
            canvas.drawBitmap(createBitmap, 0.0f, 0.0f, paint);
            int width = (printImgSize - bitmap.getWidth()) / 2;
            int height = (i - bitmap.getHeight()) / 2;
            if (height < 0) {
                height = 0;
            }
            return ImageUtils.addImageWatermark(createBitmap, bitmap, width, height, 255);
        } catch (Exception e) {
            LogUtils.e(e);
            return bitmap;
        }
    }

    public static Bitmap mergeBitmapRight(Bitmap bitmap) {
        try {
            int printImgSize = PrinterModelUtils.getPrintImgSize();
            Bitmap createBitmap = Bitmap.createBitmap(printImgSize, bitmap.getHeight(), Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(createBitmap);
            Paint paint = new Paint();
            canvas.drawColor(-1);
            canvas.drawBitmap(createBitmap, 0.0f, 0.0f, paint);
            return ImageUtils.addImageWatermark(createBitmap, bitmap, printImgSize - bitmap.getWidth(), 0, 255);
        } catch (Exception e) {
            LogUtils.e(e);
            return bitmap;
        }
    }

    public static Bitmap mergeBitmaps(List<Bitmap> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        int i = 0;
        int height = list.get(0).getHeight();
        int i2 = 0;
        for (Bitmap width : list) {
            i2 += width.getWidth();
        }
        Bitmap createBitmap = Bitmap.createBitmap(i2, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(createBitmap);
        for (Bitmap next : list) {
            canvas.drawBitmap(next, (float) i, 0.0f, (Paint) null);
            i += next.getWidth();
        }
        return createBitmap;
    }

    public static Bitmap mergeBitmaps(List<Bitmap> list, MergeDirection mergeDirection) {
        if (list != null && !list.isEmpty()) {
            int i = 0;
            if (mergeDirection == MergeDirection.VERTICAL) {
                int width = list.get(0).getWidth();
                int i2 = 0;
                for (Bitmap next : list) {
                    i2 += next.getHeight();
                    width = Math.max(width, next.getWidth());
                }
                Bitmap createBitmap = Bitmap.createBitmap(width, i2, Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(createBitmap);
                for (Bitmap next2 : list) {
                    canvas.drawBitmap(next2, 0.0f, (float) i, (Paint) null);
                    i += next2.getHeight();
                }
                return createBitmap;
            } else if (mergeDirection == MergeDirection.HORIZONTAL) {
                int height = list.get(0).getHeight();
                int i3 = 0;
                for (Bitmap next3 : list) {
                    i3 += next3.getWidth();
                    height = Math.max(height, next3.getHeight());
                }
                Bitmap createBitmap2 = Bitmap.createBitmap(i3, height, Bitmap.Config.RGB_565);
                createBitmap2.eraseColor(-1);
                Canvas canvas2 = new Canvas(createBitmap2);
                for (Bitmap next4 : list) {
                    canvas2.drawBitmap(next4, (float) i, 0.0f, (Paint) null);
                    i += next4.getWidth();
                }
                return createBitmap2;
            }
        }
        return null;
    }

    public static Bitmap mergeBitmapTop(Bitmap bitmap, int i) {
        try {
            int printImgSize = PrinterModelUtils.getPrintImgSize();
            int height = bitmap.getHeight();
            int i2 = i * 8;
            Bitmap createBitmap = Bitmap.createBitmap(printImgSize, i2, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(createBitmap);
            Paint paint = new Paint();
            canvas.drawColor(-1);
            canvas.drawBitmap(createBitmap, 0.0f, 0.0f, paint);
            int width = (printImgSize - bitmap.getWidth()) / 2;
            int i3 = 0;
            int i4 = i2 > height ? (i2 - height) / 2 : 0;
            if ("YK06".equals(PrinterModelUtils.getPrinterModel().getModelNo())) {
                i3 = i4;
            }
            return ImageUtils.addImageWatermark(createBitmap, bitmap, width, i3, 255);
        } catch (Exception e) {
            LogUtils.e(e);
            return bitmap;
        }
    }

    public static Bitmap mergeBitmap(Bitmap bitmap, int i) {
        try {
            Bitmap createBitmap = Bitmap.createBitmap(i, bitmap.getHeight(), Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(createBitmap);
            Paint paint = new Paint();
            canvas.drawColor(-1);
            canvas.drawBitmap(createBitmap, 0.0f, 0.0f, paint);
            return ImageUtils.addImageWatermark(createBitmap, bitmap, (i - bitmap.getWidth()) / 2, 0, 255);
        } catch (Exception e) {
            LogUtils.e(e);
            return bitmap;
        }
    }

    public static Bitmap mergeBitmap(Bitmap bitmap, Bitmap bitmap2, int i) {
        try {
            int height = bitmap.getHeight();
            if (bitmap2 == null) {
                bitmap2 = Bitmap.createBitmap(i, height, Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(bitmap2);
                Paint paint = new Paint();
                canvas.drawColor(-1);
                canvas.drawBitmap(bitmap2, 0.0f, 0.0f, paint);
            }
            return ImageUtils.addImageWatermark(bitmap2, bitmap, (i - bitmap.getWidth()) / 2, 0, 255);
        } catch (Exception e) {
            LogUtils.e(e);
            return bitmap;
        }
    }

    public static Bitmap mergeBitmap(Bitmap bitmap, Bitmap bitmap2, int i, int i2, int i3, int i4) {
        if (bitmap2 == null) {
            try {
                bitmap2 = Bitmap.createBitmap(i, i2, Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(bitmap2);
                Paint paint = new Paint();
                canvas.drawColor(-1);
                canvas.drawBitmap(bitmap2, 0.0f, 0.0f, paint);
            } catch (Exception e) {
                LogUtils.e(e);
                return bitmap;
            }
        }
        return ImageUtils.addImageWatermark(bitmap2, bitmap, i3, i4, 255);
    }

    public static Bitmap mergeBitmapTop(Bitmap bitmap, int i, int i2) {
        try {
            Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(createBitmap);
            Paint paint = new Paint();
            canvas.drawColor(-1);
            canvas.drawBitmap(createBitmap, 0.0f, 0.0f, paint);
            bitmap.getHeight();
            return ImageUtils.addImageWatermark(createBitmap, bitmap, (i - bitmap.getWidth()) / 2, 0, 255);
        } catch (Exception e) {
            LogUtils.e(e);
            return bitmap;
        }
    }

    public static Bitmap mergeBitmap(Bitmap bitmap, int i, int i2) {
        try {
            Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(createBitmap);
            Paint paint = new Paint();
            canvas.drawColor(-1);
            canvas.drawBitmap(createBitmap, 0.0f, 0.0f, paint);
            return ImageUtils.addImageWatermark(createBitmap, bitmap, (i - bitmap.getWidth()) / 2, (i2 - bitmap.getHeight()) / 2, 255);
        } catch (Exception e) {
            LogUtils.e(e);
            return bitmap;
        }
    }

    public static Bitmap mergeBitmapRight(Bitmap bitmap, int i) {
        try {
            Bitmap createBitmap = Bitmap.createBitmap(i, bitmap.getHeight(), Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(createBitmap);
            Paint paint = new Paint();
            canvas.drawColor(-1);
            canvas.drawBitmap(createBitmap, 0.0f, 0.0f, paint);
            return ImageUtils.addImageWatermark(createBitmap, bitmap, i - bitmap.getWidth(), 0, 255);
        } catch (Exception e) {
            LogUtils.e(e);
            return bitmap;
        }
    }

    public static Bitmap mergeBitmapBottom(Bitmap bitmap, int i, int i2) {
        try {
            Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(createBitmap);
            Paint paint = new Paint();
            canvas.drawColor(-1);
            canvas.drawBitmap(createBitmap, 0.0f, 0.0f, paint);
            return ImageUtils.addImageWatermark(createBitmap, bitmap, (i - bitmap.getWidth()) / 2, i2 - bitmap.getHeight(), 255);
        } catch (Exception e) {
            LogUtils.e(e);
            return bitmap;
        }
    }

    public static Bitmap pngToWhite(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        try {
            Bitmap copy = bitmap.copy(Bitmap.Config.RGB_565, true);
            Canvas canvas = new Canvas(copy);
            canvas.drawColor(-1);
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
            return copy;
        } catch (Exception e) {
            LogUtils.e(e);
            return bitmap;
        }
    }

    public static Bitmap view2Bitmap(View view) {
        try {
            if (view.getWidth() <= 0 || view.getHeight() <= 0) {
                return null;
            }
            return ImageUtils.view2Bitmap(view);
        } catch (OutOfMemoryError unused) {
            System.gc();
            return null;
        }
    }

    public static Bitmap scale(Bitmap bitmap, int i, int i2) {
        try {
            return ImageUtils.scale(bitmap, i, i2);
        } catch (OutOfMemoryError unused) {
            System.gc();
            return null;
        } catch (IllegalArgumentException unused2) {
            return null;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, int i, float f, float f2) {
        if (bitmap == null) {
            return null;
        }
        try {
            return ImageUtils.rotate(bitmap, i, f, f2);
        } catch (OutOfMemoryError unused) {
            System.gc();
            return bitmap;
        }
    }

    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    public static Bitmap createBitmap(int i, int i2) {
        try {
            Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(createBitmap);
            Paint paint = new Paint();
            paint.setColor(ViewCompat.MEASURED_STATE_MASK);
            canvas.drawBitmap(createBitmap, 0.0f, 0.0f, paint);
            return createBitmap;
        } catch (Exception e) {
            LogUtils.e(e);
            return null;
        }
    }

    public static int[] convertGreyImgByFloydPixels(Context context, Bitmap bitmap) {
        Bitmap bitmap2 = bitmap;
        int[] convertGreyImgByFloydPixels = convertGreyImgByFloydPixels(context, bitmap2, SPUtils.getInstance().getFloat(Code.highThreshold, 0.2f), SPUtils.getInstance().getFloat(Code.lowThreshold, 0.2f), SPUtils.getInstance().getFloat(Code.thresholdScale, 0.46f), SPUtils.getInstance().getFloat(Code.imageSharpness, 1.1f), SPUtils.getInstance().getInt(Code.imageLowValue, 70), SPUtils.getInstance().getInt(Code.imageHighValue, 185), 1.0d);
        ditherPixels(convertGreyImgByFloydPixels, bitmap2.getWidth(), bitmap2.getHeight());
        return convertGreyImgByFloydPixels;
    }

    /* JADX WARNING: Removed duplicated region for block: B:44:0x013f A[Catch:{ Exception -> 0x014d }] */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x0142 A[Catch:{ Exception -> 0x014d }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int[] convertGreyImgByFloydPixels(android.content.Context r23, android.graphics.Bitmap r24, float r25, float r26, float r27, float r28, int r29, int r30, double r31) {
        /*
            r0 = r24
            r1 = r25
            r2 = r26
            r3 = r27
            r4 = r28
            r5 = r29
            r6 = r30
            r7 = r31
            if (r0 != 0) goto L_0x0014
            r0 = 0
            return r0
        L_0x0014:
            jp.co.cyberagent.android.gpuimage.filter.GPUImageSharpenFilter r9 = new jp.co.cyberagent.android.gpuimage.filter.GPUImageSharpenFilter
            r9.<init>(r4)
            jp.co.cyberagent.android.gpuimage.GPUImage r10 = new jp.co.cyberagent.android.gpuimage.GPUImage
            r11 = r23
            r10.<init>(r11)
            r10.setImage((android.graphics.Bitmap) r0)
            r10.setFilter(r9)
            android.graphics.Bitmap r0 = r10.getBitmapWithFilterApplied()
            android.graphics.Bitmap r9 = com.blankj.utilcode.util.ImageUtils.toGray(r0)
            int r12 = r9.getWidth()
            int r16 = r9.getHeight()
            int r0 = r12 * r16
            int[] r10 = new int[r0]
            r13 = 0
            r14 = 0
            r11 = 0
            r15 = r12
            r9.getPixels(r10, r11, r12, r13, r14, r15, r16)
            r9 = r16
            r11 = 256(0x100, float:3.59E-43)
            int[] r13 = new int[r11]
            r15 = 0
        L_0x0048:
            r14 = 255(0xff, float:3.57E-43)
            if (r15 >= r9) goto L_0x006c
            r11 = 0
        L_0x004d:
            if (r11 >= r12) goto L_0x0065
            int r16 = r12 * r15
            int r16 = r16 + r11
            r17 = r10
            r10 = r17[r16]
            r10 = r10 & r14
            r17[r16] = r10
            r16 = r13[r10]
            int r16 = r16 + 1
            r13[r10] = r16
            int r11 = r11 + 1
            r10 = r17
            goto L_0x004d
        L_0x0065:
            r17 = r10
            int r15 = r15 + 1
            r11 = 256(0x100, float:3.59E-43)
            goto L_0x0048
        L_0x006c:
            r17 = r10
            float r0 = (float) r0
            float r10 = r0 * r2
            int r10 = (int) r10
            float r0 = r0 * r1
            int r0 = (int) r0
            r15 = 0
            r20 = 0
        L_0x0077:
            r11 = 256(0x100, float:3.59E-43)
            if (r15 >= r11) goto L_0x008f
            r11 = r13[r15]
            r22 = r15
            long r14 = (long) r11
            long r20 = r20 + r14
            long r14 = (long) r10
            int r11 = (r20 > r14 ? 1 : (r20 == r14 ? 0 : -1))
            if (r11 <= 0) goto L_0x008a
            r15 = r22
            goto L_0x0090
        L_0x008a:
            int r15 = r22 + 1
            r14 = 255(0xff, float:3.57E-43)
            goto L_0x0077
        L_0x008f:
            r15 = 0
        L_0x0090:
            r10 = 255(0xff, float:3.57E-43)
            r18 = 0
        L_0x0094:
            if (r10 <= 0) goto L_0x00a8
            r11 = r13[r10]
            r24 = r10
            long r10 = (long) r11
            long r18 = r18 + r10
            long r10 = (long) r0
            int r10 = (r18 > r10 ? 1 : (r18 == r10 ? 0 : -1))
            if (r10 <= 0) goto L_0x00a5
            r10 = r24
            goto L_0x00a9
        L_0x00a5:
            int r10 = r24 + -1
            goto L_0x0094
        L_0x00a8:
            r10 = 0
        L_0x00a9:
            if (r15 <= r5) goto L_0x00ac
            r15 = r5
        L_0x00ac:
            if (r10 >= r6) goto L_0x00af
            r10 = r6
        L_0x00af:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x014d }
            r0.<init>()     // Catch:{ Exception -> 0x014d }
            java.lang.String r11 = "highThreshold---"
            r0.append(r11)     // Catch:{ Exception -> 0x014d }
            r0.append(r1)     // Catch:{ Exception -> 0x014d }
            java.lang.String r1 = "---lowThreshold---"
            r0.append(r1)     // Catch:{ Exception -> 0x014d }
            r0.append(r2)     // Catch:{ Exception -> 0x014d }
            java.lang.String r1 = "---scale---"
            r0.append(r1)     // Catch:{ Exception -> 0x014d }
            r0.append(r3)     // Catch:{ Exception -> 0x014d }
            java.lang.String r1 = "---sharpness---"
            r0.append(r1)     // Catch:{ Exception -> 0x014d }
            r0.append(r4)     // Catch:{ Exception -> 0x014d }
            java.lang.String r1 = "---lowValue---"
            r0.append(r1)     // Catch:{ Exception -> 0x014d }
            r0.append(r15)     // Catch:{ Exception -> 0x014d }
            java.lang.String r1 = "---highValue---"
            r0.append(r1)     // Catch:{ Exception -> 0x014d }
            r0.append(r10)     // Catch:{ Exception -> 0x014d }
            java.lang.String r1 = "---lValue---"
            r0.append(r1)     // Catch:{ Exception -> 0x014d }
            r0.append(r5)     // Catch:{ Exception -> 0x014d }
            java.lang.String r1 = "---hValue---"
            r0.append(r1)     // Catch:{ Exception -> 0x014d }
            r0.append(r6)     // Catch:{ Exception -> 0x014d }
            java.lang.String r1 = "---valueScale---"
            r0.append(r1)     // Catch:{ Exception -> 0x014d }
            r0.append(r7)     // Catch:{ Exception -> 0x014d }
            java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x014d }
            java.lang.Object[] r0 = new java.lang.Object[]{r0}     // Catch:{ Exception -> 0x014d }
            com.blankj.utilcode.util.LogUtils.e(r0)     // Catch:{ Exception -> 0x014d }
            r0 = 0
        L_0x0108:
            if (r0 >= r9) goto L_0x014d
            r1 = 0
        L_0x010b:
            if (r1 >= r12) goto L_0x0148
            int r2 = r12 * r0
            int r2 = r2 + r1
            r4 = r17[r2]     // Catch:{ Exception -> 0x014d }
            if (r4 > r15) goto L_0x0118
            float r4 = (float) r4     // Catch:{ Exception -> 0x014d }
            float r4 = r4 * r3
        L_0x0116:
            int r4 = (int) r4     // Catch:{ Exception -> 0x014d }
            goto L_0x0138
        L_0x0118:
            r5 = 1065353216(0x3f800000, float:1.0)
            if (r4 < r10) goto L_0x0125
            float r6 = (float) r4     // Catch:{ Exception -> 0x014d }
            int r4 = 255 - r4
            float r4 = (float) r4     // Catch:{ Exception -> 0x014d }
            float r5 = r5 - r3
            float r4 = r4 * r5
            float r6 = r6 + r4
            int r4 = (int) r6     // Catch:{ Exception -> 0x014d }
            goto L_0x0138
        L_0x0125:
            int r4 = r4 - r15
            float r4 = (float) r4     // Catch:{ Exception -> 0x014d }
            float r6 = (float) r10     // Catch:{ Exception -> 0x014d }
            int r11 = 255 - r10
            float r11 = (float) r11     // Catch:{ Exception -> 0x014d }
            float r5 = r5 - r3
            float r11 = r11 * r5
            float r6 = r6 + r11
            float r5 = (float) r15     // Catch:{ Exception -> 0x014d }
            float r5 = r5 * r3
            float r6 = r6 - r5
            float r4 = r4 * r6
            int r6 = r10 - r15
            float r6 = (float) r6     // Catch:{ Exception -> 0x014d }
            float r4 = r4 / r6
            float r4 = r4 + r5
            goto L_0x0116
        L_0x0138:
            double r4 = (double) r4     // Catch:{ Exception -> 0x014d }
            double r4 = r4 * r7
            int r4 = (int) r4     // Catch:{ Exception -> 0x014d }
            r5 = 255(0xff, float:3.57E-43)
            if (r4 <= r5) goto L_0x0140
            r4 = r5
        L_0x0140:
            if (r4 >= 0) goto L_0x0143
            r4 = 0
        L_0x0143:
            r17[r2] = r4     // Catch:{ Exception -> 0x014d }
            int r1 = r1 + 1
            goto L_0x010b
        L_0x0148:
            r5 = 255(0xff, float:3.57E-43)
            int r0 = r0 + 1
            goto L_0x0108
        L_0x014d:
            return r17
        */
        throw new UnsupportedOperationException("Method not decompiled: com.lib.Utils.ImageDisposeUtil.convertGreyImgByFloydPixels(android.content.Context, android.graphics.Bitmap, float, float, float, float, int, int, double):int[]");
    }

    public static void ditherGrayPixels(int[] iArr, int i, int i2) {
        try {
            int i3 = level;
            int i4 = 256 / i3;
            int i5 = 256 / (i3 - 1);
            int i6 = 0;
            while (i6 < i2) {
                boolean z = i6 < i2 + -1;
                int i7 = 0;
                while (i7 < i) {
                    boolean z2 = i7 > 0;
                    boolean z3 = i7 < i + -1;
                    int i8 = i6 * i;
                    int i9 = i7 + i8;
                    int i10 = iArr[i9];
                    int i11 = (i10 / i4) * i5;
                    iArr[i9] = i11;
                    int i12 = i10 - i11;
                    if (z3) {
                        int i13 = i7 + 1 + i8;
                        iArr[i13] = iArr[i13] + ((i12 * 7) / 16);
                    }
                    if (z2 && z) {
                        int i14 = (i7 - 1) + ((i6 + 1) * i);
                        iArr[i14] = iArr[i14] + ((i12 * 3) / 16);
                    }
                    if (z) {
                        int i15 = ((i6 + 1) * i) + i7;
                        iArr[i15] = iArr[i15] + ((i12 * 5) / 16);
                    }
                    if (z3 && z) {
                        int i16 = i7 + 1 + ((i6 + 1) * i);
                        iArr[i16] = iArr[i16] + (i12 / 16);
                    }
                    i7++;
                }
                i6++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void ditherPixels(int[] iArr, int i, int i2) {
        int i3 = 0;
        while (i3 < i2) {
            boolean z = i3 < i2 + -1;
            int i4 = 0;
            while (i4 < i) {
                boolean z2 = i4 > 0;
                boolean z3 = i4 < i + -1;
                int i5 = i3 * i;
                int i6 = i4 + i5;
                try {
                    int i7 = iArr[i6];
                    int i8 = i7 < 128 ? 0 : 255;
                    iArr[i6] = i8;
                    int i9 = i7 - i8;
                    if (z3) {
                        int i10 = i4 + 1 + i5;
                        iArr[i10] = iArr[i10] + ((i9 * 7) / 16);
                    }
                    if (z2 && z) {
                        int i11 = (i4 - 1) + ((i3 + 1) * i);
                        iArr[i11] = iArr[i11] + ((i9 * 3) / 16);
                    }
                    if (z) {
                        int i12 = ((i3 + 1) * i) + i4;
                        iArr[i12] = iArr[i12] + ((i9 * 5) / 16);
                    }
                    if (z3 && z) {
                        int i13 = i4 + 1 + ((i3 + 1) * i);
                        iArr[i13] = iArr[i13] + (i9 / 16);
                    }
                    i4++;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            i3++;
        }
    }

    public static byte[] convert4ColorImageToPointData(Context context, Bitmap bitmap) {
        byte[] bArr;
        try {
            int width = bitmap.getWidth() / 2;
            int width2 = bitmap.getWidth();
            int height = bitmap.getHeight();
            PrinterModel.DataBean printerModel = PrinterModelUtils.getPrinterModel();
            int[] convertGreyImgByFloydPixels = convertGreyImgByFloydPixels(context, bitmap, SPUtils.getInstance().getFloat(Code.highThreshold, 0.2f), SPUtils.getInstance().getFloat(Code.lowThreshold, 0.2f), SPUtils.getInstance().getFloat(Code.thresholdScale, 0.46f), SPUtils.getInstance().getFloat(Code.imageSharpness, 1.1f), SPUtils.getInstance().getInt(Code.grayLowValue, 110), SPUtils.getInstance().getInt(Code.grayHighValue, 150), printerModel.getGrayScale());
            ditherGrayPixels(convertGreyImgByFloydPixels, width2, height);
            int i = width * 16;
            int i2 = (height * width) + i;
            LogUtils.e("newBufLen:" + i2);
            if (i2 % 4 != 0) {
                bArr = new byte[(i2 + (i2 % 4))];
            } else {
                bArr = new byte[i2];
            }
            for (int i3 = 0; i3 < i; i3++) {
                bArr[i3] = 0;
            }
            int i4 = 0;
            for (int i5 = 0; i5 < height; i5++) {
                StringBuffer stringBuffer = new StringBuffer();
                for (int i6 = 0; i6 < width2; i6++) {
                    int i7 = level;
                    int i8 = (int) (((float) convertGreyImgByFloydPixels[i4]) / (256.0f / ((float) i7)));
                    int i9 = 15;
                    if (i8 == i7 - 1) {
                        i8 = 15;
                    }
                    if (i8 <= 15) {
                        i9 = i8;
                    }
                    if (i9 < 0) {
                        i9 = 0;
                    }
                    switch (i9) {
                        case 0:
                            stringBuffer.insert(0, PDNumberFormatDictionary.FRACTIONAL_DISPLAY_FRACTION);
                            break;
                        case 1:
                            stringBuffer.insert(0, "E");
                            break;
                        case 2:
                            stringBuffer.insert(0, "D");
                            break;
                        case 3:
                            stringBuffer.insert(0, "C");
                            break;
                        case 4:
                            stringBuffer.insert(0, "B");
                            break;
                        case 5:
                            stringBuffer.insert(0, "A");
                            break;
                        case 6:
                            stringBuffer.insert(0, "9");
                            break;
                        case 7:
                            stringBuffer.insert(0, "8");
                            break;
                        case 8:
                            stringBuffer.insert(0, "7");
                            break;
                        case 9:
                            stringBuffer.insert(0, "6");
                            break;
                        case 10:
                            stringBuffer.insert(0, "5");
                            break;
                        case 11:
                            stringBuffer.insert(0, "4");
                            break;
                        case 12:
                            stringBuffer.insert(0, "3");
                            break;
                        case 13:
                            stringBuffer.insert(0, "2");
                            break;
                        case 14:
                            stringBuffer.insert(0, "1");
                            break;
                        case 15:
                            stringBuffer.insert(0, PDLayoutAttributeObject.GLYPH_ORIENTATION_VERTICAL_ZERO_DEGREES);
                            break;
                        default:
                            stringBuffer.insert(0, "f");
                            break;
                    }
                    if (stringBuffer.length() == 2) {
                        bArr[i] = ConvertUtils.hexString2Bytes(stringBuffer.toString())[0];
                        i++;
                        stringBuffer = new StringBuffer();
                    }
                    i4++;
                }
            }
            int i10 = "DL_X7".equals(printerModel.getModelNo()) ? 1 : 20;
            ArrayList arrayList = new ArrayList();
            int i11 = width * i10;
            int i12 = (height + 16) / i10;
            int i13 = 0;
            for (int i14 = 0; i14 < i12; i14++) {
                byte[] bArr2 = new byte[i11];
                System.arraycopy(bArr, i14 * i11, bArr2, 0, i11);
                byte[] grayScanData = grayScanData(bArr2);
                arrayList.add(grayScanData);
                i13 = i13 + grayScanData.length + 9;
            }
            int i15 = i11 * i12;
            if (i15 < bArr.length) {
                byte[] bArr3 = new byte[(bArr.length - i15)];
                System.arraycopy(bArr, i15, bArr3, 0, bArr.length - i15);
                byte[] grayScanData2 = grayScanData(bArr3);
                arrayList.add(grayScanData2);
                i13 = i13 + grayScanData2.length + 9;
            }
            int grayImageSpeed = printerModel.getGrayImageSpeed();
            LogUtils.e("speed", "计算后的speed", Integer.valueOf(grayImageSpeed));
            byte[] feedPaper = PrintDataUtils.feedPaper(grayImageSpeed);
            byte[] bArr4 = new byte[i13];
            Iterator it = arrayList.iterator();
            int i16 = 0;
            while (it.hasNext()) {
                byte[] bArr5 = (byte[]) it.next();
                System.arraycopy(bArr5, 0, bArr4, i16, bArr5.length);
                int length = i16 + bArr5.length;
                System.arraycopy(feedPaper, 0, bArr4, length, feedPaper.length);
                i16 = length + feedPaper.length;
            }
            arrayList.clear();
            return bArr4;
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    public static byte[] grayScanData(byte[] bArr) {
        byte[] bArr2;
        try {
            if ("FL01".equals(PrinterModelUtils.getPrinterModel().getModelNo())) {
                bArr2 = bArr;
            } else {
                bArr2 = MiniLZO.compress(bArr);
            }
            byte[] bArr3 = new byte[(bArr2.length + 12)];
            bArr3[0] = 81;
            bArr3[1] = TarConstants.LF_PAX_EXTENDED_HEADER_LC;
            bArr3[2] = -49;
            bArr3[3] = 0;
            byte[] hexString2Bytes = ConvertUtils.hexString2Bytes(Integer.toHexString(bArr2.length + 4));
            if (hexString2Bytes.length > 1) {
                bArr3[4] = hexString2Bytes[1];
                bArr3[5] = hexString2Bytes[0];
            } else {
                bArr3[4] = hexString2Bytes[0];
                bArr3[5] = 0;
            }
            byte[] hexString2Bytes2 = ConvertUtils.hexString2Bytes(Integer.toHexString(bArr.length));
            if (hexString2Bytes2.length > 1) {
                bArr3[6] = hexString2Bytes2[1];
                bArr3[7] = hexString2Bytes2[0];
            } else {
                bArr3[6] = hexString2Bytes2[0];
                bArr3[7] = 0;
            }
            byte[] hexString2Bytes3 = ConvertUtils.hexString2Bytes(Integer.toHexString(bArr2.length));
            if (hexString2Bytes3.length > 1) {
                bArr3[8] = hexString2Bytes3[1];
                bArr3[9] = hexString2Bytes3[0];
            } else {
                bArr3[8] = hexString2Bytes3[0];
                bArr3[9] = 0;
            }
            System.arraycopy(bArr2, 0, bArr3, 10, bArr2.length);
            bArr3[bArr2.length + 10] = BluetoothOrder.calcCrc8(bArr3, 6, bArr2.length + 4);
            bArr3[bArr2.length + 11] = -1;
            return bArr3;
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    public static byte[] eightLZOData(byte[] bArr) {
        byte[] bArr2;
        try {
            if (PrinterModelUtils.getPrinterModel().isCorePrint()) {
                bArr2 = zlibCompress(bArr);
            } else {
                bArr2 = MiniLZO.compress(bArr);
            }
            byte[] bArr3 = new byte[(bArr2.length + 12)];
            bArr3[0] = 81;
            bArr3[1] = TarConstants.LF_PAX_EXTENDED_HEADER_LC;
            bArr3[2] = -50;
            bArr3[3] = 0;
            byte[] hexString2Bytes = ConvertUtils.hexString2Bytes(Integer.toHexString(bArr2.length + 4));
            if (hexString2Bytes.length > 1) {
                bArr3[4] = hexString2Bytes[1];
                bArr3[5] = hexString2Bytes[0];
            } else {
                bArr3[4] = hexString2Bytes[0];
                bArr3[5] = 0;
            }
            byte[] hexString2Bytes2 = ConvertUtils.hexString2Bytes(Integer.toHexString(bArr.length));
            if (hexString2Bytes2.length > 1) {
                bArr3[6] = hexString2Bytes2[1];
                bArr3[7] = hexString2Bytes2[0];
            } else {
                bArr3[6] = hexString2Bytes2[0];
                bArr3[7] = 0;
            }
            byte[] hexString2Bytes3 = ConvertUtils.hexString2Bytes(Integer.toHexString(bArr2.length));
            if (hexString2Bytes3.length > 1) {
                bArr3[8] = hexString2Bytes3[1];
                bArr3[9] = hexString2Bytes3[0];
            } else {
                bArr3[8] = hexString2Bytes3[0];
                bArr3[9] = 0;
            }
            System.arraycopy(bArr2, 0, bArr3, 10, bArr2.length);
            bArr3[bArr2.length + 10] = BluetoothOrder.calcCrc8(bArr3, 6, bArr2.length + 4);
            bArr3[bArr2.length + 11] = -1;
            return bArr3;
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    public static byte[] zlibCompress(byte[] bArr) {
        Deflater deflater = new Deflater();
        deflater.setInput(bArr);
        deflater.finish();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(bArr.length);
        byte[] bArr2 = new byte[1024];
        while (!deflater.finished()) {
            byteArrayOutputStream.write(bArr2, 0, deflater.deflate(bArr2));
        }
        try {
            byteArrayOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Bitmap beautyImage(Bitmap bitmap, float f, float f2, float f3) {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setRotate(0, f);
        colorMatrix.setRotate(1, f);
        colorMatrix.setRotate(2, f);
        ColorMatrix colorMatrix2 = new ColorMatrix();
        colorMatrix2.setSaturation(f2);
        ColorMatrix colorMatrix3 = new ColorMatrix();
        colorMatrix3.setScale(f3, f3, f3, 1.0f);
        ColorMatrix colorMatrix4 = new ColorMatrix();
        colorMatrix4.postConcat(colorMatrix);
        colorMatrix4.postConcat(colorMatrix2);
        colorMatrix4.postConcat(colorMatrix3);
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint(1);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix4));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        return createBitmap;
    }

    public static Bitmap mirrorBitmap(Bitmap bitmap) {
        Bitmap bitmap2;
        try {
            Matrix matrix = new Matrix();
            matrix.preScale(-1.0f, 1.0f);
            bitmap2 = bitmap;
            try {
                return Bitmap.createBitmap(bitmap2, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            } catch (Exception e) {
                e = e;
                Exception exc = e;
                LogUtils.e("ImageDisposeUtil", "镜像图片失败");
                exc.printStackTrace();
                return bitmap2;
            }
        } catch (Exception e2) {
            e = e2;
            bitmap2 = bitmap;
            Exception exc2 = e;
            LogUtils.e("ImageDisposeUtil", "镜像图片失败");
            exc2.printStackTrace();
            return bitmap2;
        }
    }

    public static boolean isWhiteBitmap(Bitmap bitmap) {
        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int i = width * height;
            int[] iArr = new int[i];
            bitmap.getPixels(iArr, 0, width, 0, 0, width, height);
            for (int i2 = 0; i2 < i; i2++) {
                if (iArr[i2] != -1) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Bitmap setBitmapAlpha(Bitmap bitmap, int i) {
        try {
            Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            Paint paint = new Paint();
            paint.setAlpha(i);
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
            return createBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return bitmap;
        }
    }

    public static Bitmap convertToLineDrawing(Bitmap bitmap, int i) {
        LogUtils.e("convertToLineDrawing", Integer.valueOf(i));
        Mat mat = new Mat();
        Utils.bitmapToMat(bitmap, mat);
        Mat mat2 = new Mat();
        Imgproc.cvtColor(mat, mat2, 6);
        Mat mat3 = new Mat();
        Imgproc.bilateralFilter(mat2, mat3, 9, 75.0d, 75.0d);
        Mat mat4 = new Mat();
        Imgproc.GaussianBlur(mat3, mat4, new Size(5.0d, 5.0d), 0.0d);
        Mat mat5 = new Mat();
        Imgproc.Canny(mat4, mat5, (double) (i - 70), (double) (30 - i));
        Mat mat6 = new Mat();
        Core.bitwise_not(mat5, mat6);
        Bitmap createBitmap = Bitmap.createBitmap(mat6.cols(), mat6.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat6, createBitmap);
        return createBitmap;
    }

    public static Bitmap convertToSketch(Bitmap bitmap, int i) {
        if (i % 2 == 0) {
            i++;
        }
        if (i <= 0) {
            i = 1;
        }
        LogUtils.e("convertToSketch", Integer.valueOf(i));
        Mat mat = new Mat();
        Utils.bitmapToMat(bitmap, mat);
        Mat mat2 = new Mat();
        Imgproc.cvtColor(mat, mat2, 6);
        Mat mat3 = new Mat();
        Core.bitwise_not(mat2, mat3);
        Mat mat4 = new Mat();
        double d = (double) i;
        Imgproc.GaussianBlur(mat3, mat4, new Size(d, d), 0.0d);
        Mat mat5 = new Mat();
        Core.bitwise_not(mat4, mat5);
        Mat mat6 = new Mat();
        Core.divide(mat2, mat5, mat6, 256.0d);
        Bitmap createBitmap = Bitmap.createBitmap(mat6.cols(), mat6.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat6, createBitmap);
        return createBitmap;
    }

    public static Bitmap convertToSketchWithCanny(Bitmap bitmap, int i) {
        Mat mat = new Mat();
        Utils.bitmapToMat(bitmap, mat);
        Mat mat2 = new Mat();
        Imgproc.cvtColor(mat, mat2, 6);
        Mat mat3 = new Mat();
        Imgproc.Canny(mat2, mat3, (double) (50 - i), (double) (150 - i));
        Mat mat4 = new Mat();
        Core.bitwise_not(mat3, mat4);
        Bitmap createBitmap = Bitmap.createBitmap(mat4.cols(), mat4.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat4, createBitmap);
        return createBitmap;
    }

    public static Bitmap convertToBlackWhite(Bitmap bitmap) {
        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int i = width;
            int i2 = height;
            int[] iArr = new int[(width * height)];
            bitmap.getPixels(iArr, 0, i, 0, 0, i, i2);
            int[] iArr2 = iArr;
            for (int i3 = 0; i3 < i2; i3++) {
                for (int i4 = 0; i4 < i; i4++) {
                    int i5 = (i * i3) + i4;
                    int i6 = iArr2[i5];
                    int i7 = (int) ((((double) ((16711680 & i6) >> 16)) * 0.3d) + (((double) ((65280 & i6) >> 8)) * 0.59d) + (((double) (i6 & 255)) * 0.11d));
                    iArr2[i5] = i7 | (i7 << 16) | ViewCompat.MEASURED_STATE_MASK | (i7 << 8);
                }
            }
            Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.RGB_565);
            int i8 = i;
            createBitmap.setPixels(iArr2, 0, i8, 0, 0, i8, i2);
            return createBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return bitmap;
        }
    }

    public static FitTransparentBean fitTransparentBit(Bitmap bitmap) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int[] iArr = new int[(width * height)];
        Bitmap bitmap2 = bitmap;
        bitmap2.getPixels(iArr, 0, width, 0, 0, width, height);
        int i = 0;
        for (int i2 = 0; i2 < height; i2++) {
            int i3 = 0;
            while (true) {
                if (i3 >= width) {
                    break;
                } else if (iArr[(i2 * width) + i3] != 0) {
                    i = i2;
                    break;
                } else {
                    i3++;
                }
            }
            if (i != 0) {
                break;
            }
        }
        int i4 = i;
        int i5 = height;
        for (int i6 = height - 1; i6 >= 0; i6--) {
            int i7 = 0;
            while (true) {
                if (i7 >= width) {
                    break;
                } else if (iArr[(i6 * width) + i7] != 0) {
                    i5 = i6;
                    break;
                } else {
                    i7++;
                }
            }
            if (i5 != height) {
                break;
            }
        }
        int i8 = i5;
        int i9 = 0;
        for (int i10 = 0; i10 < width; i10++) {
            int i11 = 0;
            while (true) {
                if (i11 >= height) {
                    break;
                } else if (iArr[(i11 * width) + i10] != 0) {
                    i9 = i10;
                    break;
                } else {
                    i11++;
                }
            }
            if (i9 != 0) {
                break;
            }
        }
        int i12 = i9;
        int i13 = width;
        for (int i14 = width - 1; i14 >= 0; i14--) {
            int i15 = 0;
            while (true) {
                if (i15 >= height) {
                    break;
                } else if (iArr[(i15 * width) + i14] != 0) {
                    i13 = i14;
                    break;
                } else {
                    i15++;
                }
            }
            if (i13 != width) {
                break;
            }
        }
        int i16 = i13;
        return new FitTransparentBean(i12, i4, i16, i8, Bitmap.createBitmap(bitmap2, i12, i4, i16 - i12, i8 - i4));
    }

    public static Bitmap fitWhiteBitmap(Bitmap bitmap) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int[] iArr = new int[(width * height)];
        Bitmap bitmap2 = bitmap;
        bitmap2.getPixels(iArr, 0, width, 0, 0, width, height);
        int i = 0;
        for (int i2 = 0; i2 < height; i2++) {
            int i3 = 0;
            while (true) {
                if (i3 >= width) {
                    break;
                } else if (iArr[(i2 * width) + i3] != -1) {
                    i = i2;
                    break;
                } else {
                    i3++;
                }
            }
            if (i != 0) {
                break;
            }
        }
        int i4 = height;
        for (int i5 = height - 1; i5 >= 0; i5--) {
            int i6 = 0;
            while (true) {
                if (i6 >= width) {
                    break;
                } else if (iArr[(i5 * width) + i6] != -1) {
                    i4 = i5;
                    break;
                } else {
                    i6++;
                }
            }
            if (i4 != height) {
                break;
            }
        }
        int i7 = 0;
        for (int i8 = 0; i8 < width; i8++) {
            int i9 = 0;
            while (true) {
                if (i9 >= height) {
                    break;
                } else if (iArr[(i9 * width) + i8] != -1) {
                    i7 = i8;
                    break;
                } else {
                    i9++;
                }
            }
            if (i7 != 0) {
                break;
            }
        }
        int i10 = width;
        for (int i11 = width - 1; i11 >= 0; i11--) {
            int i12 = 0;
            while (true) {
                if (i12 >= height) {
                    break;
                } else if (iArr[(i12 * width) + i11] != -1) {
                    i10 = i11;
                    break;
                } else {
                    i12++;
                }
            }
            if (i10 != width) {
                break;
            }
        }
        return Bitmap.createBitmap(bitmap2, i7, i, i10 - i7, i4 - i);
    }

    public static Bitmap fitBlackBitmap(Bitmap bitmap) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int[] iArr = new int[(width * height)];
        Bitmap bitmap2 = bitmap;
        bitmap2.getPixels(iArr, 0, width, 0, 0, width, height);
        int i = 0;
        for (int i2 = 0; i2 < height; i2++) {
            int i3 = 0;
            while (true) {
                if (i3 >= width) {
                    break;
                } else if (iArr[(i2 * width) + i3] != -1) {
                    i = i2;
                    break;
                } else {
                    i3++;
                }
            }
            if (i != 0) {
                break;
            }
        }
        int i4 = height;
        for (int i5 = height - 1; i5 >= 0; i5--) {
            int i6 = 0;
            while (true) {
                if (i6 >= width) {
                    break;
                } else if (iArr[(i5 * width) + i6] != -1) {
                    i4 = i5;
                    break;
                } else {
                    i6++;
                }
            }
            if (i4 != height) {
                break;
            }
        }
        int i7 = 0;
        for (int i8 = 0; i8 < width; i8++) {
            int i9 = 0;
            while (true) {
                if (i9 >= height) {
                    break;
                } else if (iArr[(i9 * width) + i8] != -1) {
                    i7 = i8;
                    break;
                } else {
                    i9++;
                }
            }
            if (i7 != 0) {
                break;
            }
        }
        int i10 = width;
        for (int i11 = width - 1; i11 >= 0; i11--) {
            int i12 = 0;
            while (true) {
                if (i12 >= height) {
                    break;
                } else if (iArr[(i12 * width) + i11] != -1) {
                    i10 = i11;
                    break;
                } else {
                    i12++;
                }
            }
            if (i10 != width) {
                break;
            }
        }
        return Bitmap.createBitmap(bitmap2, i7, i, i10 - i7, i4 - i);
    }

    public static Bitmap fiTransparentBitmap(Bitmap bitmap) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int[] iArr = new int[(width * height)];
        Bitmap bitmap2 = bitmap;
        bitmap2.getPixels(iArr, 0, width, 0, 0, width, height);
        int i = 0;
        for (int i2 = 0; i2 < height; i2++) {
            int i3 = 0;
            while (true) {
                if (i3 >= width) {
                    break;
                } else if (iArr[(i2 * width) + i3] != 0) {
                    i = i2;
                    break;
                } else {
                    i3++;
                }
            }
            if (i != 0) {
                break;
            }
        }
        int i4 = height;
        for (int i5 = height - 1; i5 >= 0; i5--) {
            int i6 = 0;
            while (true) {
                if (i6 >= width) {
                    break;
                } else if (iArr[(i5 * width) + i6] != 0) {
                    i4 = i5;
                    break;
                } else {
                    i6++;
                }
            }
            if (i4 != height) {
                break;
            }
        }
        int i7 = 0;
        for (int i8 = 0; i8 < width; i8++) {
            int i9 = 0;
            while (true) {
                if (i9 >= height) {
                    break;
                } else if (iArr[(i9 * width) + i8] != 0) {
                    i7 = i8;
                    break;
                } else {
                    i9++;
                }
            }
            if (i7 != 0) {
                break;
            }
        }
        int i10 = width;
        for (int i11 = width - 1; i11 >= 0; i11--) {
            int i12 = 0;
            while (true) {
                if (i12 >= height) {
                    break;
                } else if (iArr[(i12 * width) + i11] != 0) {
                    i10 = i11;
                    break;
                } else {
                    i12++;
                }
            }
            if (i10 != width) {
                break;
            }
        }
        return Bitmap.createBitmap(bitmap2, i7, i, i10 - i7, i4 - i);
    }

    public static Bitmap whiteToTransparent(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] iArr = new int[(width * height)];
        bitmap.getPixels(iArr, 0, width, 0, 0, width, height);
        for (int i = 0; i < height; i++) {
            for (int i2 = 0; i2 < width; i2++) {
                int i3 = (i * width) + i2;
                if (iArr[i3] == -1) {
                    iArr[i3] = 0;
                }
            }
        }
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        createBitmap.setPixels(iArr, 0, width, 0, 0, width, height);
        return createBitmap;
    }

    public static Bitmap getAssetsBitmap(Context context, String str) {
        try {
            if (str.startsWith("/system/")) {
                return ImageUtils.getBitmap(str);
            }
            InputStream open = context.getAssets().open(str);
            Bitmap decodeStream = BitmapFactory.decodeStream(open);
            open.close();
            return decodeStream;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static InputStream bitmapToInputStream(Bitmap bitmap, Bitmap.CompressFormat compressFormat, int i) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(compressFormat, i, byteArrayOutputStream);
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    public static Bitmap decodeSampledBitmapFromBitmap(Bitmap bitmap, int i) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width <= i) {
            return bitmap;
        }
        int i2 = 1;
        if (width > i) {
            while (width / i2 > i) {
                i2 *= 2;
            }
        }
        new BitmapFactory.Options().inSampleSize = i2;
        int i3 = width / i2;
        int i4 = height / i2;
        if (AICameraDeviceUtils.Companion.isAoMeiJia()) {
            i3 = 720;
            i4 = 1280;
            if (width > height) {
                i4 = 720;
                i3 = 1280;
            }
        }
        return Bitmap.createScaledBitmap(bitmap, i3, i4, true);
    }

    public static String saveBitmap(Bitmap bitmap, boolean z) {
        if (bitmap == null) {
            return "";
        }
        try {
            String str = PathUtils.getExternalAppPicturesPath() + "/" + TimeUtils.getNowMills() + ".jpg";
            if (!ImageUtils.save(bitmap, str, Bitmap.CompressFormat.PNG)) {
                return "";
            }
            if (!z) {
                return str;
            }
            bitmap.recycle();
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:55:0x00c2 A[SYNTHETIC, Splitter:B:55:0x00c2] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.io.File composeAndSaveOnce(android.content.Context r7, android.graphics.Bitmap r8, android.graphics.Bitmap r9, boolean r10, int r11, java.io.File r12, int r13) throws java.io.IOException {
        /*
            if (r8 == 0) goto L_0x00d1
            boolean r7 = r8.isRecycled()
            if (r7 != 0) goto L_0x00d1
            int r7 = r8.getWidth()
            int r0 = r8.getHeight()
            if (r11 > 0) goto L_0x0013
            r11 = r7
        L_0x0013:
            r1 = 1065353216(0x3f800000, float:1.0)
            if (r7 <= r11) goto L_0x001c
            float r11 = (float) r11
            float r11 = r11 * r1
            float r2 = (float) r7
            float r11 = r11 / r2
            goto L_0x001d
        L_0x001c:
            r11 = r1
        L_0x001d:
            float r7 = (float) r7
            float r7 = r7 * r11
            int r7 = java.lang.Math.round(r7)
            r2 = 1
            int r7 = java.lang.Math.max(r2, r7)
            float r0 = (float) r0
            float r0 = r0 * r11
            int r0 = java.lang.Math.round(r0)
            int r0 = java.lang.Math.max(r2, r0)
            boolean r3 = r8.hasAlpha()
            if (r3 != 0) goto L_0x0042
            if (r9 == 0) goto L_0x0041
            boolean r3 = r9.hasAlpha()
            if (r3 == 0) goto L_0x0041
            goto L_0x0042
        L_0x0041:
            r2 = 0
        L_0x0042:
            if (r2 == 0) goto L_0x0047
            android.graphics.Bitmap$Config r3 = android.graphics.Bitmap.Config.ARGB_8888
            goto L_0x0049
        L_0x0047:
            android.graphics.Bitmap$Config r3 = android.graphics.Bitmap.Config.RGB_565
        L_0x0049:
            r4 = 0
            android.graphics.Bitmap r3 = android.graphics.Bitmap.createBitmap(r7, r0, r3)     // Catch:{ all -> 0x00be }
            android.graphics.Canvas r5 = new android.graphics.Canvas     // Catch:{ all -> 0x0069 }
            r5.<init>(r3)     // Catch:{ all -> 0x0069 }
            android.graphics.Matrix r6 = new android.graphics.Matrix     // Catch:{ all -> 0x0069 }
            r6.<init>()     // Catch:{ all -> 0x0069 }
            r6.postScale(r11, r11)     // Catch:{ all -> 0x0069 }
            if (r10 == 0) goto L_0x006b
            float r10 = (float) r7     // Catch:{ all -> 0x0069 }
            r11 = 1073741824(0x40000000, float:2.0)
            float r10 = r10 / r11
            float r0 = (float) r0     // Catch:{ all -> 0x0069 }
            float r0 = r0 / r11
            r11 = -1082130432(0xffffffffbf800000, float:-1.0)
            r6.postScale(r11, r1, r10, r0)     // Catch:{ all -> 0x0069 }
            goto L_0x006b
        L_0x0069:
            r7 = move-exception
            goto L_0x00c0
        L_0x006b:
            r5.drawBitmap(r8, r6, r4)     // Catch:{ all -> 0x0069 }
            if (r9 == 0) goto L_0x0090
            boolean r8 = r9.isRecycled()     // Catch:{ all -> 0x0069 }
            if (r8 != 0) goto L_0x0090
            int r8 = r9.getWidth()     // Catch:{ all -> 0x0069 }
            if (r8 <= r7) goto L_0x0085
            float r7 = (float) r7     // Catch:{ all -> 0x0069 }
            float r7 = r7 * r1
            int r8 = r9.getWidth()     // Catch:{ all -> 0x0069 }
            float r8 = (float) r8     // Catch:{ all -> 0x0069 }
            float r1 = r7 / r8
        L_0x0085:
            android.graphics.Matrix r7 = new android.graphics.Matrix     // Catch:{ all -> 0x0069 }
            r7.<init>()     // Catch:{ all -> 0x0069 }
            r7.postScale(r1, r1)     // Catch:{ all -> 0x0069 }
            r5.drawBitmap(r9, r7, r4)     // Catch:{ all -> 0x0069 }
        L_0x0090:
            java.io.FileOutputStream r7 = new java.io.FileOutputStream     // Catch:{ all -> 0x0069 }
            r7.<init>(r12)     // Catch:{ all -> 0x0069 }
            if (r2 != 0) goto L_0x00a5
            android.graphics.Bitmap$CompressFormat r8 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch:{ all -> 0x00a1 }
            int r9 = clampQuality(r13)     // Catch:{ all -> 0x00a1 }
            r3.compress(r8, r9, r7)     // Catch:{ all -> 0x00a1 }
            goto L_0x00ac
        L_0x00a1:
            r8 = move-exception
            r4 = r7
            r7 = r8
            goto L_0x00c0
        L_0x00a5:
            android.graphics.Bitmap$CompressFormat r8 = android.graphics.Bitmap.CompressFormat.PNG     // Catch:{ all -> 0x00a1 }
            r9 = 100
            r3.compress(r8, r9, r7)     // Catch:{ all -> 0x00a1 }
        L_0x00ac:
            r7.flush()     // Catch:{ all -> 0x00a1 }
            r7.close()     // Catch:{ Exception -> 0x00b2 }
        L_0x00b2:
            if (r3 == 0) goto L_0x00bd
            boolean r7 = r3.isRecycled()
            if (r7 != 0) goto L_0x00bd
            r3.recycle()
        L_0x00bd:
            return r12
        L_0x00be:
            r7 = move-exception
            r3 = r4
        L_0x00c0:
            if (r4 == 0) goto L_0x00c5
            r4.close()     // Catch:{ Exception -> 0x00c5 }
        L_0x00c5:
            if (r3 == 0) goto L_0x00d0
            boolean r8 = r3.isRecycled()
            if (r8 != 0) goto L_0x00d0
            r3.recycle()
        L_0x00d0:
            throw r7
        L_0x00d1:
            java.lang.IllegalArgumentException r7 = new java.lang.IllegalArgumentException
            java.lang.String r8 = "shot is null or recycled"
            r7.<init>(r8)
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.lib.Utils.ImageDisposeUtil.composeAndSaveOnce(android.content.Context, android.graphics.Bitmap, android.graphics.Bitmap, boolean, int, java.io.File, int):java.io.File");
    }

    public static Bitmap snapshotViewScaled(View view, int i, boolean z) {
        if (view == null || view.getWidth() <= 0 || view.getHeight() <= 0) {
            return null;
        }
        int width = view.getWidth();
        int height = view.getHeight();
        if (i <= 0) {
            i = width;
        }
        float f = 1.0f;
        if (width > i) {
            f = (((float) i) * 1.0f) / ((float) width);
        }
        Bitmap createBitmap = Bitmap.createBitmap(Math.max(1, Math.round(((float) width) * f)), Math.max(1, Math.round(((float) height) * f)), z ? Bitmap.Config.RGB_565 : Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.scale(f, f);
        view.draw(canvas);
        return createBitmap;
    }
}
