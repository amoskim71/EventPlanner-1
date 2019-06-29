package hgs.app.prototype;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
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

    protected void shareFile(final ArrayList<Uri> dataURI){
        if(dataURI == null || dataURI.isEmpty())
            return;

        final ContentResolver cr = getContentResolver();
        final String title = getResources().getString(R.string.share_chooser_title);
        Intent sendIntent;

        if(cr.getType(dataURI.get(0)).contains("video")){
            sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_STREAM, dataURI);
            sendIntent.setType("video/*");
        }else{
            sendIntent = new Intent(Intent.ACTION_SEND_MULTIPLE); // MimeTypes might be mixed.
            sendIntent.putExtra(Intent.EXTRA_STREAM, dataURI);
            sendIntent.setType("image/*");
        }

        sendWithChooser(sendIntent, title);
    }

    // A helper function for starting a chooser intent
    protected void sendWithChooser(Intent intent, String title){
        // Create intent to show the chooser dialog
        Intent chooser = Intent.createChooser(intent, title);

        // Verify the original intent will resolve to at least one activity
        if (intent.resolveActivity(getPackageManager()) != null) {
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
                    uriList.add(data.getClipData().getItemAt(i).getUri());
                }
            }
            else if(data.getData() != null){
                uriList.add(data.getData());
            }

            shareFile(uriList);
        }
    }
}
