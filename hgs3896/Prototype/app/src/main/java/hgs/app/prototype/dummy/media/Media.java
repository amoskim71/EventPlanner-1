package hgs.app.prototype.dummy.media;

public interface Media {
    enum MediaType { PHOTO, VIDEO, VOTE };
    public MediaType getMediaType();
    public long getIdentifier();
}
