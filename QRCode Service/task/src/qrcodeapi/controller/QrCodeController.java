package qrcodeapi.controller;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import qrcodeapi.ImageType;
import qrcodeapi.dto.ErrorDto;
import qrcodeapi.service.QrCodeService;

import java.awt.*;
import java.awt.image.BufferedImage;

@RestController
@RequestMapping("/api/qrcode")
public class QrCodeController {

    private final QrCodeService qrCodeService;

    @Autowired
    public QrCodeController(QrCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    @GetMapping
    public ResponseEntity<Object> getQrCode(
            @RequestParam("contents") String contents,
            @RequestParam(value = "size", defaultValue = "250") int size,
            @RequestParam(value = "correction", defaultValue = "L") String correction,
            @RequestParam(value = "type", defaultValue = "png") String type
    ) {
        if (!isValidContents(contents)) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorDto("Contents cannot be null or blank"));
        }

        if (!isValidSize(size)) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorDto("Image size must be between 150 and 350 pixels"));
        }

        if (!isValidCorrection(correction)) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorDto("Permitted error correction levels are L, M, Q, H"));
        }

        if (!isValidType(type)) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorDto("Only png, jpeg and gif image types are supported"));
        }

        BufferedImage qrcode = qrCodeService.generateQrCode(contents, size, correction);
        return ResponseEntity
                .ok()
                .contentType(ImageType.valueOf(type.toUpperCase()).getMediaType())
                .body(qrcode);
    }

    private boolean isValidCorrection(String correction) {
        return correction.equals(ErrorCorrectionLevel.M.name()) ||
                correction.equals(ErrorCorrectionLevel.L.name()) ||
                correction.equals(ErrorCorrectionLevel.H.name()) ||
                correction.equals(ErrorCorrectionLevel.Q.name());
    }

    private boolean isValidContents(String contents) {
        return contents != null && !contents.isBlank();
    }

    private boolean isValidSize(int size) {
        return size >= 150 && size <= 350;
    }

    private boolean isValidType(String type) {
        String uppercaseType = type.toUpperCase();
        return uppercaseType.equals(ImageType.GIF.name()) ||
                uppercaseType.equals(ImageType.PNG.name()) ||
                uppercaseType.equals(ImageType.JPEG.name());
    }
}
