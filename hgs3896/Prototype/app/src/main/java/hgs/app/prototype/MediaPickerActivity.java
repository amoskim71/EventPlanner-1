package hgs.app.prototype;

import android.content.Intent;
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
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.sangcomz.fishbun.define.Define;

import java.util.ArrayList;

public class MediaPickerActivity extends AppCompatActivity {

    ArrayList<Uri> path = new ArrayList<>();
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

        FishBun.with(MediaPickerActivity.this)
                .setImageAdapter(new GlideAdapter())
                .setPickerCount(20) // Maximum number of selected photos
                .setSelectedImages(path)
                .setAlbumSpanCount(2, 3)
                .setCamera(true)
                .setButtonInAlbumActivity(true)
                .startAlbum();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Define.ALBUM_REQUEST_CODE){
            if (resultCode == RESULT_OK) {
                path = data.getParcelableArrayListExtra(Define.INTENT_PATH);
                imageAdapter.changePath(path);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList("selections", path);
        super.onSaveInstanceState(outState);
    }
}
