package qrcodeapi;

import org.springframework.http.MediaType;

public enum ImageType {
    PNG, JPEG, GIF;

    private final MediaType mediaType;

    ImageType() {
        this.mediaType = MediaType.parseMediaType("image/" + this.name().toLowerCase());
    }

    public MediaType getMediaType() {
        return mediaType;
    }
}
