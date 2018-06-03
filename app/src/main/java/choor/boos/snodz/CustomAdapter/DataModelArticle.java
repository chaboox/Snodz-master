package choor.boos.snodz.CustomAdapter;

/**
 * Created by moi on 21/04/2018.
 */

public class DataModelArticle {
    String nom;
    String prixAchat;
    String prixVente;
    String type;
    String taxe;
    String quantite;


    public DataModelArticle(String nom, String prixAchat, String prixVente, String type, String taxe, String quantite ) {
        this.nom=nom;
        this.prixAchat=prixAchat;
        this.prixVente=prixVente;
        this.type=type;
        this.taxe = taxe;
        this.quantite = quantite;

    }


    public String getNom() {
        return nom;
    }


    public String getPrixAchat() {
        return prixAchat;
    }


    public String getPrixVente() {
        return prixVente;
    }


    public String getType() {
        return type;
    }

    public String getTaxe() {
        return taxe;
    }

    public String getQuantite() {
        return quantite;
    }
}
