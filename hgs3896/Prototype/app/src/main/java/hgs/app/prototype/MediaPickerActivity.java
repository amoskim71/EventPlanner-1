package hgs.app.prototype;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.util.ArrayList;

import hgs.app.prototype.support.Glide4Engine;

public class MediaPickerActivity extends AppCompatActivity {

    static private int REQUEST_CODE_CHOOSE = 1;

    ArrayList<Uri> path;
    ImageView imgMain;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    MediaAdapter imageAdapter;
    ImageController mainController;
    FloatingActionButton button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            path = savedInstanceState.getParcelableArrayList("selections");
        }else{
            path = new ArrayList<>();
        }

        setContentView(R.layout.activity_picker);

        imgMain = findViewById(R.id.img_main);
        recyclerView = findViewById(R.id.image_rv);
        button = findViewById(R.id.confirm_pick_button);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mainController = new ImageController(imgMain);
        imageAdapter = new MediaAdapter(this, mainController, path);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(imageAdapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!path.isEmpty()) {
                    Intent intent = new Intent();
                    intent.putParcelableArrayListExtra("selections", path);
                    setResult(RESULT_OK, intent);
                }else{
                    setResult(RESULT_CANCELED);
                }
                finish();
            }
        });

        Matisse.from(MediaPickerActivity.this)
                .choose(MimeType.ofAll())
                .countable(true)
                .maxSelectable(9)
                .theme(R.style.Matisse_Dracula)
                // .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new Glide4Engine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK){
            path = (ArrayList<Uri>)Matisse.obtainResult(data);
            imageAdapter.changePath(path);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList("selections", path);
        super.onSaveInstanceState(outState);
    }
}
