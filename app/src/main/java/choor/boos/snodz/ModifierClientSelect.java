package choor.boos.snodz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ModifierClientSelect extends AppCompatActivity {
    EditText nom,prenom,tel;
          String  id,idClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_client_select);
        nom = (EditText)findViewById(R.id.nom);
        prenom = (EditText)findViewById(R.id.prenom);
        tel = (EditText)findViewById(R.id.tel);
        Button Enre = (Button)findViewById(R.id.Enregistrer);
        Intent i = getIntent();
        id = i.getStringExtra("idUser");
        idClient = i.getStringExtra("idClient");
        Log.d("yoo", idClient+"  "+id);
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("client").child(id).child(idClient);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nom.setText(dataSnapshot.child("nom").getValue().toString());
                prenom.setText(dataSnapshot.child("prenom").getValue().toString());
                tel.setText(dataSnapshot.child("telephone").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Enre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("client").child(id).child(idClient);

               
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                      
                        // idArticle =  dataSnapshot.getChildrenCount()+1;
                      
                     
                        myRef.child("nom").setValue(nom.getText().toString());
                        myRef.child("prenom").setValue(prenom.getText().toString());
                        myRef.child("telephone").setValue(tel.getText().toString());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });


    }
}
