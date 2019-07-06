package hgs.app.prototype.dummy.media;

import android.net.Uri;

public class VideoMedia implements FileMedia {
    // 식별 번호
    private long identifier;

    @Override
    public Uri getUri() {
        return null;
    }

    @Override
    public MediaType getMediaType() {
        return MediaType.VIDEO;
    }

    @Override
    public long getIdentifier() {
        return identifier;
    }
}
