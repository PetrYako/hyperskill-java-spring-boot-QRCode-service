package qrcodeapi.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.Map;

@Service
public class QrCodeService {
    private final QRCodeWriter qrCodeWriter = new QRCodeWriter();

    public BufferedImage generateQrCode(String contents, int size, String correctionLevel) {
        Map<EncodeHintType, ?> hints = Map.of(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.valueOf(correctionLevel));

        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(contents, BarcodeFormat.QR_CODE, size, size, hints);
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
    }
}
