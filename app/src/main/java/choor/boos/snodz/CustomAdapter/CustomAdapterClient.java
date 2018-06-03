package choor.boos.snodz.CustomAdapter;

/**
 * Created by moi on 20/04/2018.
 */



        import android.content.Context;
        import android.support.design.widget.Snackbar;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.animation.Animation;
        import android.view.animation.AnimationUtils;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;
        import java.util.ArrayList;

        import choor.boos.snodz.R;


/**
 * Created by anupamchugh on 09/02/16.
 */
public class CustomAdapterClient extends ArrayAdapter<DataModelClient> implements View.OnClickListener{

    private ArrayList<DataModelClient> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtNom;
        TextView txtPrenom;
        TextView txtTel;
        ImageView info;
    }



    public CustomAdapterClient(ArrayList<DataModelClient> data, Context context) {
        super(context, R.layout.row_item_client, data);
        this.dataSet = data;
        this.mContext=context;

    }


    @Override
    public void onClick(View v) {


        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModelClient dataModelClient =(DataModelClient)object;




        switch (v.getId())
        {

            case R.id.item_info:

                Snackbar.make(v, "Release date " + dataModelClient.getFeature(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();

                break;


        }


    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModelClient dataModelClient = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_client, parent, false);
            viewHolder.txtNom = (TextView) convertView.findViewById(R.id.nom);
            viewHolder.txtPrenom = (TextView) convertView.findViewById(R.id.prenom);
            viewHolder.txtTel = (TextView) convertView.findViewById(R.id.tel);
            viewHolder.info = (ImageView) convertView.findViewById(R.id.item_info);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;


        viewHolder.txtNom.setText(dataModelClient.getNom());
        viewHolder.txtPrenom.setText(dataModelClient.getPrenom());
        viewHolder.txtTel.setText(dataModelClient.getTel());
        viewHolder.info.setOnClickListener(this);
        viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }


}
