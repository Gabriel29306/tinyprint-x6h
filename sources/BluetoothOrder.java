package com.lib.blueUtils;

import cn.com.heaton.blelibrary.ble.utils.ByteUtils;
import com.Utils.PrinterModel;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.stringlib.R$string;
import java.util.UUID;
import okio.Utf8;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.pdfbox.pdmodel.documentinterchange.taggedpdf.PDLayoutAttributeObject;
import org.apache.poi.hslf.model.PPFont;
import org.apache.poi.hslf.record.OEPlaceholderAtom;
import org.apache.poi.hssf.record.PaletteRecord;
import org.apache.poi.ss.formula.ptg.Area3DPtg;
import org.apache.poi.ss.formula.ptg.AreaErrPtg;
import org.apache.poi.ss.formula.ptg.AttrPtg;
import org.apache.poi.ss.formula.ptg.BoolPtg;
import org.apache.poi.ss.formula.ptg.DeletedArea3DPtg;
import org.apache.poi.ss.formula.ptg.DeletedRef3DPtg;
import org.apache.poi.ss.formula.ptg.IntPtg;
import org.apache.poi.ss.formula.ptg.MemFuncPtg;
import org.apache.poi.ss.formula.ptg.NumberPtg;
import org.apache.poi.ss.formula.ptg.Ref3DPtg;
import org.apache.poi.ss.formula.ptg.RefErrorPtg;
import org.apache.poi.ss.formula.ptg.RefNPtg;
import org.apache.poi.ss.formula.ptg.RefPtg;

public class BluetoothOrder {
    private static final byte[] CHECKSUM_TABLE = {0, 7, 14, 9, 28, 27, 18, 21, PaletteRecord.STANDARD_PALETTE_SIZE, Utf8.REPLACEMENT_BYTE, TarConstants.LF_FIFO, TarConstants.LF_LINK, RefPtg.sid, 35, RefErrorPtg.sid, 45, 112, 119, 126, 121, 108, 107, 98, 101, 72, 79, 70, 65, 84, TarConstants.LF_GNUTYPE_SPARSE, 90, 93, -32, -25, -18, -23, -4, -5, -14, -11, -40, -33, -42, -47, -60, -61, -54, -51, -112, -105, -98, -103, -116, -117, -126, -123, -88, -81, -90, -95, -76, -77, -70, -67, -57, -64, -55, -50, -37, -36, -43, -46, -1, -8, -15, -10, -29, -28, -19, -22, -73, -80, -71, -66, -85, -84, -91, -94, -113, -120, -127, -122, -109, -108, -99, -102, 39, 32, MemFuncPtg.sid, 46, Area3DPtg.sid, DeletedRef3DPtg.sid, TarConstants.LF_DIR, TarConstants.LF_SYMLINK, NumberPtg.sid, OEPlaceholderAtom.MediaClip, 17, 22, 3, 4, 13, 10, 87, PPFont.FF_DECORATIVE, 89, 94, TarConstants.LF_GNUTYPE_LONGLINK, TarConstants.LF_GNUTYPE_LONGNAME, 69, 66, 111, 104, 97, 102, 115, 116, 125, 122, -119, -114, -121, Byte.MIN_VALUE, -107, -110, -101, -100, -79, -74, -65, -72, -83, -86, -93, -92, -7, -2, -9, -16, -27, -30, -21, -20, -63, -58, -49, -56, -35, -38, -45, -44, 105, 110, TarConstants.LF_PAX_GLOBAL_EXTENDED_HEADER, 96, 117, 114, 123, 124, 81, 86, 95, TarConstants.LF_PAX_EXTENDED_HEADER_UC, 77, 74, 67, 68, AttrPtg.sid, IntPtg.sid, 23, 16, 5, 2, 11, 12, 33, 38, 47, 40, DeletedArea3DPtg.sid, Ref3DPtg.sid, TarConstants.LF_CHR, TarConstants.LF_BLK, 78, 73, 64, 71, 82, 85, 92, 91, 118, 113, TarConstants.LF_PAX_EXTENDED_HEADER_LC, Byte.MAX_VALUE, 106, 109, 100, 99, 62, 57, 48, TarConstants.LF_CONTIG, 34, 37, RefNPtg.sid, AreaErrPtg.sid, 6, 1, 8, 15, 26, BoolPtg.sid, 20, 19, -82, -87, -96, -89, -78, -75, -68, -69, -106, -111, -104, -97, -118, -115, -124, -125, -34, -39, -48, -41, -62, -59, -52, -53, -26, -31, -24, -17, -6, -3, -12, -13};
    public static final byte[] endSendData = {81, 121, 2, 0, 1, 0, 2, 14, -1};
    public static final byte[] finishLattice = {81, TarConstants.LF_PAX_EXTENDED_HEADER_LC, -90, 0, 11, 0, -86, 85, 23, 0, 0, 0, 0, 0, 0, 0, 23, 17, -1};
    public static final byte[] getDevId = {81, TarConstants.LF_PAX_EXTENDED_HEADER_LC, -69, 0, 1, 0, 1, 7, -1};
    public static final byte[] getDevInfo = {81, TarConstants.LF_PAX_EXTENDED_HEADER_LC, -88, 0, 1, 0, 0, 0, -1};
    public static final byte[] getDevState = {81, TarConstants.LF_PAX_EXTENDED_HEADER_LC, -93, 0, 1, 0, 0, 0, -1};
    public static final byte[] getElectricQuantity = {81, TarConstants.LF_PAX_EXTENDED_HEADER_LC, -70, 0, 1, 0, 0, 0, -1};
    public static final byte[] new_flag = {18, 81, TarConstants.LF_PAX_EXTENDED_HEADER_LC, -68, 0, 1, 0, 1, 7, -1};
    public static final byte[] new_getDevId = {18, 81, TarConstants.LF_PAX_EXTENDED_HEADER_LC, -69, 0, 1, 0, 1, 7, -1};
    public static final byte[] new_getDevInfo = {18, 81, TarConstants.LF_PAX_EXTENDED_HEADER_LC, -88, 0, 1, 0, 0, 0, -1};
    public static final byte[] new_getDevState = {18, 81, TarConstants.LF_PAX_EXTENDED_HEADER_LC, -93, 0, 1, 0, 0, 0, -1};
    public static final byte[] new_print_img = {18, 81, TarConstants.LF_PAX_EXTENDED_HEADER_LC, -66, 0, 1, 0, 0, 0, -1};
    public static final byte[] new_print_text = {18, 81, TarConstants.LF_PAX_EXTENDED_HEADER_LC, -66, 0, 1, 0, 1, 7, -1};
    public static final byte[] paper = {81, TarConstants.LF_PAX_EXTENDED_HEADER_LC, -95, 0, 2, 0, 48, 0, -7, -1};
    public static final byte[] paper2 = {81, TarConstants.LF_PAX_EXTENDED_HEADER_LC, -95, 0, 2, 0, 96, 0, -11, -1};
    public static final byte[] paper_1m = {81, TarConstants.LF_PAX_EXTENDED_HEADER_LC, -95, 0, 2, 0, 1, 0, 21, -1};
    public static final byte[] paper_300dpi = {81, TarConstants.LF_PAX_EXTENDED_HEADER_LC, -95, 0, 2, 0, 72, 0, -13, -1};
    public static final byte[] printLattice = {81, TarConstants.LF_PAX_EXTENDED_HEADER_LC, -90, 0, 11, 0, -86, 85, 23, PaletteRecord.STANDARD_PALETTE_SIZE, 68, 95, 95, 95, 68, PaletteRecord.STANDARD_PALETTE_SIZE, RefNPtg.sid, -95, -1};
    public static final byte[] print_Label = {81, TarConstants.LF_PAX_EXTENDED_HEADER_LC, -66, 0, 1, 0, 3, 9, -1};
    public static final byte[] print_img = {81, TarConstants.LF_PAX_EXTENDED_HEADER_LC, -66, 0, 1, 0, 0, 0, -1};
    public static final byte[] print_img_gray_16 = {81, TarConstants.LF_PAX_EXTENDED_HEADER_LC, -66, 0, 2, 0, 0, 1, 0, -1};
    public static final byte[] print_img_gray_8 = {81, TarConstants.LF_PAX_EXTENDED_HEADER_LC, -66, 0, 2, 0, 0, 0, 0, -1};
    public static final byte[] print_tattoo = {81, TarConstants.LF_PAX_EXTENDED_HEADER_LC, -66, 0, 1, 0, 2, 14, -1};
    public static final byte[] print_text = {81, TarConstants.LF_PAX_EXTENDED_HEADER_LC, -66, 0, 1, 0, 1, 7, -1};
    public static final byte[] print_text_gray_16 = {81, TarConstants.LF_PAX_EXTENDED_HEADER_LC, -66, 0, 2, 0, 1, 1, 18, -1};
    public static final byte[] print_text_gray_8 = {81, TarConstants.LF_PAX_EXTENDED_HEADER_LC, -66, 0, 2, 0, 1, 0, 21, -1};
    public static final byte[] quality1 = {81, TarConstants.LF_PAX_EXTENDED_HEADER_LC, -92, 0, 1, 0, TarConstants.LF_LINK, -105, -1};
    public static final byte[] quality2 = {81, TarConstants.LF_PAX_EXTENDED_HEADER_LC, -92, 0, 1, 0, TarConstants.LF_SYMLINK, -98, -1};
    public static final byte[] quality3 = {81, TarConstants.LF_PAX_EXTENDED_HEADER_LC, -92, 0, 1, 0, TarConstants.LF_CHR, -103, -1};
    public static final byte[] quality4 = {81, TarConstants.LF_PAX_EXTENDED_HEADER_LC, -92, 0, 1, 0, TarConstants.LF_BLK, -116, -1};
    public static final byte[] quality5 = {81, TarConstants.LF_PAX_EXTENDED_HEADER_LC, -92, 0, 1, 0, TarConstants.LF_DIR, -117, -1};
    public static final byte[] sendImageFile = {81, 121, 1, 0, 1, 0, 1, 7, -1};
    public static final byte[] sendVideoFile = {81, 121, 1, 0, 1, 0, 2, 14, -1};
    public static final UUID[] serviceUUID = {UUID.fromString("0000AE00-0000-1000-8000-00805F9B34FB"), UUID.fromString("0000FF00-0000-1000-8000-00805F9B34FB"), UUID.fromString("0000AB00-0000-1000-8000-00805F9B34FB")};
    public static final byte[] startSendData = {81, 121, 2, 0, 1, 0, 1, 7, -1};
    public static final byte[] updateDev = {81, TarConstants.LF_PAX_EXTENDED_HEADER_LC, -87, 0, 1, 0, 0, 0, -1};
    public static final byte[] wifiData = {81, TarConstants.LF_PAX_EXTENDED_HEADER_LC, -86, 0, 10, 0, TarConstants.LF_CHR, TarConstants.LF_FIFO, 48, 115, 117, 110, 112, 101, 110, TarConstants.LF_PAX_GLOBAL_EXTENDED_HEADER, 1, -1, 81, TarConstants.LF_PAX_EXTENDED_HEADER_LC, -85, 0, 8, 0, TarConstants.LF_CHR, TarConstants.LF_DIR, PaletteRecord.STANDARD_PALETTE_SIZE, PaletteRecord.STANDARD_PALETTE_SIZE, TarConstants.LF_CONTIG, TarConstants.LF_CHR, TarConstants.LF_DIR, TarConstants.LF_LINK, -87, -1};
    public static final UUID[] writeUUid = {UUID.fromString("0000AE01-0000-1000-8000-00805F9B34FB"), UUID.fromString("0000FF02-0000-1000-8000-00805F9B34FB"), UUID.fromString("0000AB01-0000-1000-8000-00805F9B34FB")};

    public static String getDevType(int i) {
        return "XW00" + i;
    }

    public static byte[] getDevId(String str) {
        if (PrinterModelUtils.getPrinterModel(str).isNewFormat()) {
            return new_getDevId;
        }
        return getDevId;
    }

    public static byte[] getDevInfo(String str) {
        if (PrinterModelUtils.getPrinterModel(str).isNewFormat()) {
            return new_getDevInfo;
        }
        return getDevInfo;
    }

    public static byte[] getXWDevInfo(byte[] bArr) {
        byte[] bArr2 = new byte[(bArr.length + 8)];
        bArr2[0] = 81;
        bArr2[1] = TarConstants.LF_PAX_EXTENDED_HEADER_LC;
        bArr2[2] = -47;
        bArr2[3] = 0;
        bArr2[4] = 8;
        bArr2[5] = 0;
        for (int i = 0; i < bArr.length; i++) {
            bArr2[i + 6] = bArr[i];
        }
        bArr2[bArr.length + 6] = calcCrc8(bArr);
        bArr2[bArr.length + 7] = -1;
        LogUtils.e("dddddd", ConvertUtils.bytes2HexString(bArr2));
        return bArr2;
    }

    public static byte[] getDevStateByte(String str) {
        if (PrinterModelUtils.getPrinterModel(str).isNewFormat()) {
            return new_getDevState;
        }
        return getDevState;
    }

    public static String getDevWifiState(int i) {
        if (i == 0) {
            return StringUtils.getString(R$string.no_wifi_no_setting);
        }
        if (i == 1) {
            return StringUtils.getString(R$string.no_wifi_setting);
        }
        if (i == 2) {
            return StringUtils.getString(R$string.network_not_set_wifi);
        }
        if (i == 3) {
            return StringUtils.getString(R$string.network_set_wifi);
        }
        return "";
    }

    public static String getDevState(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        if (str.endsWith("1")) {
            return StringUtils.getString(R$string.no_paper);
        }
        if (str.endsWith("10")) {
            return StringUtils.getString(R$string.paper_positions_open);
        }
        if (str.endsWith("100")) {
            return StringUtils.getString(R$string.too_hot);
        }
        if (str.endsWith("1000")) {
            return StringUtils.getString(R$string.no_power_please_charge);
        }
        if (str.endsWith("10000")) {
            return StringUtils.getString(R$string.charging_tip);
        }
        if (str.endsWith("10000000")) {
            return StringUtils.getString(R$string.printing);
        }
        return "";
    }

    public static byte[] getBlackening(int i) {
        PrinterModel.DataBean printerModel = PrinterModelUtils.getPrinterModel();
        if (printerModel.isA4XII()) {
            if (i >= 6) {
                return quality5;
            }
            if (i >= 3) {
                return quality3;
            }
            return quality2;
        } else if (!printerModel.isCorePrint()) {
            return quality3;
        } else {
            if (i == 3) {
                return quality5;
            }
            if (i == 2) {
                return quality3;
            }
            return quality2;
        }
    }

    public static byte[] sendWifi(String str, String str2) {
        byte[] bytes = str.getBytes();
        byte[] bytes2 = str2.getBytes();
        byte[] bArr = new byte[(bytes.length + 8 + bytes2.length + 8)];
        byte[] int2byte = ByteUtils.int2byte(bytes.length);
        byte[] int2byte2 = ByteUtils.int2byte(str2.length());
        bArr[0] = 81;
        bArr[1] = TarConstants.LF_PAX_EXTENDED_HEADER_LC;
        bArr[2] = -86;
        bArr[3] = 0;
        bArr[4] = int2byte[0];
        bArr[5] = 0;
        for (int i = 0; i < bytes.length; i++) {
            bArr[i + 6] = bytes[i];
        }
        bArr[bytes.length + 6] = calcCrc8(bytes);
        bArr[bytes.length + 7] = -1;
        bArr[bytes.length + 8] = 81;
        bArr[bytes.length + 9] = TarConstants.LF_PAX_EXTENDED_HEADER_LC;
        bArr[bytes.length + 10] = -85;
        bArr[bytes.length + 11] = 0;
        bArr[bytes.length + 12] = int2byte2[0];
        bArr[bytes.length + 13] = 0;
        for (int i2 = 0; i2 < bytes2.length; i2++) {
            bArr[i2 + 14 + bytes.length] = bytes2[i2];
        }
        bArr[bytes.length + 14 + bytes2.length] = calcCrc8(bytes2);
        bArr[bytes.length + 15 + bytes2.length] = -1;
        return bArr;
    }

    public static byte[] getWifiName(String str) {
        byte[] bytes = str.getBytes();
        byte[] bArr = new byte[(bytes.length + 8)];
        byte[] int2byte = ByteUtils.int2byte(bytes.length);
        bArr[0] = 81;
        bArr[1] = TarConstants.LF_PAX_EXTENDED_HEADER_LC;
        bArr[2] = -86;
        bArr[3] = 0;
        bArr[4] = int2byte[0];
        bArr[5] = 0;
        for (int i = 0; i < bytes.length; i++) {
            bArr[i + 6] = bytes[i];
        }
        bArr[bytes.length + 6] = calcCrc8(bytes);
        bArr[bytes.length + 7] = -1;
        return bArr;
    }

    public static byte[] getWifiPwd(String str) {
        byte[] bytes = str.getBytes();
        byte[] int2byte = ByteUtils.int2byte(str.length());
        byte[] bArr = new byte[(bytes.length + 8)];
        bArr[0] = 81;
        bArr[1] = TarConstants.LF_PAX_EXTENDED_HEADER_LC;
        bArr[2] = -85;
        bArr[3] = 0;
        bArr[4] = int2byte[0];
        bArr[5] = 0;
        for (int i = 0; i < bytes.length; i++) {
            bArr[i + 6] = bytes[i];
        }
        bArr[bytes.length + 6] = calcCrc8(bytes);
        bArr[bytes.length + 7] = -1;
        return bArr;
    }

    public static byte[] sendDateToWifi(String str, String str2) {
        byte[] bytes = (str + str2).getBytes();
        byte[] bArr = new byte[(bytes.length + 10)];
        byte[] int2byte = ByteUtils.int2byte(bytes.length + 2);
        bArr[0] = 81;
        bArr[1] = TarConstants.LF_PAX_EXTENDED_HEADER_LC;
        bArr[2] = -91;
        bArr[3] = 0;
        bArr[4] = int2byte[0];
        bArr[5] = 0;
        bArr[6] = 0;
        bArr[7] = -96;
        for (int i = 0; i < bytes.length; i++) {
            bArr[i + 8] = bytes[i];
        }
        bArr[bytes.length + 8] = calcCrc8(bArr, 6, bytes.length + 2);
        bArr[bytes.length + 9] = -1;
        return bArr;
    }

    public static byte[] sendWifiPw(String str) {
        byte[] bytes = str.getBytes();
        int length = bytes.length;
        byte[] bArr = new byte[(length + 8)];
        byte[] int2byte = ByteUtils.int2byte(bytes.length);
        bArr[0] = 81;
        bArr[1] = TarConstants.LF_PAX_EXTENDED_HEADER_LC;
        bArr[2] = -85;
        bArr[3] = 0;
        bArr[4] = int2byte[0];
        bArr[5] = 0;
        for (int i = 0; i < bytes.length; i++) {
            bArr[i + 6] = bytes[i];
        }
        bArr[length + 6] = calcCrc8(bytes);
        bArr[length + 7] = -1;
        return bArr;
    }

    public static byte[] writeDevIDs(String str, String str2) {
        if (PrinterModelUtils.getPrinterModel(str2).isNewCompress()) {
            return newWriteDevId(str);
        }
        return writeDevId(str);
    }

    public static byte[] writeDevId(String str) {
        if (str.length() != 12) {
            return null;
        }
        byte[] hexToByteArray = hexToByteArray(TarConstants.VERSION_POSIX + str);
        int length = hexToByteArray.length;
        byte[] bArr = new byte[(length + 8)];
        bArr[0] = 81;
        bArr[1] = TarConstants.LF_PAX_EXTENDED_HEADER_LC;
        bArr[2] = -69;
        bArr[3] = 0;
        bArr[4] = 7;
        bArr[5] = 0;
        for (int i = 0; i < hexToByteArray.length; i++) {
            bArr[i + 6] = hexToByteArray[i];
        }
        bArr[length + 6] = calcCrc8(hexToByteArray);
        bArr[length + 7] = -1;
        return bArr;
    }

    public static byte[] newWriteDevId(String str) {
        if (str.length() != 12) {
            return null;
        }
        byte[] hexToByteArray = hexToByteArray(TarConstants.VERSION_POSIX + str);
        int length = hexToByteArray.length;
        byte[] bArr = new byte[(length + 9)];
        bArr[0] = 18;
        bArr[1] = 81;
        bArr[2] = TarConstants.LF_PAX_EXTENDED_HEADER_LC;
        bArr[3] = -69;
        bArr[4] = 0;
        bArr[5] = 7;
        bArr[6] = 0;
        for (int i = 0; i < hexToByteArray.length; i++) {
            bArr[i + 7] = hexToByteArray[i];
        }
        bArr[length + 7] = calcCrc8(hexToByteArray);
        bArr[length + 8] = -1;
        return bArr;
    }

    public static byte[] sendLanguage(int i) {
        byte[] hexString2Bytes = ConvertUtils.hexString2Bytes(Integer.toHexString(i));
        byte[] bArr = new byte[9];
        bArr[0] = 81;
        bArr[1] = TarConstants.LF_PAX_EXTENDED_HEADER_LC;
        bArr[2] = -74;
        bArr[3] = 0;
        bArr[4] = 1;
        bArr[5] = 0;
        if (hexString2Bytes.length > 1) {
            bArr[6] = hexString2Bytes[1];
        } else {
            bArr[6] = hexString2Bytes[0];
        }
        bArr[7] = calcCrc8(bArr, 6, 1);
        bArr[8] = -1;
        return bArr;
    }

    public static byte[] hexToByteArray(String str) {
        byte[] bArr;
        int length = str.length();
        if (length % 2 == 1) {
            length++;
            bArr = new byte[(length / 2)];
            str = PDLayoutAttributeObject.GLYPH_ORIENTATION_VERTICAL_ZERO_DEGREES + str;
        } else {
            bArr = new byte[(length / 2)];
        }
        int i = 0;
        int i2 = 0;
        while (i < length) {
            int i3 = i + 2;
            bArr[i2] = hexToByte(str.substring(i, i3));
            i2++;
            i = i3;
        }
        return bArr;
    }

    public static byte hexToByte(String str) {
        return (byte) Integer.parseInt(str, 16);
    }

    public static byte calcCrc8(byte[] bArr) {
        return calcCrc8(bArr, 0, bArr.length, (byte) 0);
    }

    public static byte calcCrc8(byte[] bArr, int i, int i2) {
        return calcCrc8(bArr, i, i2, (byte) 0);
    }

    public static byte calcCrc8(byte[] bArr, int i, int i2, byte b) {
        for (int i3 = i; i3 < i + i2; i3++) {
            b = CHECKSUM_TABLE[(b ^ bArr[i3]) & 255];
        }
        return b;
    }
}
