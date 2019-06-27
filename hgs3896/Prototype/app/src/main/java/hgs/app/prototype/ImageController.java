package hgs.app.prototype;

import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageController {
    ImageView imgMain;

    ImageController(ImageView imgMain) {
        this.imgMain = imgMain;
    }

    void setImgMain(Uri path) {
        Glide.with(imgMain.getContext())
                .load(path)
                .fitCenter()
                .centerCrop()
                .into(imgMain);
    }
}

