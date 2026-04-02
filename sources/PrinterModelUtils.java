package com.lib.blueUtils;

import com.Utils.PrintModelUtils;
import com.Utils.PrinterModel;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.lib.Utils.Code;
import com.lib.Utils.DeviceUtils;
import com.lib.Utils.SpecialPhoneMtu;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.poi.hslf.model.Shape;

public class PrinterModelUtils {
    private static String json = "";
    public static ArrayList modelList = new ArrayList();
    private static PrinterModel printerModel;

    public static PrinterModel.DataBean getPrinterModel() {
        try {
            return getPrinterModel(SPUtils.getInstance().getString(Code.DEVICETYPE));
        } catch (NullPointerException unused) {
            return null;
        }
    }

    public static void getModelList() {
        ArrayList<PrinterModel.DataBean> arrayList;
        String str;
        String string = SPUtils.getInstance().getString(Code.DEVINFOJSON, "");
        json = string;
        Class cls = PrinterModel.class;
        if (StringUtils.isEmpty(string)) {
            arrayList = new ArrayList<>();
        } else {
            PrinterModel printerModel2 = (PrinterModel) GsonUtils.fromJson(json, cls);
            printerModel = printerModel2;
            arrayList = printerModel2.getData();
        }
        if (arrayList == null) {
            arrayList = new ArrayList<>();
        }
        if ("iPrint".equals(SPUtils.getInstance().getString(Code.appInfo))) {
            str = PrintModelUtils.Companion.getData_iPrint();
        } else if ("tinyPrint".equals(SPUtils.getInstance().getString(Code.appInfo))) {
            str = PrintModelUtils.Companion.getData_tinyPrint();
        } else if (Code.zhiwuya.equals(Code.appInfo())) {
            str = PrintModelUtils.Companion.getData_zhiwuya();
        } else if ("CorePrint".equals(Code.appInfo())) {
            str = PrintModelUtils.Companion.getCorePrint();
        } else {
            str = PrintModelUtils.Companion.getData_jzxx();
        }
        Iterator<PrinterModel.DataBean> it = ((PrinterModel) GsonUtils.fromJson(str, cls)).getData().iterator();
        while (it.hasNext()) {
            PrinterModel.DataBean next = it.next();
            Iterator<PrinterModel.DataBean> it2 = arrayList.iterator();
            while (true) {
                if (it2.hasNext()) {
                    if (next.getModelNo().equals(it2.next().getModelNo())) {
                        break;
                    }
                } else {
                    arrayList.add(next);
                    break;
                }
            }
        }
        modelList = arrayList;
        LogUtils.e("getModelList", Integer.valueOf(arrayList.size()));
    }

    public static PrinterModel.DataBean getPrinterModel(String str) {
        try {
            if (StringUtils.isEmpty(str)) {
                return PrinterModel.getModel("");
            }
            if (modelList.size() == 0) {
                getModelList();
            }
            Iterator it = modelList.iterator();
            PrinterModel.DataBean dataBean = null;
            while (it.hasNext()) {
                PrinterModel.DataBean dataBean2 = (PrinterModel.DataBean) it.next();
                if (str.startsWith(dataBean2.getModelNo()) && (dataBean == null || dataBean.getModelNo().length() < dataBean2.getModelNo().length())) {
                    dataBean = dataBean2;
                }
            }
            return dataBean == null ? PrinterModel.getModel("") : dataBean;
        } catch (NullPointerException e) {
            LogUtils.e("getPrinterModel——e", e.getMessage());
            return PrinterModel.getModel("");
        }
    }

    public static int getPrintSize() {
        try {
            PrinterModel.DataBean printerModel2 = getPrinterModel(SPUtils.getInstance().getString(Code.DEVICETYPE));
            if (showPaperWidth(printerModel2)) {
                String string = SPUtils.getInstance().getString(Code.paperWidth);
                if (!StringUtils.isEmpty(string)) {
                    if (!Code.m210.equals(string)) {
                        if (Code.m113.equals(string)) {
                            return 768;
                        }
                        if (Code.m79.equals(string)) {
                            return Shape.MASTER_DPI;
                        }
                        if (Code.m57.equals(string)) {
                            return 384;
                        }
                    }
                }
                return printerModel2.getPaperSize();
            }
            return printerModel2.getPaperSize();
        } catch (NullPointerException unused) {
            return 384;
        }
    }

    public static int getPrintImgSize() {
        try {
            PrinterModel.DataBean printerModel2 = getPrinterModel(SPUtils.getInstance().getString(Code.DEVICETYPE));
            if (!DeviceUtils.addMorPix(printerModel2.getModelNo())) {
                return printerModel2.getPrintSize();
            }
            return printerModel2.getPaperSize();
        } catch (NullPointerException unused) {
            return 384;
        }
    }

    public static int getImgPrintSpeed() {
        try {
            PrinterModel.DataBean printerModel2 = getPrinterModel(SPUtils.getInstance().getString(Code.DEVICETYPE));
            if (printerModel2.getModelNo().equals("GB01") && SpecialPhoneMtu.createSpecialPhoneMtu().getImgSpeed() != 0) {
                return SpecialPhoneMtu.createSpecialPhoneMtu().getImgSpeed();
            }
            if (Code.OFFICE_PRINT.equals(SPUtils.getInstance().getString(Code.ACTIVITY_NAME))) {
                return printerModel2.getTextPrintSpeed();
            }
            return printerModel2.getImgPrintSpeed();
        } catch (NullPointerException unused) {
            return 20;
        }
    }

    public static int getTextPrintSpeed() {
        try {
            PrinterModel.DataBean printerModel2 = getPrinterModel(SPUtils.getInstance().getString(Code.DEVICETYPE));
            if (!printerModel2.getModelNo().equals("GB01") || SpecialPhoneMtu.createSpecialPhoneMtu().getImgSpeed() == 0) {
                return printerModel2.getTextPrintSpeed();
            }
            return SpecialPhoneMtu.createSpecialPhoneMtu().getTextSpeed();
        } catch (NullPointerException unused) {
            return 20;
        }
    }

    public static int getTattooSpeed() {
        try {
            PrinterModel.DataBean printerModel2 = getPrinterModel(SPUtils.getInstance().getString(Code.DEVICETYPE));
            if (printerModel2.isTattooPaper()) {
                return printerModel2.getTattooSpeed();
            }
            return printerModel2.getImgPrintSpeed();
        } catch (NullPointerException unused) {
            return 20;
        }
    }

    public static int getPrinterMtu() {
        try {
            PrinterModel.DataBean printerModel2 = getPrinterModel(SPUtils.getInstance().getString(Code.DEVICETYPE));
            if (!printerModel2.getModelNo().equals("GB01") || SpecialPhoneMtu.createSpecialPhoneMtu().getMTU() == 0) {
                return printerModel2.getImgMTU();
            }
            return SpecialPhoneMtu.createSpecialPhoneMtu().getMTU();
        } catch (NullPointerException unused) {
            return 0;
        }
    }

    public static boolean showAiPainting() {
        PrinterModel.DataBean printerModel2 = getPrinterModel(SPUtils.getInstance().getString(Code.DEVICETYPE));
        String string = SPUtils.getInstance().getString(Code.showAiPaintingDev, "0019B-C");
        if (StringUtils.isEmpty(string) || StringUtils.isEmpty(printerModel2.getModelNo())) {
            return false;
        }
        return string.contains(printerModel2.getModelNo());
    }

    public static boolean showPaperWidth() {
        return showPaperWidth(getPrinterModel(SPUtils.getInstance().getString(Code.DEVICETYPE)));
    }

    public static boolean showPaperWidth(PrinterModel.DataBean dataBean) {
        String string = SPUtils.getInstance().getString(Code.changePaperWidth, "GT08,A42II,A41II,GT09,X9,A43,P4");
        if (StringUtils.isEmpty(string) || StringUtils.isEmpty(dataBean.getModelNo())) {
            return false;
        }
        return string.contains(dataBean.getModelNo());
    }

    public static boolean checkPrintStatus(PrinterModel.DataBean dataBean) {
        String string = SPUtils.getInstance().getString(Code.PrintCamera, "XiaoWa,CP01,JRX01,QDX01");
        if (StringUtils.isEmpty(string) || StringUtils.isEmpty(dataBean.getModelNo())) {
            return false;
        }
        return string.contains(dataBean.getModelNo());
    }

    public static boolean checkPrintStatus(String str) {
        String string = SPUtils.getInstance().getString(Code.PrintCamera, "XiaoWa,CP01,JRX01,QDX01");
        if (StringUtils.isEmpty(string) || StringUtils.isEmpty(str)) {
            return false;
        }
        return string.contains(str);
    }

    public static boolean checkUnShowSquare(String str) {
        String string = SPUtils.getInstance().getString(Code.unShowSquare, "X6,X6h,X6H,X6HP");
        if (!StringUtils.isEmpty(string) && !StringUtils.isEmpty(str)) {
            return string.contains(str);
        }
        return true;
    }

    public static int getPrintEneragy(int i, int i2, boolean z) {
        int i3;
        PrinterModel.DataBean printerModel2 = getPrinterModel();
        int i4 = Code.DEFCONCENTRATIONS;
        if (Code.OFFICE_PRINT.equals(SPUtils.getInstance().getString(Code.ACTIVITY_NAME))) {
            i3 = printerModel2.getTextEneragy();
        } else if (Code.IMG_PRINT_TYPE == i2 || Code.IMG_PRINT_LABEL == i2) {
            i3 = printerModel2.getModerationEneragy();
            if (printerModel2.isGrayPrint() && z) {
                i3 = printerModel2.getGrayModerationEneragy();
            }
        } else {
            i3 = Code.TEXT_PRINT_TYPE == i2 ? (!printerModel2.isGrayPrint() || !z) ? printerModel2.getTextEneragy() : printerModel2.getModerationEneragy() : 0;
        }
        double d = (double) i3;
        return (int) (d + (((double) (i - i4)) * 0.15d * d));
    }

    public static int getInterval() {
        PrinterModel.DataBean printerModel2 = getPrinterModel(SPUtils.getInstance().getString(Code.DEVICETYPE));
        if (!printerModel2.getModelNo().equals("GB01") || SpecialPhoneMtu.createSpecialPhoneMtu().getTextMTU() == 0) {
            return printerModel2.getInterval();
        }
        return SpecialPhoneMtu.createSpecialPhoneMtu().getInterval();
    }

    public static int getSlowInterval() {
        return getPrinterModel(SPUtils.getInstance().getString(Code.DEVICETYPE)).getSlowInterval();
    }

    public static int getDevDpi() {
        try {
            return getPrinterModel(SPUtils.getInstance().getString(Code.DEVICETYPE)).getDevdpi();
        } catch (NullPointerException unused) {
            return 200;
        }
    }

    public static int getoneLength() {
        try {
            return getPrinterModel(SPUtils.getInstance().getString(Code.DEVICETYPE)).getOneLength();
        } catch (NullPointerException unused) {
            return 12;
        }
    }

    public static int getDevSize() {
        try {
            PrinterModel.DataBean printerModel2 = getPrinterModel(SPUtils.getInstance().getString(Code.DEVICETYPE));
            if (showPaperWidth(printerModel2)) {
                String string = SPUtils.getInstance().getString(Code.paperWidth);
                if (StringUtils.isEmpty(string)) {
                    return 8;
                }
                if (Code.m210.equals(string)) {
                    return 8;
                }
                if (Code.m113.equals(string)) {
                    return 4;
                }
                if (Code.m79.equals(string)) {
                    return 3;
                }
                if (Code.m57.equals(string)) {
                    return 2;
                }
            }
            return printerModel2.getSize();
        } catch (NullPointerException unused) {
            return 12;
        }
    }

    public static int getEightImageMaxHigh(PrinterModel.DataBean dataBean) {
        try {
            if (dataBean.getDevdpi() == 300) {
                return (int) (((double) 2200) * 1.5d);
            }
            return 2200;
        } catch (NullPointerException unused) {
            return 0;
        }
    }

    public static int getEightImageMaxHigh() {
        try {
            if (getPrinterModel(SPUtils.getInstance().getString(Code.DEVICETYPE)).getDevdpi() == 300) {
                return (int) (((double) 2200) * 1.5d);
            }
            return 2200;
        } catch (NullPointerException unused) {
            return 0;
        }
    }

    public static int getEightMaxHigh() {
        try {
            if (getPrinterModel(SPUtils.getInstance().getString(Code.DEVICETYPE)).getDevdpi() == 300) {
                return (int) (((double) 2400) * 1.5d);
            }
            return 2400;
        } catch (NullPointerException unused) {
            return 0;
        }
    }

    public static int getEightMaxHigh(PrinterModel.DataBean dataBean) {
        try {
            if (dataBean.getDevdpi() == 300) {
                return 3800;
            }
            return "X9".equals(dataBean.getModelNo()) ? 2460 : 2400;
        } catch (NullPointerException unused) {
            return 0;
        }
    }
}
