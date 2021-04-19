package br.feevale.environmentalthreatsfirebase;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditThreat extends AppCompatActivity {

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference root = db.getReference();
    DatabaseReference threats = root.child(Utils.THREATS_KEY);
    EditText txtAddress, txtDate, txtDescription;
    EnvironmentalThreat current;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_threat);

        setResult(-1);

        txtAddress = findViewById(R.id.txtAddress);
        txtDate = findViewById(R.id.txtDate);
        txtDate.addTextChangedListener(Utils.Mask.insert("##/##/####", txtDate));
        txtDescription = findViewById(R.id.txtDescription);

        this.key = getIntent().getStringExtra("KEY");
        this.current = (EnvironmentalThreat) getIntent().getSerializableExtra("OBJ");

        txtAddress.setText(current.getAddress());
        txtDate.setText(current.getDate());
        txtDescription.setText(current.getDescription());
    }

    public void onClickEdit(View v){
        if (!Utils.validateDate(txtDate.getText().toString())){
            Toast.makeText(getBaseContext(), "Forneça uma data no formato dd/mm/yyyy.", Toast.LENGTH_SHORT).show();
            return;
        }
        current.setAddress(txtAddress.getText().toString());
        current.setDate(txtDate.getText().toString());
        current.setDescription(txtDescription.getText().toString());
        threats.child(this.key).setValue(this.current);

        setResult(Utils.InternalResponse.UPDATE_ON_UPDATE);
        finish();
    }


}