package br.feevale.environmentalthreatsfirebase;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    //FirebaseDatabase db = FirebaseDatabase.getInstance();
    //DatabaseReference root = db.getReference();
    //DatabaseReference threats = root.child(Utils.THREATS_KEY);
    FirebaseDatabase db;
    DatabaseReference root;
    DatabaseReference threats;
    FirebaseListAdapter<EnvironmentalThreat> listAdapter;
    ListView listThreat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseDatabase.getInstance();
        root = db.getReference();
        threats = root.child(Utils.THREATS_KEY);


        listThreat = findViewById(R.id.listThreat);
        listAdapter = new FirebaseListAdapter<EnvironmentalThreat>(this,EnvironmentalThreat.class, R.layout.threat_list_item, this.threats) {
            @Override
            protected void populateView(View v, EnvironmentalThreat model, int position) {
                TextView txtDesc = v.findViewById(R.id.txtDescData);
                TextView txtDate = v.findViewById(R.id.txtDateData);
                ImageView imgImage = v.findViewById(R.id.imgPhotoData);

                txtDesc.setText(model.getDescription());
                txtDate.setText(model.getDate());
                if (model.getImage() != null){
                    byte bytes[] = Base64.decode(model.getImage(), Base64.DEFAULT);
                    Bitmap img = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imgImage.setImageBitmap(img);
                }
            }
        };
        listThreat.setAdapter(listAdapter);

        listThreat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DatabaseReference item = listAdapter.getRef(position);
                onClickEditThreat(item.getKey(), listAdapter.getItem(position));
            }
        });

        listThreat.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DatabaseReference item = listAdapter.getRef(position);
                item.removeValue();
                Toast.makeText(getBaseContext(), "Ameaça deletada", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    public void onClickAddThreat(View v){
        Intent intent = new Intent(getBaseContext(), AddThreat.class);
        startActivityForResult(intent, Utils.InternalResponse.UPDATE_ON_INSERT);
    }

    public void onClickEditThreat(String key, EnvironmentalThreat threat){
        Intent intent = new Intent(getBaseContext(), EditThreat.class);
        intent.putExtra("KEY", key);
        intent.putExtra("OBJ", threat);
        startActivityForResult(intent, Utils.InternalResponse.UPDATE_ON_UPDATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Utils.InternalResponse.UPDATE_ON_INSERT){
            listAdapter.notifyDataSetChanged();
            Toast.makeText(getBaseContext(), "Ameaça cadastrada!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (resultCode == Utils.InternalResponse.UPDATE_ON_UPDATE){
            listAdapter.notifyDataSetChanged();
            Toast.makeText(getBaseContext(), "Ameaça editada!", Toast.LENGTH_SHORT).show();
            return;
        }
    }

}
