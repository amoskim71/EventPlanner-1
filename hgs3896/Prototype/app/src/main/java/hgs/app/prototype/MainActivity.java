package hgs.app.prototype;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button btn_share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_share = findViewById(R.id.btn_share);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MediaPickerActivity.class);
                startActivityForResult(i, 0);
            }
        });
    }

    @Deprecated
    protected void pickFile() {
        Intent requestIntent = new Intent();
        requestIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        requestIntent.setAction(Intent.ACTION_GET_CONTENT);
        requestIntent.setType("image/* | video/* | application/vnd.google.panorama360+jpg");

        final String title = getResources().getString(R.string.pick_chooser_title);

        Intent chooser = Intent.createChooser(requestIntent, title);

        if (requestIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(chooser, 0);
        }
    }

    protected void shareFile(final ArrayList<Uri> dataURI){
        if(dataURI == null || dataURI.isEmpty())
            return;

        Intent sendIntent = null;

        if(dataURI.size() == 1){
            sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_STREAM, dataURI.get(0));
        }else{
            sendIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            sendIntent.putExtra(Intent.EXTRA_STREAM, dataURI);
        }

        sendIntent.setType("image/* | video/*");

        final String title = getResources().getString(R.string.share_chooser_title);

        // Create intent to show the chooser dialog
        Intent chooser = Intent.createChooser(sendIntent, title);

        // Verify the original intent will resolve to at least one activity
        if (sendIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If the selection didn't work
        if (resultCode != RESULT_OK) {
            // Exit without doing anything else
            Snackbar.make(findViewById(R.id.myCoordinatorLayout), R.string.nothing_selected_msg, Snackbar.LENGTH_SHORT).show();
            return;
        } else {
            ArrayList<Uri> uriList = data.getParcelableArrayListExtra("selections");
            // Get the file's content URI from the incoming Intent
            if(data.getClipData() != null){
                int count = data.getClipData().getItemCount();
                for (int i=0; i<count; i++){
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    uriList.add(imageUri);
                }
            }
            else if(data.getData() != null){
                Uri imgUri = data.getData();
                uriList.add(imgUri);
            }

            shareFile(uriList);
        }
    }
}
