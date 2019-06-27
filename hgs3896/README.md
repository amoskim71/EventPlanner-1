# Prototype for our app(Event Planner)

## Need to Implement (Task List)
- [x] Support an easy-to-share button to share media files such as photos and videos as convinient as possible.
    > Adopt the custom multiple image selection library.
- [ ] Sync with the default Android calendar app
    > Request to Google Calendar and get the data
- [ ] Pinpoint a marker on a map either by the current location or by a random choice of an user.
    > Use Kakao Map APIs or Kakao Navigation APIs.

## Details in Code

### Pick multiple files - deprecated (since we use the image picking library(FishBun))
```java
@depreacted
protected void pickFile() {
    Intent requestIntent = new Intent();
    requestIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); //  Set a flag that it is multiple selections.
    requestIntent.setAction(Intent.ACTION_GET_CONTENT); // Set an Action used by an intent.
    requestIntent.setType("image/* | video/*"); // Set MIME.

    final String title = getResources().getString(R.string.pick_chooser_title);
    // Make a chooser intent to show all possible options to deal with multiple file selections.
    Intent chooser = Intent.createChooser(requestIntent, title);
    // Check if there are more than one applications to select media.
    if (requestIntent.resolveActivity(getPackageManager()) != null) {
        // To receive the result(such as which items are selected), start an activity for result.
        startActivityForResult(chooser, 0);
    }
}
```

### Share Multiple Files
```java
// Input: Array List of Uris
protected void shareFile(final ArrayList<Uri> dataURI){
    if(dataURI == null || dataURI.isEmpty()) // Data Check
        return;

    Intent sendIntent = null;

    if(dataURI.size() == 1){
        // Single File
        sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_STREAM, dataURI.get(0));
    }else{
        // Multiple Files
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
```

### Overrided Methods

#### onCreate
```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Link the java object, 'btn_share' with the corresponding xml object, 'R.id.btn_share'.
    btn_share = findViewById(R.id.btn_share); 
    btn_share.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            pickFile();
        }
    });
}
```

#### onActivityResult
```java
@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // If the selection didn't work
    if (resultCode != RESULT_OK) {
        // For the snackbar popup, if nothing has been selected after selection, show a proper message to the user by the Android snackbar.
        Snackbar.make(findViewById(R.id.myCoordinatorLayout), R.string.nothing_selected_msg, Snackbar.LENGTH_SHORT).show();
        // Exit without doing anything else
        return;
    } else {
        ArrayList<Uri> uriList = new ArrayList<>();
        // Get the file's content URI from the incoming Intent
        if(data.getClipData() != null){
            // Multiple Files
            int count = data.getClipData().getItemCount();
            for (int i=0; i<count; i++){
                Uri imageUri = data.getClipData().getItemAt(i).getUri();
                uriList.add(imageUri);
            }
        }
        else if(data.getData() != null){
            // Single File
            Uri imgUri = data.getData();
            uriList.add(imgUri);
        }

        shareFile(uriList);
    }
}
```