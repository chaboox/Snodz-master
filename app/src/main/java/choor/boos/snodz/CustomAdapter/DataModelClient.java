package choor.boos.snodz.CustomAdapter;

/**
 * Created by moi on 20/04/2018.
 */


/**
 * Created by anupamchugh on 09/02/16.
 */
public class DataModelClient {

    String nom;
    String prenom;
    String tel;
    String feature;
    String id;


    public DataModelClient(String nom, String prenom, String tel, String feature , String id) {
        this.nom=nom;
        this.prenom=prenom;
        this.tel=tel;
        this.feature=feature;
        this.id = id;

    }


    public String getNom() {
        return nom;
    }


    public String getPrenom() {
        return prenom;
    }


    public String getTel() {
        return tel;
    }


    public String getFeature() {
        return feature;
    }

    public String getId() {
        return id;
    }

}
