package choor.boos.snodz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import choor.boos.snodz.CustomAdapter.CustomAdapterClient;
import choor.boos.snodz.CustomAdapter.DataModelClient;

public class ModifierClient extends AppCompatActivity {

    ArrayList<DataModelClient> dataModelClients;
    ListView listView;
    private static CustomAdapterClient adapter;
    String idU ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_client);
        listView=(ListView)findViewById(R.id.list);


        dataModelClients = new ArrayList<>();
        SharedPreferences prefs = getSharedPreferences("CachSnodz", MODE_PRIVATE);
        idU = prefs.getString("id", "");


        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("client").child(idU);
      //  final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("client").child(id);;
        //Query referenceUser = databaseReference.orderByKey().limitToLast(1);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // idArticle =  dataSnapshot.getChildrenCount()+1;
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                        System.out.println("yo "+child.child("nom").getValue().toString());
                  dataModelClients.add(new DataModelClient(child.child("nom").getValue().toString(), child.child("prenom").getValue().toString(), child.child("telephone").getValue().toString(),"September 23, 2008",child.getKey()));


                    Log.d("User key", child.getKey());


                }
                adapter= new CustomAdapterClient(dataModelClients,getApplicationContext());

                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        DataModelClient dataModelClient = dataModelClients.get(position);

                        Snackbar.make(view, dataModelClient.getNom()+"\n"+ dataModelClient.getPrenom()+" API: "+ dataModelClient.getTel(), Snackbar.LENGTH_LONG)
                                .setAction("No action", null).show();
                        Intent i = new Intent(ModifierClient.this,ModifierClientSelect.class);
                       // Log.d("yoo", "  "+id);
                        i.putExtra("idUser",idU);
                        i.putExtra("idClient", dataModelClient.getId());

                        startActivity(i);

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
