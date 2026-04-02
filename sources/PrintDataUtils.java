package com.lib.blueUtils;

import android.content.Context;
import android.graphics.Bitmap;
import cn.com.heaton.blelibrary.ble.utils.ByteUtils;
import com.Utils.PrinterModel;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.lib.Beans.PrintBean;
import com.lib.Utils.Code;
import com.lib.Utils.ComputeUtils;
import com.lib.Utils.DeviceUtils;
import com.lib.Utils.EventBusUtils;
import com.lib.Utils.ImageDisposeUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.poi.ss.formula.ptg.RefErrorPtg;

public class PrintDataUtils {
    private static PrintDataUtils mSingleInstance;
    /* access modifiers changed from: private */
    public static int[] p0 = {0, 128};
    /* access modifiers changed from: private */
    public static int[] p1 = {0, 64};
    /* access modifiers changed from: private */
    public static int[] p2 = {0, 32};
    /* access modifiers changed from: private */
    public static int[] p3 = {0, 16};
    /* access modifiers changed from: private */
    public static int[] p4 = {0, 8};
    /* access modifiers changed from: private */
    public static int[] p5 = {0, 4};
    /* access modifiers changed from: private */
    public static int[] p6 = {0, 2};
    private int ImgSpeed = 0;
    private boolean canPrintLabel = false;
    private int concentration = 0;
    private ArrayList dataIdentify;
    /* access modifiers changed from: private */
    public int edtEnergy = 0;
    private int eneragy = 0;
    /* access modifiers changed from: private */
    public ArrayList eneragyList;
    private boolean newCompress = true;
    private int textSpeed = 0;

    public static PrintDataUtils getInstance() {
        if (mSingleInstance == null) {
            synchronized (PrintDataUtils.class) {
                try {
                    if (mSingleInstance == null) {
                        mSingleInstance = new PrintDataUtils();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return mSingleInstance;
    }

    public boolean isCanPrintLabel() {
        return this.canPrintLabel;
    }

    public void setCanPrintLabel(boolean z) {
        this.canPrintLabel = z;
    }

    public int getTextSpeed() {
        return this.textSpeed;
    }

    public void setTextSpeed(int i) {
        this.textSpeed = i;
    }

    public int getImgSpeed() {
        return this.ImgSpeed;
    }

    public int getEdtEnergy() {
        return this.edtEnergy;
    }

    public void setEdtEnergy(int i) {
        this.edtEnergy = i;
    }

    public int getEneragy(int i, boolean z) {
        int i2 = this.edtEnergy;
        if (i2 > 0) {
            return i2;
        }
        return PrinterModelUtils.getPrintEneragy(this.concentration, i, z);
    }

    public int getTattooEnergy(PrinterModel.DataBean dataBean) {
        int i = this.edtEnergy;
        if (i > 0) {
            return i;
        }
        if (!"GW09".equals(dataBean.getModelNo())) {
            return dataBean.getTattooEnergy();
        }
        double tattooEnergy = (double) dataBean.getTattooEnergy();
        return (int) (tattooEnergy + (((double) (this.concentration - Code.DEFCONCENTRATIONS)) * 0.15d * tattooEnergy));
    }

    public ArrayList<Integer> getEneragyList() {
        return this.eneragyList;
    }

    public void setEneragyList(ArrayList<Integer> arrayList) {
        this.eneragyList = arrayList;
    }

    public void setImgSpeed(int i) {
        this.ImgSpeed = i;
    }

    public void BitmapToData(Context context, ArrayList<PrintBean> arrayList, int i, int i2, int i3, boolean z) {
        if (arrayList.size() == 0) {
            EventBusUtils.getInstance().post("onWriteFailed");
            return;
        }
        final PrinterModel.DataBean printerModel = PrinterModelUtils.getPrinterModel();
        this.concentration = i3;
        LogUtils.e("concentration---" + this.concentration);
        this.newCompress = z;
        this.dataIdentify = new ArrayList();
        final Context context2 = context;
        final ArrayList<PrintBean> arrayList2 = arrayList;
        final int i4 = i;
        final int i5 = i3;
        new Thread(new Runnable(1) {
            /* JADX WARNING: Removed duplicated region for block: B:43:0x0171 A[LOOP:2: B:41:0x016b->B:43:0x0171, LOOP_END] */
            /* JADX WARNING: Removed duplicated region for block: B:48:0x018e  */
            /* JADX WARNING: Removed duplicated region for block: B:50:0x019b  */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                    r12 = this;
                    java.util.ArrayList r0 = new java.util.ArrayList
                    r0.<init>()
                    java.util.Vector r1 = new java.util.Vector
                    r1.<init>()
                    java.util.ArrayList r2 = r2
                    r1.addAll(r2)
                    r2 = 0
                    r3 = r2
                    r4 = r3
                L_0x0012:
                    int r5 = r1.size()
                    if (r3 >= r5) goto L_0x005c
                    java.lang.Object r5 = r1.get(r3)
                    com.lib.Beans.PrintBean r5 = (com.lib.Beans.PrintBean) r5
                    boolean r6 = r5.isShow()
                    if (r6 == 0) goto L_0x0059
                    android.graphics.Bitmap r6 = r5.getBitmap()
                    if (r6 == 0) goto L_0x0059
                    android.content.Context r6 = r3
                    android.graphics.Bitmap r7 = r5.getBitmap()
                    int r8 = r5.getPrintType()
                    byte[] r6 = com.lib.blueUtils.PrintDataUtils.bitmapToBWPix(r6, r7, r8)
                    r5.getLabelUpPaperNum()
                    com.lib.blueUtils.PrintDataUtils r7 = com.lib.blueUtils.PrintDataUtils.this
                    int r8 = r4
                    int r9 = r5.getPrintType()
                    byte[] r6 = r7.eachLinePixToCmdB(r6, r8, r9)
                    boolean r5 = r5.isAddWhite()
                    if (r5 == 0) goto L_0x0054
                    byte[] r5 = com.lib.blueUtils.BluetoothOrder.paper
                    int r7 = r5.length
                    int r4 = r4 + r7
                    r0.add(r5)
                L_0x0054:
                    int r5 = r6.length
                    int r4 = r4 + r5
                    r0.add(r6)
                L_0x0059:
                    int r3 = r3 + 1
                    goto L_0x0012
                L_0x005c:
                    com.lib.blueUtils.PrintDataUtils r3 = com.lib.blueUtils.PrintDataUtils.this
                    java.util.ArrayList r3 = r3.eneragyList
                    if (r3 == 0) goto L_0x006d
                    com.lib.blueUtils.PrintDataUtils r3 = com.lib.blueUtils.PrintDataUtils.this
                    java.util.ArrayList r3 = r3.eneragyList
                    r3.clear()
                L_0x006d:
                    com.lib.blueUtils.PrintDataUtils r3 = com.lib.blueUtils.PrintDataUtils.this
                    boolean r3 = r3.isCanPrintLabel()
                    java.lang.String r5 = "Label Printer"
                    if (r3 == 0) goto L_0x00ac
                    com.Utils.PrinterModel$DataBean r3 = r5
                    java.lang.String r3 = r3.getModelNo()
                    boolean r3 = r5.equals(r3)
                    if (r3 == 0) goto L_0x009d
                    int r4 = r4 + 20
                    byte[] r3 = com.lib.blueUtils.BluetoothOrder.paper
                    int r3 = r3.length
                    int r3 = r3 * 2
                    int r4 = r4 + r3
                    int r4 = r4 + 9
                    int r3 = 1
                    int r4 = r4 * r3
                    int r4 = r4 + 9
                    r3 = 8
                    byte[] r3 = com.lib.blueUtils.PrintDataUtils.LongDetectionLabel(r3)
                    int r3 = r3.length
                    int r4 = r4 + r3
                    byte[] r3 = new byte[r4]
                    goto L_0x0109
                L_0x009d:
                    byte[] r3 = com.lib.blueUtils.BluetoothOrder.quality3
                    int r3 = r3.length
                    int r4 = r4 + r3
                    int r4 = r4 + 9
                    byte[] r3 = com.lib.blueUtils.BluetoothOrder.getDevState
                    int r3 = r3.length
                    int r4 = r4 + r3
                    int r4 = r4 + 20
                    byte[] r3 = new byte[r4]
                    goto L_0x0109
                L_0x00ac:
                    com.Utils.PrinterModel$DataBean r3 = r5
                    java.lang.String r3 = r3.getModelNo()
                    boolean r3 = r5.equals(r3)
                    if (r3 == 0) goto L_0x00e1
                    byte[] r3 = com.lib.blueUtils.BluetoothOrder.paper
                    int r6 = r3.length
                    int r6 = r6 * 3
                    int r4 = r4 + r6
                    int r4 = r4 + 9
                    int r6 = 1
                    int r4 = r4 * r6
                    int r4 = r4 + 9
                    int r3 = r3.length
                    com.Utils.PrinterModel$DataBean r6 = r5
                    int r6 = r6.getPaperNum()
                    int r6 = r6 + -2
                    int r3 = r3 * r6
                    int r4 = r4 + r3
                    byte[] r3 = com.lib.blueUtils.BluetoothOrder.printLattice
                    int r3 = r3.length
                    int r4 = r4 + r3
                    byte[] r3 = com.lib.blueUtils.BluetoothOrder.finishLattice
                    int r3 = r3.length
                    int r4 = r4 + r3
                    int r4 = r4 + 9
                    byte[] r3 = com.lib.blueUtils.BluetoothOrder.getDevState
                    int r3 = r3.length
                    int r4 = r4 + r3
                    byte[] r3 = new byte[r4]
                    goto L_0x0109
                L_0x00e1:
                    byte[] r3 = com.lib.blueUtils.BluetoothOrder.paper
                    int r6 = r3.length
                    int r6 = r6 * 2
                    int r4 = r4 + r6
                    int r4 = r4 + 9
                    int r6 = 1
                    int r4 = r4 * r6
                    int r4 = r4 + 9
                    int r3 = r3.length
                    com.Utils.PrinterModel$DataBean r6 = r5
                    int r6 = r6.getPaperNum()
                    int r6 = r6 + -2
                    int r3 = r3 * r6
                    int r4 = r4 + r3
                    byte[] r3 = com.lib.blueUtils.BluetoothOrder.printLattice
                    int r3 = r3.length
                    int r4 = r4 + r3
                    byte[] r3 = com.lib.blueUtils.BluetoothOrder.finishLattice
                    int r3 = r3.length
                    int r4 = r4 + r3
                    int r4 = r4 + 9
                    byte[] r3 = com.lib.blueUtils.BluetoothOrder.getDevState
                    int r3 = r3.length
                    int r4 = r4 + r3
                    byte[] r3 = new byte[r4]
                L_0x0109:
                    int r4 = r7
                    byte[] r4 = com.lib.blueUtils.BluetoothOrder.getBlackening(r4)
                    int r6 = r4.length
                    java.lang.System.arraycopy(r4, r2, r3, r2, r6)
                    int r4 = r4.length
                    r6 = r2
                L_0x0115:
                    int r7 = 1
                    r8 = 300(0x12c, float:4.2E-43)
                    r9 = 200(0xc8, float:2.8E-43)
                    if (r6 >= r7) goto L_0x01fd
                    com.Utils.PrinterModel$DataBean r7 = r5
                    java.lang.String r7 = r7.getModelNo()
                    boolean r7 = r5.equals(r7)
                    if (r7 == 0) goto L_0x0147
                    com.lib.blueUtils.PrintDataUtils r7 = com.lib.blueUtils.PrintDataUtils.this
                    boolean r7 = r7.isCanPrintLabel()
                    if (r7 == 0) goto L_0x013c
                    byte[] r7 = com.lib.blueUtils.PrintDataUtils.checkLabelLocation()
                    int r10 = r7.length
                    java.lang.System.arraycopy(r7, r2, r3, r4, r10)
                    int r7 = r7.length
                L_0x013a:
                    int r4 = r4 + r7
                    goto L_0x0167
                L_0x013c:
                    r7 = 4
                    byte[] r7 = com.lib.blueUtils.PrintDataUtils.paperLe(r7)
                    int r10 = r7.length
                    java.lang.System.arraycopy(r7, r2, r3, r4, r10)
                    int r7 = r7.length
                    goto L_0x013a
                L_0x0147:
                    com.lib.blueUtils.PrintDataUtils r7 = com.lib.blueUtils.PrintDataUtils.this
                    boolean r7 = r7.isCanPrintLabel()
                    if (r7 == 0) goto L_0x0167
                    com.Utils.PrinterModel$DataBean r7 = r5
                    int r7 = r7.getLabelUpPaperNum()
                    if (r7 == 0) goto L_0x0167
                    com.Utils.PrinterModel$DataBean r7 = r5
                    int r7 = r7.getLabelUpPaperNum()
                    byte[] r7 = com.lib.blueUtils.PrintDataUtils.paperLeCheckBlack(r7)
                    int r10 = r7.length
                    java.lang.System.arraycopy(r7, r2, r3, r4, r10)
                    int r7 = r7.length
                    goto L_0x013a
                L_0x0167:
                    java.util.Iterator r7 = r0.iterator()
                L_0x016b:
                    boolean r10 = r7.hasNext()
                    if (r10 == 0) goto L_0x017e
                    java.lang.Object r10 = r7.next()
                    byte[] r10 = (byte[]) r10
                    int r11 = r10.length
                    java.lang.System.arraycopy(r10, r2, r3, r4, r11)
                    int r10 = r10.length
                    int r4 = r4 + r10
                    goto L_0x016b
                L_0x017e:
                    com.Utils.PrinterModel$DataBean r7 = r5
                    boolean r7 = r7.isAutoLabelCheck()
                    if (r7 == 0) goto L_0x019b
                    com.lib.blueUtils.PrintDataUtils r7 = com.lib.blueUtils.PrintDataUtils.this
                    boolean r7 = r7.isCanPrintLabel()
                    if (r7 == 0) goto L_0x019b
                    r7 = 500(0x1f4, float:7.0E-43)
                    byte[] r7 = com.lib.blueUtils.PrintDataUtils.paperLeCheckBlack(r7)
                    int r8 = r7.length
                    java.lang.System.arraycopy(r7, r2, r3, r4, r8)
                    int r7 = r7.length
                L_0x0199:
                    int r4 = r4 + r7
                    goto L_0x01f9
                L_0x019b:
                    r7 = 25
                    byte[] r7 = com.lib.blueUtils.PrintDataUtils.feedPaper(r7)
                    int r10 = r7.length
                    java.lang.System.arraycopy(r7, r2, r3, r4, r10)
                    int r7 = r7.length
                    int r4 = r4 + r7
                    com.lib.blueUtils.PrintDataUtils r7 = com.lib.blueUtils.PrintDataUtils.this
                    boolean r7 = r7.isCanPrintLabel()
                    if (r7 != 0) goto L_0x01f9
                    com.Utils.PrinterModel$DataBean r7 = r5
                    int r7 = r7.getDevdpi()
                    if (r7 != r9) goto L_0x01be
                    byte[] r7 = com.lib.blueUtils.BluetoothOrder.paper
                    int r10 = r7.length
                    java.lang.System.arraycopy(r7, r2, r3, r4, r10)
                    goto L_0x01d3
                L_0x01be:
                    com.Utils.PrinterModel$DataBean r7 = r5
                    int r7 = r7.getDevdpi()
                    if (r7 != r8) goto L_0x01cd
                    byte[] r7 = com.lib.blueUtils.BluetoothOrder.paper_300dpi
                    int r10 = r7.length
                    java.lang.System.arraycopy(r7, r2, r3, r4, r10)
                    goto L_0x01d3
                L_0x01cd:
                    byte[] r7 = com.lib.blueUtils.BluetoothOrder.paper
                    int r10 = r7.length
                    java.lang.System.arraycopy(r7, r2, r3, r4, r10)
                L_0x01d3:
                    byte[] r7 = com.lib.blueUtils.BluetoothOrder.paper
                    int r10 = r7.length
                    int r4 = r4 + r10
                    com.Utils.PrinterModel$DataBean r10 = r5
                    int r10 = r10.getDevdpi()
                    if (r10 != r9) goto L_0x01e4
                    int r8 = r7.length
                    java.lang.System.arraycopy(r7, r2, r3, r4, r8)
                    goto L_0x01f7
                L_0x01e4:
                    com.Utils.PrinterModel$DataBean r9 = r5
                    int r9 = r9.getDevdpi()
                    if (r9 != r8) goto L_0x01f3
                    byte[] r8 = com.lib.blueUtils.BluetoothOrder.paper_300dpi
                    int r9 = r8.length
                    java.lang.System.arraycopy(r8, r2, r3, r4, r9)
                    goto L_0x01f7
                L_0x01f3:
                    int r8 = r7.length
                    java.lang.System.arraycopy(r7, r2, r3, r4, r8)
                L_0x01f7:
                    int r7 = r7.length
                    goto L_0x0199
                L_0x01f9:
                    int r6 = r6 + 1
                    goto L_0x0115
                L_0x01fd:
                    com.lib.blueUtils.PrintDataUtils r5 = com.lib.blueUtils.PrintDataUtils.this
                    boolean r5 = r5.isCanPrintLabel()
                    if (r5 == 0) goto L_0x021e
                    com.Utils.PrinterModel$DataBean r5 = r5
                    boolean r5 = r5.isAutoLabelCheck()
                    if (r5 != 0) goto L_0x0254
                    com.Utils.PrinterModel$DataBean r5 = r5
                    java.lang.String r5 = r5.getModelNo()
                    byte[] r5 = com.lib.blueUtils.BluetoothOrder.getDevInfo(r5)
                    int r6 = r5.length
                    java.lang.System.arraycopy(r5, r2, r3, r4, r6)
                    int r5 = r5.length
                    int r4 = r4 + r5
                    goto L_0x0254
                L_0x021e:
                    r5 = r2
                L_0x021f:
                    com.Utils.PrinterModel$DataBean r6 = r5
                    int r6 = r6.getPaperNum()
                    int r6 = r6 + -2
                    if (r5 >= r6) goto L_0x0254
                    com.Utils.PrinterModel$DataBean r6 = r5
                    int r6 = r6.getDevdpi()
                    if (r6 != r9) goto L_0x0238
                    byte[] r6 = com.lib.blueUtils.BluetoothOrder.paper
                    int r7 = r6.length
                    java.lang.System.arraycopy(r6, r2, r3, r4, r7)
                    goto L_0x024d
                L_0x0238:
                    com.Utils.PrinterModel$DataBean r6 = r5
                    int r6 = r6.getDevdpi()
                    if (r6 != r8) goto L_0x0247
                    byte[] r6 = com.lib.blueUtils.BluetoothOrder.paper_300dpi
                    int r7 = r6.length
                    java.lang.System.arraycopy(r6, r2, r3, r4, r7)
                    goto L_0x024d
                L_0x0247:
                    byte[] r6 = com.lib.blueUtils.BluetoothOrder.paper
                    int r7 = r6.length
                    java.lang.System.arraycopy(r6, r2, r3, r4, r7)
                L_0x024d:
                    byte[] r6 = com.lib.blueUtils.BluetoothOrder.paper
                    int r6 = r6.length
                    int r4 = r4 + r6
                    int r5 = r5 + 1
                    goto L_0x021f
                L_0x0254:
                    byte[] r5 = com.lib.blueUtils.BluetoothOrder.getDevState
                    int r6 = r5.length
                    java.lang.System.arraycopy(r5, r2, r3, r4, r6)
                    int r2 = r5.length
                    r1.clear()
                    java.util.ArrayList r1 = r2
                    r1.clear()
                    r0.clear()
                    com.lib.Utils.EventBusUtils r0 = com.lib.Utils.EventBusUtils.getInstance()
                    java.lang.String r1 = com.lib.Utils.Code.STARTPRINT
                    r0.post((java.lang.String) r1, (java.lang.Object) r3)
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.lib.blueUtils.PrintDataUtils.AnonymousClass1.run():void");
            }
        }).start();
    }

    public void GrayScaleBitmapData(Context context, ArrayList<PrintBean> arrayList, int i, int i2, int i3, boolean z) {
        if (arrayList.size() == 0) {
            EventBusUtils.getInstance().post("onWriteFailed");
            return;
        }
        final PrinterModel.DataBean printerModel = PrinterModelUtils.getPrinterModel();
        this.concentration = i3;
        LogUtils.e("concentration---" + this.concentration);
        this.newCompress = z;
        this.dataIdentify = new ArrayList();
        final Context context2 = context;
        final ArrayList<PrintBean> arrayList2 = arrayList;
        final int i4 = i;
        final int i5 = i3;
        new Thread(new Runnable(1) {
            public void run() {
                byte[] bArr;
                ArrayList arrayList = new ArrayList();
                Vector vector = new Vector();
                vector.addAll(arrayList2);
                int i = 0;
                for (int i2 = 0; i2 < vector.size(); i2++) {
                    PrintBean printBean = (PrintBean) vector.get(i2);
                    if (printBean.isShow() && printBean.getBitmap() != null) {
                        byte[] r6 = PrintDataUtils.this.eachLinePixToCmdGray(ImageDisposeUtil.convert4ColorImageToPointData(context2, printBean.getBitmap()), i4, printBean.getPrintType(), i5);
                        if (printBean.isAddWhite()) {
                            byte[] bArr2 = BluetoothOrder.paper;
                            i += bArr2.length;
                            arrayList.add(bArr2);
                        }
                        i += r6.length;
                        arrayList.add(r6);
                    }
                }
                if (PrintDataUtils.this.eneragyList != null) {
                    PrintDataUtils.this.eneragyList.clear();
                }
                if (PrintDataUtils.this.isCanPrintLabel()) {
                    byte[] bArr3 = BluetoothOrder.paper;
                    bArr = new byte[(((i + (bArr3.length * 2)) * 1) + 9 + (bArr3.length * (printerModel.getPaperNum() - 2)) + 9)];
                } else {
                    byte[] bArr4 = BluetoothOrder.paper;
                    bArr = new byte[(((i + (bArr4.length * 2)) * 1) + (bArr4.length * (printerModel.getPaperNum() - 2)) + 9 + BluetoothOrder.getDevState.length + 9)];
                }
                byte[] blackening = BluetoothOrder.getBlackening(i5);
                System.arraycopy(blackening, 0, bArr, 0, blackening.length);
                int length = blackening.length;
                for (int i3 = 0; i3 < 1; i3++) {
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        byte[] bArr5 = (byte[]) it.next();
                        System.arraycopy(bArr5, 0, bArr, length, bArr5.length);
                        length += bArr5.length;
                    }
                    byte[] feedPaper = PrintDataUtils.feedPaper(15);
                    System.arraycopy(feedPaper, 0, bArr, length, feedPaper.length);
                    length += feedPaper.length;
                    if (!PrintDataUtils.this.isCanPrintLabel()) {
                        if (printerModel.getDevdpi() == 200) {
                            byte[] bArr6 = BluetoothOrder.paper;
                            System.arraycopy(bArr6, 0, bArr, length, bArr6.length);
                        } else if (printerModel.getDevdpi() == 300) {
                            byte[] bArr7 = BluetoothOrder.paper_300dpi;
                            System.arraycopy(bArr7, 0, bArr, length, bArr7.length);
                        } else {
                            byte[] bArr8 = BluetoothOrder.paper;
                            System.arraycopy(bArr8, 0, bArr, length, bArr8.length);
                        }
                        byte[] bArr9 = BluetoothOrder.paper;
                        int length2 = length + bArr9.length;
                        if (printerModel.getDevdpi() == 200) {
                            System.arraycopy(bArr9, 0, bArr, length2, bArr9.length);
                        } else if (printerModel.getDevdpi() == 300) {
                            byte[] bArr10 = BluetoothOrder.paper_300dpi;
                            System.arraycopy(bArr10, 0, bArr, length2, bArr10.length);
                        } else {
                            System.arraycopy(bArr9, 0, bArr, length2, bArr9.length);
                        }
                        length = length2 + bArr9.length;
                    }
                }
                if (!PrintDataUtils.this.isCanPrintLabel()) {
                    for (int i4 = 0; i4 < printerModel.getPaperNum() - 2; i4++) {
                        if (printerModel.getDevdpi() == 200) {
                            byte[] bArr11 = BluetoothOrder.paper;
                            System.arraycopy(bArr11, 0, bArr, length, bArr11.length);
                        } else if (printerModel.getDevdpi() == 300) {
                            byte[] bArr12 = BluetoothOrder.paper_300dpi;
                            System.arraycopy(bArr12, 0, bArr, length, bArr12.length);
                        } else {
                            byte[] bArr13 = BluetoothOrder.paper;
                            System.arraycopy(bArr13, 0, bArr, length, bArr13.length);
                        }
                        length += BluetoothOrder.paper.length;
                    }
                    byte[] bArr14 = BluetoothOrder.getDevState;
                    System.arraycopy(bArr14, 0, bArr, length, bArr14.length);
                    int length3 = bArr14.length;
                }
                vector.clear();
                arrayList2.clear();
                arrayList.clear();
                EventBusUtils.getInstance().post(Code.STARTPRINT, (Object) bArr);
            }
        }).start();
    }

    /* access modifiers changed from: private */
    public byte[] eachLinePixToCmdGray(byte[] bArr, int i, int i2, int i3) {
        int i4;
        byte[] bArr2;
        int length = bArr.length / i;
        PrinterModel.DataBean printerModel = PrinterModelUtils.getPrinterModel();
        int eneragy2 = getEneragy(i2, true);
        if (eneragy2 != 0) {
            bArr2 = new byte[(bArr.length + BluetoothOrder.print_img_gray_16.length + 19 + BluetoothOrder.paper.length)];
            i4 = 10;
            byte[] bArr3 = new byte[10];
            byte[] hexString2Bytes = ConvertUtils.hexString2Bytes(Integer.toHexString(eneragy2));
            byte b = hexString2Bytes.length >= 2 ? hexString2Bytes[1] : 0;
            bArr3[0] = 81;
            bArr3[1] = TarConstants.LF_PAX_EXTENDED_HEADER_LC;
            bArr3[2] = -81;
            bArr3[3] = 0;
            bArr3[4] = 2;
            bArr3[5] = 0;
            bArr3[6] = b;
            bArr3[7] = hexString2Bytes[0];
            bArr3[8] = BluetoothOrder.calcCrc8(bArr3, 6, 2);
            bArr3[9] = -1;
            System.arraycopy(bArr3, 0, bArr2, 0, 10);
        } else {
            bArr2 = new byte[(bArr.length + BluetoothOrder.print_img_gray_16.length + 9 + BluetoothOrder.paper.length)];
            i4 = 0;
        }
        byte[] bArr4 = BluetoothOrder.print_img_gray_16;
        System.arraycopy(bArr4, 0, bArr2, i4, bArr4.length);
        int length2 = i4 + bArr4.length;
        int i5 = this.textSpeed;
        if (i5 == 0) {
            i5 = printerModel.getGrayImageSpeed();
        }
        LogUtils.e("灰阶：打印浓度---" + eneragy2, "speed-----" + i5);
        byte[] feedPaper = feedPaper(i5);
        System.arraycopy(feedPaper, 0, bArr2, length2, feedPaper.length);
        System.arraycopy(bArr, 0, bArr2, length2 + 9, bArr.length);
        return bArr2;
    }

    public byte[] checkBleSpeed() {
        int imgMTU = (PrinterModelUtils.getPrinterModel().getImgMTU() - 3) * 80;
        byte[] bArr = new byte[imgMTU];
        for (int i = 0; i < imgMTU; i++) {
            bArr[i] = 0;
        }
        return bArr;
    }

    public void EightBitmapToData(Context context, ArrayList<PrintBean> arrayList, int i, int i2, int i3, boolean z) {
        if (arrayList.size() == 0) {
            EventBusUtils.getInstance().post("onWriteFailed");
            return;
        }
        final PrinterModel.DataBean printerModel = PrinterModelUtils.getPrinterModel();
        this.concentration = i3;
        LogUtils.e("concentration---" + this.concentration);
        this.newCompress = z;
        this.dataIdentify = new ArrayList();
        final Context context2 = context;
        final ArrayList<PrintBean> arrayList2 = arrayList;
        final int i4 = i;
        final int i5 = i2;
        final int i6 = i3;
        new Thread(new Runnable() {
            /* JADX WARNING: Removed duplicated region for block: B:43:0x014f  */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                    r11 = this;
                    java.util.ArrayList r0 = new java.util.ArrayList
                    r0.<init>()
                    java.util.Vector r1 = new java.util.Vector
                    r1.<init>()
                    java.util.ArrayList r2 = r2
                    r1.addAll(r2)
                    r2 = 0
                    r3 = r2
                L_0x0011:
                    int r4 = r1.size()
                    r5 = 1
                    if (r3 >= r4) goto L_0x015d
                    java.lang.Object r4 = r1.get(r3)
                    com.lib.Beans.PrintBean r4 = (com.lib.Beans.PrintBean) r4
                    boolean r6 = r4.isShow()
                    if (r6 == 0) goto L_0x0159
                    android.graphics.Bitmap r6 = r4.getBitmap()
                    if (r6 == 0) goto L_0x0159
                    android.content.Context r6 = r3
                    android.graphics.Bitmap r7 = r4.getBitmap()
                    int r8 = r4.getPrintType()
                    byte[] r6 = com.lib.blueUtils.PrintDataUtils.bitmapToBWPix(r6, r7, r8)
                    int r7 = r4
                    com.Utils.PrinterModel$DataBean r8 = r5
                    int r8 = r8.getSize()
                    r9 = 8
                    if (r9 != r8) goto L_0x0051
                    com.Utils.PrinterModel$DataBean r8 = r5
                    boolean r8 = com.lib.Utils.DeviceUtils.addMorPix((com.Utils.PrinterModel.DataBean) r8)
                    if (r8 == 0) goto L_0x0051
                    int r8 = com.lib.blueUtils.PrintDataUtils.getLeftPixelNum()
                    int r7 = r7 + r8
                L_0x0051:
                    com.lib.blueUtils.PrintDataUtils r8 = com.lib.blueUtils.PrintDataUtils.this
                    int r9 = r4.getPrintType()
                    byte[] r6 = r8.eachLinePixToCmdB(r6, r7, r9)
                    boolean r7 = r4.isAddWhite()
                    if (r7 == 0) goto L_0x0065
                    byte[] r7 = com.lib.blueUtils.BluetoothOrder.paper
                    int r7 = r7.length
                    goto L_0x0066
                L_0x0065:
                    r7 = r2
                L_0x0066:
                    int r8 = r6.length
                    int r7 = r7 + r8
                    com.Utils.PrinterModel$DataBean r8 = r5
                    java.lang.String r8 = r8.getModelNo()
                    java.lang.String r9 = "Professional Printer"
                    boolean r8 = r9.equals(r8)
                    if (r8 == 0) goto L_0x008f
                    int r7 = r7 + 9
                    byte[] r8 = com.lib.blueUtils.BluetoothOrder.paper
                    int r8 = r8.length
                    int r7 = r7 + r8
                    int r7 = r7 + r5
                    byte[] r5 = com.lib.blueUtils.BluetoothOrder.getDevState
                    int r5 = r5.length
                    int r7 = r7 + r5
                    int r7 = r7 + 9
                    byte[] r5 = new byte[r7]
                    byte[] r7 = com.lib.blueUtils.PrintDataUtils.stopPrintByte()
                    int r8 = r7.length
                    java.lang.System.arraycopy(r7, r2, r5, r2, r8)
                    int r7 = r7.length
                    goto L_0x009d
                L_0x008f:
                    int r7 = r7 + 9
                    byte[] r8 = com.lib.blueUtils.BluetoothOrder.paper
                    int r8 = r8.length
                    int r7 = r7 + r8
                    int r7 = r7 + r5
                    byte[] r5 = com.lib.blueUtils.BluetoothOrder.getDevState
                    int r5 = r5.length
                    int r7 = r7 + r5
                    byte[] r5 = new byte[r7]
                    r7 = r2
                L_0x009d:
                    android.graphics.Bitmap r8 = r4.getBitmap()
                    int r8 = r8.getHeight()
                    int r9 = r6
                    byte[] r9 = com.lib.blueUtils.BluetoothOrder.getBlackening(r9)
                    int r10 = r9.length
                    java.lang.System.arraycopy(r9, r2, r5, r7, r10)
                    int r9 = r9.length
                    int r7 = r7 + r9
                    int r9 = r6.length
                    java.lang.System.arraycopy(r6, r2, r5, r7, r9)
                    int r6 = r6.length
                    int r7 = r7 + r6
                    com.blankj.utilcode.util.SPUtils r6 = com.blankj.utilcode.util.SPUtils.getInstance()
                    java.lang.String r9 = com.lib.Utils.Code.EIGHTPAPER
                    java.lang.String r6 = r6.getString(r9)
                    com.blankj.utilcode.util.SPUtils r9 = com.blankj.utilcode.util.SPUtils.getInstance()
                    java.lang.String r10 = com.lib.Utils.Code.paperWidth
                    java.lang.String r9 = r9.getString(r10)
                    boolean r10 = com.blankj.utilcode.util.StringUtils.isEmpty(r9)
                    if (r10 != 0) goto L_0x00db
                    java.lang.String r10 = com.lib.Utils.Code.m210
                    boolean r9 = r10.equals(r9)
                    if (r9 != 0) goto L_0x00db
                    java.lang.String r6 = com.lib.Utils.Code.EIGHT_JUAN
                L_0x00db:
                    com.Utils.PrinterModel$DataBean r9 = r5
                    boolean r9 = r9.isA4XII()
                    if (r9 == 0) goto L_0x0104
                    java.lang.String r4 = com.lib.Utils.Code.EIGHT_A4
                    boolean r4 = r4.equals(r6)
                    if (r4 == 0) goto L_0x00f8
                    r4 = 500(0x1f4, float:7.0E-43)
                    byte[] r4 = com.lib.blueUtils.PrintDataUtils.paperLeCheckBlack(r4)
                    int r6 = r4.length
                    java.lang.System.arraycopy(r4, r2, r5, r7, r6)
                    int r4 = r4.length
                L_0x00f6:
                    int r7 = r7 + r4
                    goto L_0x0147
                L_0x00f8:
                    r4 = 100
                    byte[] r4 = com.lib.blueUtils.PrintDataUtils.paperLeCheckBlack(r4)
                    int r6 = r4.length
                    java.lang.System.arraycopy(r4, r2, r5, r7, r6)
                    int r4 = r4.length
                    goto L_0x00f6
                L_0x0104:
                    java.lang.String r9 = com.lib.Utils.Code.EIGHT_A4
                    boolean r6 = r9.equals(r6)
                    if (r6 == 0) goto L_0x011d
                    com.Utils.PrinterModel$DataBean r4 = r5
                    int r4 = com.lib.blueUtils.PrinterModelUtils.getEightMaxHigh(r4)
                    int r4 = r4 - r8
                    byte[] r4 = com.lib.blueUtils.PrintDataUtils.paperLeCheckBlack(r4)
                    int r6 = r4.length
                    java.lang.System.arraycopy(r4, r2, r5, r7, r6)
                    int r4 = r4.length
                    goto L_0x00f6
                L_0x011d:
                    boolean r4 = r4.isEightUnPaper()
                    if (r4 != 0) goto L_0x0147
                    com.Utils.PrinterModel$DataBean r4 = r5
                    int r4 = r4.getPaperNum()
                    int r4 = r4 * 48
                    com.Utils.PrinterModel$DataBean r6 = r5
                    int r6 = r6.getDevdpi()
                    r8 = 300(0x12c, float:4.2E-43)
                    if (r6 != r8) goto L_0x013d
                    com.Utils.PrinterModel$DataBean r4 = r5
                    int r4 = r4.getPaperNum()
                    int r4 = r4 * 72
                L_0x013d:
                    byte[] r4 = com.lib.blueUtils.PrintDataUtils.paperLeCheckBlack(r4)
                    int r6 = r4.length
                    java.lang.System.arraycopy(r4, r2, r5, r7, r6)
                    int r4 = r4.length
                    goto L_0x00f6
                L_0x0147:
                    com.Utils.PrinterModel$DataBean r4 = r5
                    boolean r4 = r4.isCorePrint()
                    if (r4 != 0) goto L_0x0156
                    byte[] r4 = com.lib.blueUtils.BluetoothOrder.getDevState
                    int r6 = r4.length
                    java.lang.System.arraycopy(r4, r2, r5, r7, r6)
                    int r4 = r4.length
                L_0x0156:
                    r0.add(r5)
                L_0x0159:
                    int r3 = r3 + 1
                    goto L_0x0011
                L_0x015d:
                    com.lib.blueUtils.PrintDataUtils r3 = com.lib.blueUtils.PrintDataUtils.this
                    java.util.ArrayList r3 = r3.eneragyList
                    if (r3 == 0) goto L_0x016e
                    com.lib.blueUtils.PrintDataUtils r3 = com.lib.blueUtils.PrintDataUtils.this
                    java.util.ArrayList r3 = r3.eneragyList
                    r3.clear()
                L_0x016e:
                    int r3 = r7
                    if (r3 <= r5) goto L_0x01a7
                    java.util.ArrayList r3 = new java.util.ArrayList
                    r3.<init>()
                    r4 = r2
                L_0x0178:
                    int r5 = r7
                    if (r4 >= r5) goto L_0x0192
                    r5 = r2
                L_0x017d:
                    int r6 = r0.size()
                    if (r5 >= r6) goto L_0x018f
                    java.lang.Object r6 = r0.get(r5)
                    byte[] r6 = (byte[]) r6
                    r3.add(r6)
                    int r5 = r5 + 1
                    goto L_0x017d
                L_0x018f:
                    int r4 = r4 + 1
                    goto L_0x0178
                L_0x0192:
                    r0.clear()
                    r1.clear()
                    java.util.ArrayList r0 = r2
                    r0.clear()
                    com.lib.Utils.EventBusUtils r0 = com.lib.Utils.EventBusUtils.getInstance()
                    java.lang.String r1 = com.lib.Utils.Code.EIGHT_STARTPRINT
                    r0.post((java.lang.String) r1, (java.lang.Object) r3)
                    return
                L_0x01a7:
                    com.lib.Utils.EventBusUtils r1 = com.lib.Utils.EventBusUtils.getInstance()
                    java.lang.String r2 = com.lib.Utils.Code.EIGHT_STARTPRINT
                    r1.post((java.lang.String) r2, (java.lang.Object) r0)
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.lib.blueUtils.PrintDataUtils.AnonymousClass3.run():void");
            }
        }).start();
    }

    public static byte[] bitmapToBWPix(Context context, Bitmap bitmap, int i) {
        if (Code.TEXT_PRINT_TYPE == i) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int[] iArr = new int[(width * height)];
            Bitmap bitmap2 = bitmap;
            bitmap2.getPixels(iArr, 0, width, 0, 0, width, height);
            return format_K_threshold(iArr, bitmap2.getWidth(), bitmap2.getHeight(), true);
        }
        Bitmap bitmap3 = bitmap;
        return format_K_threshold(ImageDisposeUtil.convertGreyImgByFloydPixels(context, bitmap3), bitmap3.getWidth(), bitmap3.getHeight(), false);
    }

    public static int getLeftPixelNum() {
        PrinterModel.DataBean printerModel = PrinterModelUtils.getPrinterModel();
        if (!printerModel.isAddMorPix()) {
            return 0;
        }
        if (printerModel.getAddMorePixNum() >= 0) {
            return printerModel.getAddMorePixNum();
        }
        return ("X8-W".startsWith(printerModel.getModelNo()) || "X8-L".startsWith(printerModel.getModelNo())) ? 40 : 64;
    }

    public static byte[] format_K_threshold(int[] iArr, int i, int i2, boolean z) {
        byte[] bArr;
        int i3;
        int i4;
        PrinterModel.DataBean printerModel = PrinterModelUtils.getPrinterModel();
        int leftPixelNum = getLeftPixelNum();
        if (8 != printerModel.getSize() || !DeviceUtils.addMorPix(printerModel)) {
            bArr = new byte[(i * i2)];
        } else {
            bArr = new byte[((i + leftPixelNum) * i2)];
        }
        if (z) {
            i3 = 0;
            int i5 = 0;
            for (int i6 = 0; i6 < i2; i6++) {
                for (int i7 = 0; i7 < i; i7++) {
                    i3 += iArr[i5] & 255;
                    i5++;
                }
            }
        } else {
            i3 = 0;
            int i8 = 0;
            for (int i9 = 0; i9 < i2; i9++) {
                for (int i10 = 0; i10 < i; i10++) {
                    i3 += iArr[i8];
                    i8++;
                }
            }
        }
        int i11 = ((i3 / i2) / i) - 13;
        int i12 = 0;
        int i13 = 0;
        for (int i14 = 0; i14 < i2; i14++) {
            if (8 == printerModel.getSize() && DeviceUtils.addMorPix(printerModel)) {
                for (int i15 = 0; i15 < leftPixelNum; i15++) {
                    bArr[i12] = 0;
                    i12++;
                }
            }
            for (int i16 = 0; i16 < i; i16++) {
                if (z) {
                    i4 = iArr[i13] & 255;
                } else {
                    i4 = iArr[i13];
                }
                if (i4 == 0) {
                    bArr[i12] = 1;
                } else if (i4 > i11) {
                    bArr[i12] = 0;
                } else {
                    bArr[i12] = 1;
                }
                i12++;
                i13++;
            }
        }
        return bArr;
    }

    /* access modifiers changed from: private */
    public byte[] eachLinePixToCmdB(byte[] bArr, int i, int i2) {
        int i3;
        int i4;
        byte[] bArr2;
        int i5;
        int i6;
        int i7;
        byte b;
        byte[] bArr3;
        int i8;
        int i9;
        byte b2;
        char c;
        int i10;
        byte b3;
        byte b4;
        PrintDataUtils printDataUtils = this;
        int i11 = i;
        int i12 = i2;
        int length = bArr.length / i11;
        int i13 = i11 / 8;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        PrinterModel.DataBean printerModel = PrinterModelUtils.getPrinterModel();
        printerModel.getModelNo();
        if ("Professional Printer".equals(printerModel.getModelNo())) {
            int i14 = printDataUtils.concentration;
            i3 = i14 == 4 ? printerModel.getModerationEneragy() : i14 > 4 ? printerModel.getDeepenEneragy() : printerModel.getThinEneragy();
        } else {
            i3 = printDataUtils.getEneragy(i12, false);
            if (printDataUtils.isCanPrintLabel() && !"U1".equals(printerModel.getModelNo())) {
                i3 = printDataUtils.getEneragy(Code.IMG_PRINT_TYPE, false) * 2;
            }
        }
        int i15 = length >= 200 ? length / 200 : 0;
        if (SPUtils.getInstance().getBoolean(Code.EIGHT_TATTOO, false) && printerModel.isTattooPaper() && !printerModel.isA4XII()) {
            i3 = printDataUtils.getTattooEnergy(printerModel);
        }
        byte b5 = -1;
        if (i3 != 0) {
            i4 = 1;
            bArr2 = new byte[((i11 * length) + BluetoothOrder.print_text.length + ((i15 + 1) * 9) + 10)];
            byte[] bArr4 = new byte[10];
            byte[] hexString2Bytes = ConvertUtils.hexString2Bytes(Integer.toHexString(i3));
            byte b6 = hexString2Bytes.length >= 2 ? hexString2Bytes[1] : 0;
            bArr4[0] = 81;
            bArr4[1] = TarConstants.LF_PAX_EXTENDED_HEADER_LC;
            bArr4[2] = -81;
            bArr4[3] = 0;
            bArr4[4] = 2;
            bArr4[5] = 0;
            bArr4[6] = b6;
            bArr4[7] = hexString2Bytes[0];
            bArr4[8] = BluetoothOrder.calcCrc8(bArr4, 6, 2);
            bArr4[9] = -1;
            System.arraycopy(bArr4, 0, bArr2, 0, 10);
            LogUtils.e("eneragy1", ByteUtils.BinaryToHexString(bArr4));
            i5 = 10;
        } else {
            i4 = 1;
            bArr2 = new byte[((i11 * length) + BluetoothOrder.print_text.length + ((i15 + 1) * 9))];
            i5 = 0;
        }
        if (SPUtils.getInstance().getBoolean(Code.EIGHT_TATTOO, false) && printerModel.isTattooPaper()) {
            byte[] bArr5 = BluetoothOrder.print_tattoo;
            System.arraycopy(bArr5, 0, bArr2, i5, bArr5.length);
            i5 += bArr5.length;
            if (printDataUtils.getImgSpeed() == 0) {
                i6 = PrinterModelUtils.getTattooSpeed();
            } else {
                i6 = printDataUtils.getImgSpeed();
            }
        } else if (i12 == Code.IMG_PRINT_LABEL) {
            byte[] bArr6 = BluetoothOrder.print_Label;
            System.arraycopy(bArr6, 0, bArr2, i5, bArr6.length);
            i5 += bArr6.length;
            i6 = printDataUtils.getImgSpeed() == 0 ? PrinterModelUtils.getImgPrintSpeed() : printDataUtils.getImgSpeed();
        } else if (i12 == Code.TEXT_PRINT_TYPE) {
            byte[] bArr7 = BluetoothOrder.print_text;
            System.arraycopy(bArr7, 0, bArr2, i5, bArr7.length);
            i5 += bArr7.length;
            i6 = printDataUtils.getTextSpeed() == 0 ? PrinterModelUtils.getTextPrintSpeed() : printDataUtils.getTextSpeed();
        } else if (i12 == Code.IMG_PRINT_TYPE) {
            byte[] bArr8 = BluetoothOrder.print_img;
            System.arraycopy(bArr8, 0, bArr2, i5, bArr8.length);
            i5 += bArr8.length;
            i6 = printDataUtils.getImgSpeed() == 0 ? PrinterModelUtils.getImgPrintSpeed() : printDataUtils.getImgSpeed();
        } else {
            i6 = 0;
        }
        LogUtils.e("抽点：打印浓度---" + i3, "speed-----" + i6);
        byte[] feedPaper = feedPaper(i6);
        System.arraycopy(feedPaper, 0, bArr2, i5, feedPaper.length);
        int i16 = i5 + 9;
        SPUtils instance = SPUtils.getInstance();
        StringBuilder sb = new StringBuilder();
        sb.append(Code.lzo);
        sb.append(printerModel.getModelNo());
        PrinterModel.DataBean dataBean = printerModel;
        sb.append(SPUtils.getInstance().getString(Code.DEF_DEVICE_ADDRESS));
        if (instance.getBoolean(sb.toString(), false) || dataBean.isCorePrint()) {
            int i17 = dataBean.getDevdpi() == 300 ? 10 : 18;
            int i18 = length / i17;
            ArrayList arrayList3 = new ArrayList();
            int i19 = i17 * i13;
            int i20 = i13 * length;
            byte[] bArr9 = new byte[i20];
            int i21 = 0;
            int i22 = 0;
            for (int i23 = 0; i23 < length; i23++) {
                for (int i24 = 0; i24 < i13; i24++) {
                    if (dataBean.isA4XII()) {
                        i7 = p0[bArr[i21]] + p1[bArr[i21 + 1]] + p2[bArr[i21 + 2]] + p3[bArr[i21 + 3]] + p4[bArr[i21 + 4]] + p5[bArr[i21 + 5]] + p6[bArr[i21 + 6]];
                        b = bArr[i21 + 7];
                    } else {
                        i7 = p0[bArr[i21 + 7]] + p1[bArr[i21 + 6]] + p2[bArr[i21 + 5]] + p3[bArr[i21 + 4]] + p4[bArr[i21 + 3]] + p5[bArr[i21 + 2]] + p6[bArr[i21 + 1]];
                        b = bArr[i21];
                    }
                    i21 += 8;
                    bArr9[i22] = (byte) (i7 + b);
                    i22++;
                }
            }
            int i25 = 0;
            for (int i26 = 0; i26 < i18; i26++) {
                byte[] bArr10 = new byte[i19];
                System.arraycopy(bArr9, i26 * i19, bArr10, 0, i19);
                byte[] eightLZOData = ImageDisposeUtil.eightLZOData(bArr10);
                arrayList3.add(eightLZOData);
                i25 = i25 + eightLZOData.length + 9;
            }
            int i27 = i19 * i18;
            if (i27 < i20) {
                int i28 = i20 - i27;
                byte[] bArr11 = new byte[i28];
                System.arraycopy(bArr9, i27, bArr11, 0, i28);
                byte[] eightLZOData2 = ImageDisposeUtil.eightLZOData(bArr11);
                i25 += eightLZOData2.length;
                arrayList3.add(eightLZOData2);
                if (!"Professional Printer".equals(dataBean.getModelNo())) {
                    i25 += 9;
                }
            }
            byte[] bArr12 = new byte[(i25 + i16)];
            System.arraycopy(bArr2, 0, bArr12, 0, i16);
            Iterator it = arrayList3.iterator();
            while (it.hasNext()) {
                byte[] bArr13 = (byte[]) it.next();
                System.arraycopy(bArr13, 0, bArr12, i16, bArr13.length);
                int length2 = i16 + bArr13.length;
                if (!"Professional Printer".equals(dataBean.getModelNo())) {
                    System.arraycopy(feedPaper, 0, bArr12, length2, feedPaper.length);
                    length2 += feedPaper.length;
                }
            }
            return bArr12;
        }
        int i29 = 0;
        int i30 = 0;
        int i31 = 0;
        int i32 = 0;
        while (i29 < length) {
            i30 += 8;
            arrayList.clear();
            arrayList2.clear();
            int i33 = i16;
            int i34 = i29;
            int i35 = i3;
            if (printDataUtils.newCompress) {
                int i36 = 0;
                byte b7 = 0;
                i9 = 0;
                i8 = 0;
                while (true) {
                    if (i36 >= i11) {
                        b4 = b7;
                        break;
                    }
                    byte b8 = bArr[i31 + i36];
                    if (i36 == 0 || b7 == b8) {
                        i9++;
                    } else {
                        dataTrim(i9, b7, arrayList2);
                        i9 = i4;
                    }
                    if (b8 != 0 && i8 == 0) {
                        i8 = i4;
                    }
                    b4 = b7;
                    if (arrayList2.size() > i13) {
                        break;
                    }
                    if (!(i36 != i - 1 || i9 == 0 || i8 == 0)) {
                        dataTrim(i9, b8, arrayList2);
                        i9 = 0;
                    }
                    i36++;
                    b7 = b8;
                    i11 = i;
                }
                b2 = b4;
            } else {
                b2 = 0;
                i9 = 0;
                i8 = 0;
            }
            i31 += i;
            if (arrayList2.size() > i13 || !printDataUtils.newCompress) {
                arrayList.clear();
                i31 -= i;
                arrayList.add(Byte.valueOf(b5));
                for (int i37 = 0; i37 < i13; i37++) {
                    if (dataBean.isA4XII()) {
                        i10 = p0[bArr[i31]] + p1[bArr[i31 + 1]] + p2[bArr[i31 + 2]] + p3[bArr[i31 + 3]] + p4[bArr[i31 + 4]] + p5[bArr[i31 + 5]] + p6[bArr[i31 + 6]];
                        b3 = bArr[i31 + 7];
                    } else {
                        i10 = p0[bArr[i31 + 7]] + p1[bArr[i31 + 6]] + p2[bArr[i31 + 5]] + p3[bArr[i31 + 4]] + p4[bArr[i31 + 3]] + p5[bArr[i31 + 2]] + p6[bArr[i31 + 1]];
                        b3 = bArr[i31];
                    }
                    i31 += 8;
                    arrayList.add(Byte.valueOf((byte) (i10 + b3)));
                }
            } else {
                arrayList.addAll(arrayList2);
            }
            if (i8 == 0 && arrayList.size() == 0) {
                dataTrim(i9, b2, arrayList);
            }
            if (arrayList.size() > 0) {
                int size = arrayList.size();
                byte[] bArr14 = new byte[size];
                for (int i38 = 0; i38 < arrayList.size(); i38++) {
                    bArr14[i38] = ((Byte) arrayList.get(i38)).byteValue();
                }
                i30 += arrayList.size();
                String hexString = Integer.toHexString(size);
                bArr2[i33] = 81;
                bArr2[i33 + 1] = TarConstants.LF_PAX_EXTENDED_HEADER_LC;
                if (bArr14[0] == b5 && size == i13 + 1) {
                    bArr2[i33 + 2] = -94;
                    i30--;
                    hexString = Integer.toHexString(i13);
                } else {
                    bArr2[i33 + 2] = -65;
                }
                bArr2[i33 + 3] = 0;
                if (ConvertUtils.hexString2Bytes(hexString).length == i4) {
                    bArr2[i33 + 4] = ConvertUtils.hexString2Bytes(hexString)[0];
                    bArr2[i33 + 5] = 0;
                } else {
                    bArr2[i33 + 4] = ConvertUtils.hexString2Bytes(hexString)[1];
                    bArr2[i33 + 5] = ConvertUtils.hexString2Bytes(hexString)[0];
                }
                if (bArr14[0] == -1 && size == i13 + 1) {
                    System.arraycopy(bArr14, 1, bArr2, i33 + 6, size - 1);
                    c = 0;
                } else {
                    c = 0;
                    System.arraycopy(bArr14, 0, bArr2, i33 + 6, size);
                }
                if (bArr14[c] == -1 && size == i13 + 1) {
                    int i39 = i33 + 6;
                    bArr2[i39 + i13] = BluetoothOrder.calcCrc8(bArr2, i39, i13);
                    bArr2[i33 + 7 + i13] = -1;
                    i16 = i33 + 8 + i13;
                    b5 = -1;
                } else {
                    int i40 = i33 + 6;
                    bArr2[i40 + size] = BluetoothOrder.calcCrc8(bArr2, i40, size);
                    b5 = -1;
                    bArr2[i33 + 7 + size] = -1;
                    i16 = size + i33 + 8;
                }
            } else {
                i16 = i33;
            }
            int i41 = i32 + 1;
            if (!"Professional Printer".equals(dataBean.getModelNo())) {
                if (i41 >= 200) {
                    System.arraycopy(feedPaper, 0, bArr2, i16, feedPaper.length);
                    i16 += 9;
                    i32 = 0;
                    i29 = i34 + 1;
                    printDataUtils = this;
                    i11 = i;
                    i3 = i35;
                    i4 = 1;
                }
            }
            i32 = i41;
            i29 = i34 + 1;
            printDataUtils = this;
            i11 = i;
            i3 = i35;
            i4 = 1;
        }
        if (i3 != 0) {
            bArr3 = new byte[(i30 + BluetoothOrder.print_text.length + ((i15 + 1) * 9) + 10)];
        } else {
            bArr3 = new byte[(i30 + BluetoothOrder.print_text.length + ((i15 + 1) * 9))];
        }
        System.arraycopy(bArr2, 0, bArr3, 0, bArr3.length);
        return bArr3;
    }

    public static byte[] conver2HexToByte(String str) {
        String[] split = str.split(",");
        int length = split.length;
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            bArr[i] = Long.valueOf(split[i], 2).byteValue();
        }
        return bArr;
    }

    private static String toBinary(int i) {
        String str = "";
        while (i != 0) {
            str = (i % 2) + str;
            i /= 2;
        }
        return str;
    }

    private static void dataTrim(int i, byte b, ArrayList arrayList) {
        while (i > 127) {
            arrayList.add(Byte.valueOf(conver2HexToByte(b + toBinary(127))[0]));
            i += -127;
        }
        if (i > 0) {
            String binary = toBinary(i);
            String str = b + "";
            if (binary.length() < 8) {
                for (int i2 = 0; i2 < 7 - binary.length(); i2++) {
                    str = str + 0;
                }
            }
            arrayList.add(Byte.valueOf(conver2HexToByte(str + binary)[0]));
        }
    }

    public static byte[] feedPaper(int i) {
        byte[] bArr = new byte[9];
        bArr[0] = 81;
        bArr[1] = TarConstants.LF_PAX_EXTENDED_HEADER_LC;
        bArr[2] = -67;
        bArr[3] = 0;
        bArr[4] = 1;
        bArr[5] = 0;
        bArr[6] = ConvertUtils.hexString2Bytes(Integer.toHexString(i))[0];
        bArr[7] = BluetoothOrder.calcCrc8(bArr, 6, 1);
        bArr[8] = -1;
        return bArr;
    }

    public static byte[] checkLabelLocation() {
        return new byte[]{81, TarConstants.LF_PAX_EXTENDED_HEADER_LC, -84, 0, 2, 0, 100, 0, -95, -1, 81, TarConstants.LF_PAX_EXTENDED_HEADER_LC, -90, 0, 2, 0, 16, 2, 89, -1};
    }

    public static byte[] stopPrintByte() {
        return new byte[]{81, TarConstants.LF_PAX_EXTENDED_HEADER_LC, -90, 0, 1, 0, 5, 27, -1};
    }

    public static byte[] filledByte() {
        PrinterModel.DataBean printerModel = PrinterModelUtils.getPrinterModel();
        if (printerModel.getSize() == 8) {
            return paperLeCheckBlack(100);
        }
        byte[] bArr = new byte[(printerModel.getPaperSize() + (BluetoothOrder.paper.length * printerModel.getPaperNum()))];
        for (int i = 0; i < printerModel.getPaperSize(); i++) {
            bArr[i] = 0;
        }
        int paperSize = printerModel.getPaperSize();
        for (int i2 = 0; i2 < printerModel.getPaperNum(); i2++) {
            if (printerModel.getDevdpi() == 200) {
                byte[] bArr2 = BluetoothOrder.paper;
                System.arraycopy(bArr2, 0, bArr, paperSize, bArr2.length);
            } else if (printerModel.getDevdpi() == 300) {
                byte[] bArr3 = BluetoothOrder.paper_300dpi;
                System.arraycopy(bArr3, 0, bArr, paperSize, bArr3.length);
            } else {
                byte[] bArr4 = BluetoothOrder.paper;
                System.arraycopy(bArr4, 0, bArr, paperSize, bArr4.length);
            }
            paperSize += BluetoothOrder.paper.length;
        }
        return bArr;
    }

    public void LYPrintData(Context context, ArrayList<PrintBean> arrayList, int i, int i2) {
        final Context context2 = context;
        final ArrayList<PrintBean> arrayList2 = arrayList;
        final int i3 = i;
        final int i4 = i2;
        new Thread(new Runnable() {
            public void run() {
                ArrayList arrayList = new ArrayList();
                PrinterModel.DataBean printerModel = PrinterModelUtils.getPrinterModel();
                int i = 0;
                int i2 = 0;
                while (true) {
                    int i3 = 3;
                    if (i >= arrayList2.size()) {
                        break;
                    }
                    PrintBean printBean = (PrintBean) arrayList2.get(i);
                    Bitmap bitmap = printBean.getBitmap();
                    byte[] bitmapToBWPix = PrintDataUtils.bitmapToBWPix(context2, bitmap, printBean.getPrintType());
                    int height = bitmap.getHeight();
                    int i4 = i3;
                    int i5 = height / 24;
                    if (height % 24 != 0) {
                        i5++;
                        byte[] bArr = new byte[(i5 * 24 * i4)];
                        System.arraycopy(bitmapToBWPix, 0, bArr, 0, bitmapToBWPix.length);
                        bitmapToBWPix = bArr;
                    }
                    byte[] bArr2 = BluetoothOrder.new_print_text;
                    byte[] bArr3 = new byte[((bitmapToBWPix.length / 8) + (i5 * 9) + 5 + bArr2.length)];
                    bArr3[0] = 27;
                    bArr3[1] = 64;
                    bArr3[2] = 18;
                    bArr3[3] = 35;
                    if (printBean.getPrintType() == Code.TEXT_PRINT_TYPE) {
                        int textEneragy = printerModel.getTextEneragy();
                        int i6 = i4;
                        if (i6 == 2) {
                            bArr3[4] = ConvertUtils.hexString2Bytes(Integer.toHexString(textEneragy - 2))[0];
                        } else if (i6 == 3) {
                            bArr3[4] = ConvertUtils.hexString2Bytes(Integer.toHexString(textEneragy))[0];
                        } else if (i6 == 5) {
                            bArr3[4] = ConvertUtils.hexString2Bytes(Integer.toHexString(textEneragy + 2))[0];
                        }
                        System.arraycopy(bArr2, 0, bArr3, 5, bArr2.length);
                    } else {
                        bArr3[4] = ConvertUtils.hexString2Bytes(Integer.toHexString(PrintDataUtils.this.edtEnergy))[0];
                        byte[] bArr4 = BluetoothOrder.new_print_img;
                        System.arraycopy(bArr4, 0, bArr3, 5, bArr4.length);
                    }
                    int length = 5 + BluetoothOrder.new_print_img.length;
                    byte[] hexString2Bytes = ConvertUtils.hexString2Bytes(Integer.toHexString(i4));
                    int i7 = 0;
                    while (i7 < i5) {
                        bArr3[length] = 27;
                        bArr3[length + 1] = RefErrorPtg.sid;
                        bArr3[length + 2] = 33;
                        if (hexString2Bytes.length > 1) {
                            bArr3[length + 3] = hexString2Bytes[1];
                        } else {
                            bArr3[length + 3] = 0;
                        }
                        bArr3[length + 4] = hexString2Bytes[0];
                        int i8 = length + 5;
                        for (int i9 = 0; i9 < i4; i9++) {
                            int i10 = 0;
                            while (i10 < i3) {
                                int i11 = (i7 * 24 * i4) + (i10 * 8 * i4) + i9;
                                bArr3[i8] = (byte) (PrintDataUtils.p0[bitmapToBWPix[i11]] + PrintDataUtils.p1[bitmapToBWPix[i11 + i4]] + PrintDataUtils.p2[bitmapToBWPix[i11 + (i4 * 2)]] + PrintDataUtils.p3[bitmapToBWPix[i11 + (i4 * 3)]] + PrintDataUtils.p4[bitmapToBWPix[i11 + (i4 * 4)]] + PrintDataUtils.p5[bitmapToBWPix[i11 + (i4 * 5)]] + PrintDataUtils.p6[bitmapToBWPix[i11 + (i4 * 6)]] + bitmapToBWPix[i11 + (i4 * 7)]);
                                i8++;
                                i10++;
                                i3 = i3;
                            }
                            int i12 = i3;
                        }
                        bArr3[i8] = 27;
                        bArr3[i8 + 1] = TarConstants.LF_CHR;
                        bArr3[i8 + 2] = 0;
                        bArr3[i8 + 3] = 10;
                        length = i8 + 4;
                        i7++;
                        i3 = i3;
                    }
                    i2 += length;
                    arrayList.add(bArr3);
                    i++;
                }
                byte[] bArr5 = new byte[(i2 + BluetoothOrder.new_getDevState.length + 3)];
                Iterator it = arrayList.iterator();
                int i13 = 0;
                while (it.hasNext()) {
                    byte[] bArr6 = (byte[]) it.next();
                    System.arraycopy(bArr6, 0, bArr5, i13, bArr6.length);
                    i13 += bArr6.length;
                }
                bArr5[i13] = 27;
                bArr5[i13 + 1] = 100;
                if (printerModel.getDevdpi() == 300) {
                    bArr5[i13 + 2] = 4;
                } else {
                    bArr5[i13 + 2] = 3;
                }
                byte[] bArr7 = BluetoothOrder.new_getDevState;
                System.arraycopy(bArr7, 0, bArr5, i13 + 3, bArr7.length);
                EventBusUtils.getInstance().post(Code.STARTPRINT, (Object) bArr5);
            }
        }).start();
    }

    public void EightPrintData(Context context, ArrayList<PrintBean> arrayList, int i, int i2) {
        final Context context2 = context;
        final ArrayList<PrintBean> arrayList2 = arrayList;
        final int i3 = i;
        final int i4 = i2;
        new Thread(new Runnable() {
            public void run() {
                ArrayList arrayList = new ArrayList();
                PrinterModel.DataBean printerModel = PrinterModelUtils.getPrinterModel();
                for (int i = 0; i < arrayList2.size(); i++) {
                    PrintBean printBean = (PrintBean) arrayList2.get(i);
                    Bitmap bitmap = printBean.getBitmap();
                    byte[] bitmapToBWPix = PrintDataUtils.bitmapToBWPix(context2, bitmap, printBean.getPrintType());
                    int height = bitmap.getHeight();
                    int i2 = i3;
                    int i3 = height / 24;
                    if (height % 24 != 0) {
                        i3++;
                        byte[] bArr = new byte[(i3 * 24 * i2)];
                        System.arraycopy(bitmapToBWPix, 0, bArr, 0, bitmapToBWPix.length);
                        bitmapToBWPix = bArr;
                    }
                    byte[] bArr2 = BluetoothOrder.new_print_text;
                    byte[] bArr3 = new byte[((bitmapToBWPix.length / 8) + (i3 * 9) + 5 + bArr2.length + BluetoothOrder.new_getDevState.length + 3)];
                    bArr3[0] = 27;
                    bArr3[1] = 64;
                    bArr3[2] = 18;
                    bArr3[3] = 35;
                    if (SPUtils.getInstance().getBoolean(Code.EIGHT_TATTOO, false) && PrinterModelUtils.getPrinterModel().isTattooPaper()) {
                        bArr3[4] = ConvertUtils.hexString2Bytes(Integer.toHexString(printerModel.getTattooEnergy()))[0];
                        byte[] bArr4 = BluetoothOrder.new_print_img;
                        System.arraycopy(bArr4, 0, bArr3, 5, bArr4.length);
                    } else if (printBean.getPrintType() == Code.TEXT_PRINT_TYPE) {
                        int textEneragy = printerModel.getTextEneragy();
                        int i4 = i4;
                        if (i4 == 2) {
                            bArr3[4] = ConvertUtils.hexString2Bytes(Integer.toHexString(textEneragy - 2))[0];
                        } else if (i4 == 3) {
                            bArr3[4] = ConvertUtils.hexString2Bytes(Integer.toHexString(textEneragy))[0];
                        } else if (i4 == 5) {
                            bArr3[4] = ConvertUtils.hexString2Bytes(Integer.toHexString(textEneragy + 2))[0];
                        }
                        System.arraycopy(bArr2, 0, bArr3, 5, bArr2.length);
                    } else {
                        int i5 = i4;
                        if (i5 == 2) {
                            bArr3[4] = ConvertUtils.hexString2Bytes(Integer.toHexString(printerModel.getThinEneragy()))[0];
                        } else if (i5 == 3) {
                            bArr3[4] = ConvertUtils.hexString2Bytes(Integer.toHexString(printerModel.getModerationEneragy()))[0];
                        } else if (i5 == 5) {
                            bArr3[4] = ConvertUtils.hexString2Bytes(Integer.toHexString(printerModel.getDeepenEneragy()))[0];
                        }
                        byte[] bArr5 = BluetoothOrder.new_print_img;
                        System.arraycopy(bArr5, 0, bArr3, 5, bArr5.length);
                    }
                    int length = 5 + BluetoothOrder.new_print_img.length;
                    byte[] hexString2Bytes = ConvertUtils.hexString2Bytes(Integer.toHexString(i2));
                    for (int i6 = 0; i6 < i3; i6++) {
                        bArr3[length] = 27;
                        bArr3[length + 1] = RefErrorPtg.sid;
                        bArr3[length + 2] = 33;
                        if (hexString2Bytes.length > 1) {
                            bArr3[length + 3] = hexString2Bytes[1];
                        } else {
                            bArr3[length + 3] = 0;
                        }
                        bArr3[length + 4] = hexString2Bytes[0];
                        int i7 = length + 5;
                        for (int i8 = 0; i8 < i2; i8++) {
                            for (int i9 = 0; i9 < 3; i9++) {
                                int i10 = (i6 * 24 * i2) + (i9 * 8 * i2) + i8;
                                bArr3[i7] = (byte) (PrintDataUtils.p0[bitmapToBWPix[i10]] + PrintDataUtils.p1[bitmapToBWPix[i10 + i2]] + PrintDataUtils.p2[bitmapToBWPix[i10 + (i2 * 2)]] + PrintDataUtils.p3[bitmapToBWPix[i10 + (i2 * 3)]] + PrintDataUtils.p4[bitmapToBWPix[i10 + (i2 * 4)]] + PrintDataUtils.p5[bitmapToBWPix[i10 + (i2 * 5)]] + PrintDataUtils.p6[bitmapToBWPix[i10 + (i2 * 6)]] + bitmapToBWPix[i10 + (i2 * 7)]);
                                i7++;
                            }
                        }
                        bArr3[i7] = 27;
                        bArr3[i7 + 1] = TarConstants.LF_CHR;
                        bArr3[i7 + 2] = 0;
                        bArr3[i7 + 3] = 10;
                        length = i7 + 4;
                    }
                    int oneLength = printerModel.getOneLength();
                    if (Code.EIGHT_A4.equals(SPUtils.getInstance().getString(Code.EIGHTPAPER)) && height < PrinterModelUtils.getEightMaxHigh(printerModel)) {
                        oneLength = (PrinterModelUtils.getEightMaxHigh(printerModel) - height) / 24;
                    }
                    bArr3[length] = 27;
                    bArr3[length + 1] = 100;
                    bArr3[length + 2] = ConvertUtils.hexString2Bytes(Integer.toHexString(oneLength))[0];
                    byte[] bArr6 = BluetoothOrder.new_getDevState;
                    System.arraycopy(bArr6, 0, bArr3, length + 3, bArr6.length);
                    arrayList.add(bArr3);
                }
                EventBusUtils.getInstance().post(Code.EIGHT_STARTPRINT, (Object) arrayList);
            }
        }).start();
    }

    public static byte[] LongDetectionLabel(int i) {
        byte[] bArr = new byte[((BluetoothOrder.paper_1m.length + BluetoothOrder.getDevState.length) * i)];
        int i2 = 0;
        for (int i3 = 0; i3 < i; i3++) {
            byte[] bArr2 = BluetoothOrder.paper_1m;
            System.arraycopy(bArr2, 0, bArr, i2, bArr2.length);
            int length = i2 + bArr2.length;
            byte[] bArr3 = BluetoothOrder.getDevState;
            System.arraycopy(bArr3, 0, bArr, length, bArr3.length);
            i2 = length + bArr3.length;
        }
        return bArr;
    }

    public static byte[] upLabel(int i) {
        byte[] hexString2Bytes = ConvertUtils.hexString2Bytes(Integer.toHexString(i));
        byte[] bArr = BluetoothOrder.getDevState;
        byte[] bArr2 = new byte[(bArr.length + 10)];
        bArr2[0] = 81;
        bArr2[1] = TarConstants.LF_PAX_EXTENDED_HEADER_LC;
        bArr2[2] = -95;
        bArr2[3] = 0;
        bArr2[4] = 2;
        bArr2[5] = 0;
        if (hexString2Bytes.length > 1) {
            bArr2[6] = hexString2Bytes[1];
        } else {
            bArr2[6] = hexString2Bytes[0];
        }
        if (hexString2Bytes.length > 1) {
            bArr2[7] = hexString2Bytes[0];
        } else {
            bArr2[7] = 0;
        }
        bArr2[8] = BluetoothOrder.calcCrc8(bArr2, 6, 2);
        bArr2[9] = -1;
        System.arraycopy(bArr, 0, bArr2, 10, bArr.length);
        return bArr2;
    }

    public static byte[] paperLe(int i) {
        byte[] hexString2Bytes = ConvertUtils.hexString2Bytes(Integer.toHexString(i));
        byte[] bArr = new byte[10];
        bArr[0] = 81;
        bArr[1] = TarConstants.LF_PAX_EXTENDED_HEADER_LC;
        bArr[2] = -95;
        bArr[3] = 0;
        bArr[4] = 2;
        bArr[5] = 0;
        if (hexString2Bytes.length > 1) {
            bArr[6] = hexString2Bytes[1];
        } else {
            bArr[6] = hexString2Bytes[0];
        }
        if (hexString2Bytes.length > 1) {
            bArr[7] = hexString2Bytes[0];
        } else {
            bArr[7] = 0;
        }
        bArr[8] = BluetoothOrder.calcCrc8(bArr, 6, 2);
        bArr[9] = -1;
        return bArr;
    }

    public static byte[] paperLeCheckBlack(int i) {
        byte[] hexString2Bytes = ConvertUtils.hexString2Bytes(Integer.toHexString(Math.abs(i)));
        byte[] bArr = new byte[11];
        bArr[0] = 81;
        bArr[1] = TarConstants.LF_PAX_EXTENDED_HEADER_LC;
        if (i < 0) {
            bArr[2] = -96;
        } else {
            bArr[2] = -95;
        }
        bArr[3] = 0;
        bArr[4] = 3;
        bArr[5] = 0;
        if (hexString2Bytes.length > 1) {
            bArr[6] = hexString2Bytes[1];
        } else {
            bArr[6] = hexString2Bytes[0];
        }
        if (hexString2Bytes.length > 1) {
            bArr[7] = hexString2Bytes[0];
        } else {
            bArr[7] = 0;
        }
        bArr[8] = 17;
        bArr[9] = BluetoothOrder.calcCrc8(bArr, 6, 3);
        bArr[10] = -1;
        return bArr;
    }

    public int getPR88Speed(int i, int i2) {
        return ((int) (ComputeUtils.div(ComputeUtils.div((float) (i2 / 1024), (float) (getImgSpeed() != 0 ? getImgSpeed() : 30), 8), (float) (i * 2), 8) * ((float) ComputeUtils.pow(10, 4)))) + 5;
    }
}
