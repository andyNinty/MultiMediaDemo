package andy.lee.multimediademo;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static andy.lee.multimediademo.ExifUtil.resolveBitmapOrientation;

public class MainActivity extends BaseActivity {
    private Button mTakePhoto, mChoosePhoto;
    private ImageView mImageView;
    private Uri mUri;
    private File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTakePhoto = (Button) findViewById(R.id.take_photo);
        mChoosePhoto = (Button) findViewById(R.id.choose_photo);
        mImageView = (ImageView) findViewById(R.id.photo_view);
        mTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });
        mChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestRuntimePermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
                    @Override
                    public void onGranted() {
                        choosePhoto();
                    }

                    @Override
                    public void onDenied(List<String> permissionList) {

                    }
                });
            }
        });

    }

    private void takePhoto() {
        imageFile = new File(getExternalCacheDir(), "image.jpg");
        try {
            if (imageFile.exists()) {
                imageFile.delete();
            }
            imageFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 23) {
            mUri = FileProvider.getUriForFile(this, Constant.AUTHORITY, imageFile);

        } else {
            mUri = Uri.fromFile(imageFile);
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        startActivityForResult(intent, Constant.TAKE_PHOTO);
    }

    private void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, Constant.CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constant.TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        displayImage(mUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case Constant.CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        displayImage(data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    public void displayImage(Uri uri) throws IOException {
        Bitmap bitmap = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
            bitmap = ExifUtil.applyOrientation(bitmap, resolveBitmapOrientation(imageFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mImageView.setImageBitmap(bitmap);
    }
}
