package br.feevale.environmentalthreatsfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;

public class AddThreat extends AppCompatActivity {

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference root = db.getReference();
    DatabaseReference threats = root.child(Utils.THREATS_KEY);
    EditText txtAddress, txtDate, txtDescription;
    Bitmap img;
    ImageView imgImage;
    Boolean hasImage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_threat);

        setResult(-1);

        txtAddress = findViewById(R.id.txtAddress);
        txtDate = findViewById(R.id.txtDate);
        txtDate.addTextChangedListener(Utils.Mask.insert("##/##/####", txtDate));
        txtDescription = findViewById(R.id.txtDescription);
        imgImage = findViewById(R.id.imgPhoto);
    }

    public void onClickAdd(View v){
        if (!Utils.validateDate(txtDate.getText().toString())){
            Toast.makeText(getBaseContext(), "Forneça uma data no formato dd/mm/yyyy.", Toast.LENGTH_SHORT).show();
            return;
        }
        EnvironmentalThreat et = new EnvironmentalThreat(txtDate.getText().toString(), txtDescription.getText().toString(), txtAddress.getText().toString());

        if (hasImage){
            String bmpEncoded = encodePhoto();
            hasImage = false;
            et.setImage(bmpEncoded);
        }

        String key = threats.push().getKey();
        threats.child(key).setValue(et);
        setResult(Utils.InternalResponse.UPDATE_ON_INSERT);
        finish();
    }

    public String encodePhoto(){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        this.img.compress(Bitmap.CompressFormat.JPEG, 100, out);
        return Base64.encodeToString(out.toByteArray(), Base64.DEFAULT);
    }

    public void onClickAddPhoto(View v){
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, Utils.InternalResponse.CAMERA_CALL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utils.InternalResponse.CAMERA_CALL && resultCode == RESULT_OK){
            this.img = (Bitmap) data.getExtras().get("data");
            imgImage.setImageBitmap(this.img);
            hasImage = true;
        }
    }
}

