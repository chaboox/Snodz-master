package choor.boos.snodz;

import android.content.SharedPreferences;
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

public class AjoutEmploye extends AppCompatActivity {
    EditText nom,prenom,tel;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_employe);

        nom = (EditText)findViewById(R.id.nom);
        prenom = (EditText)findViewById(R.id.prenom);
        tel = (EditText)findViewById(R.id.tel);
        Button ajout = (Button)findViewById(R.id.ajoutE);

        SharedPreferences prefs = getSharedPreferences("CachSnodz", MODE_PRIVATE);
        id = prefs.getString("id", "");
        ajout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("employe").child(id);
                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("employe").child(id);;
                Query referenceUser = databaseReference.orderByKey().limitToLast(1);
                referenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int key=0;
                        // idArticle =  dataSnapshot.getChildrenCount()+1;
                        for (DataSnapshot child: dataSnapshot.getChildren()) {
                            Log.d("User key", child.getKey());
                            if(child.getChildrenCount()==0)
                                key= 0;
                            else
                                key = Integer.valueOf(child.getKey()) + 1;

                        }
                        DatabaseReference refArcticle = myRef.child(String.valueOf(key));
                        refArcticle.child("nom").setValue(nom.getText().toString());
                        refArcticle.child("prenom").setValue(prenom.getText().toString());
                        refArcticle.child("telephone").setValue(tel.getText().toString());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });



    }
}
