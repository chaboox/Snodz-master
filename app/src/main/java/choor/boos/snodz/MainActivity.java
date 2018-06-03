package choor.boos.snodz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.nio.BufferUnderflowException;

public class MainActivity extends AppCompatActivity {
    TextView t;
    Button b;
    EditText username,mdp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         t = (TextView)findViewById(R.id.hello);
         b = (Button) findViewById(R.id.button2);
         username = (EditText) findViewById(R.id.editText);
         mdp = (EditText) findViewById(R.id.editText2);


       // FirebaseDatabase database = FirebaseDatabase.getInstance();






        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("user");
                String id =username.getText().toString();
                id = id.replace("user","");
                Query referenceUser = myRef.orderByKey().equalTo(id);
                referenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                    //    Log.d("yo nb argu",""+dataSnapshot.getChildrenCount());
                        if(dataSnapshot.getChildrenCount()==0){
                            Toast.makeText(MainActivity.this,"veuillez verifier le username",Toast.LENGTH_LONG).show();
                        }
                        else
                        for (DataSnapshot child: dataSnapshot.getChildren()){
                           // int key = Integer.valueOf(child.getKey());
                            Log.d("User val", child.child("mdp").getValue().toString());
                            if((child.child("mdp").getValue().toString()).equals(mdp.getText().toString())){
                               // Toast.makeText(MainActivity.this,"good"+ child.getChildrenCount(),Toast.LENGTH_LONG).show();
                                if(child.getChildrenCount()==2) {//go to inscription

                                    Intent i = new Intent(MainActivity.this,Inscription.class);
                                    i.putExtra("keyUser",child.getKey()) ;
                                    startActivity(i);
                                }
                                else{ //deja inscris go to acceuil
                                    SharedPreferences.Editor editor = getSharedPreferences("CachSnodz", MODE_PRIVATE).edit();
                                    editor.putString("id", child.getKey());
                                    editor.apply();
                                    Intent i2 = new Intent(MainActivity.this,acceuil.class);
                                    startActivity(i2);
                                }

                            }
                            else   Toast.makeText(MainActivity.this,"veuillez verifier le mot de passe",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

    }
}
