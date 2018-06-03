package choor.boos.snodz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class acceuil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceuil);
        Button ajoutB = (Button)findViewById(R.id.ajoutarticle);
        Button ajoutC = (Button)findViewById(R.id.ajoutClient);
        Button ajoutE = (Button)findViewById(R.id.ajoutEmploye);
        Button modifierClient = (Button)findViewById(R.id.modifierClient);
        ajoutB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(acceuil.this, ajoutArticle.class);
                startActivity(i);
            }
        });

        ajoutC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(acceuil.this, ajoutClient.class);
                startActivity(i);
            }
        });
        ajoutE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(acceuil.this, AjoutEmploye.class);
                startActivity(i);
            }
        });

        modifierClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(acceuil.this, ModifierClient.class);
                startActivity(i);
            }
        });
    }
}
