package choor.boos.snodz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Inscription extends AppCompatActivity {
        EditText nom,prenom,email,tel,wilaya;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        Intent intent = getIntent();
        final String keyUser = intent.getStringExtra("keyUser");
         nom = (EditText)findViewById(R.id.nom);
        prenom = (EditText)findViewById(R.id.prenom);
        tel = (EditText)findViewById(R.id.tel);
        wilaya= (EditText)findViewById(R.id.wilaya);
        email = (EditText)findViewById(R.id.email);
        Button inscriptionButton = (Button)findViewById(R.id.inscription);
        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("user").child(keyUser);
        inscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nom.equals("") ||prenom.equals("") ||wilaya.equals("") ||tel.equals("")){
                    Toast.makeText(Inscription.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                }
                else {
                    myRef.child("nom").setValue(nom.getText().toString());
                    myRef.child("prenom").setValue(prenom.getText().toString());
                    myRef.child("telephone").setValue(tel.getText().toString());
                    myRef.child("email").setValue(email.getText().toString());
                    myRef.child("wilaya").setValue(wilaya.getText().toString());
                    myRef.child("statut").setValue("actif");
                    Date c = Calendar.getInstance().getTime();
                    System.out.println("Current time => " + c);

                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    String formattedDate = df.format(c);
                    myRef.child("date_inscription").setValue(formattedDate);
                    myRef.child("date_dactivation").setValue(formattedDate);
                    SharedPreferences.Editor editor = getSharedPreferences("CachSnodz", MODE_PRIVATE).edit();
                    editor.putString("id", keyUser);
                    editor.apply();
                    Intent i = new Intent(Inscription.this,acceuil.class);
                }

            }
        });
    }


}
