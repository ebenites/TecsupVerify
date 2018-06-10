package pe.edu.tecsup.tecsupverify.adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pe.edu.tecsup.tecsupverify.R;
import pe.edu.tecsup.tecsupverify.models.User;
import pe.edu.tecsup.tecsupverify.services.TecsupServiceGenerator;
import pe.edu.tecsup.tecsupverify.util.Constants;

public class UsersRVAdapter extends Adapter<UsersRVAdapter.ViewHolder> {

    private FragmentActivity activity;

    private List<User> users;

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public UsersRVAdapter(){
        this.users = new ArrayList<>();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView typeText;
        CircleImageView pictureImage;
        TextView fullnameText;
        TextView groupText;
        TextView descriptionText;

        ViewHolder(View itemView) {
            super(itemView);
            typeText = itemView.findViewById(R.id.type_text);
            pictureImage = itemView.findViewById(R.id.picture_image);
            fullnameText = itemView.findViewById(R.id.fullname_text);
            groupText = itemView.findViewById(R.id.group_text);
            descriptionText = itemView.findViewById(R.id.description_text);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        final User user = users.get(position);

        viewHolder.typeText.setText(user.getTipo());
        viewHolder.fullnameText.setText(user.getFullname());

        if(user.getCardID() == null) return;

        if(user.getCardID().getPicture()) {
            String url = TecsupServiceGenerator.PHOTO_URL + user.getId();
            Picasso.with(viewHolder.itemView.getContext()).load(url).placeholder(ResourcesCompat.getDrawable(viewHolder.itemView.getResources(), R.drawable.ic_profile, null)).into(viewHolder.pictureImage); // Error en vector placeholder: // https://github.com/square/picasso/issues/1109#issuecomment-228886243
        }else{
            viewHolder.pictureImage.setImageResource(R.drawable.ic_profile);
        }

        viewHolder.groupText.setText(user.getCardID().getPorduct()); // Para empleado mostrar sede - departamento

        if(Constants.USUARIO_TIPO_PARTICIPANTE.equals(user.getTipo())){
            if(!user.getCardID().getActive()){
                viewHolder.groupText.setText(R.string.inactive_message);
            }
            if(user.getCardID().getExpiration() == null){
                viewHolder.descriptionText.setTextColor(viewHolder.itemView.getResources().getColor(R.color.red));
                viewHolder.descriptionText.setText(R.string.expired_message);
            }else{
                viewHolder.descriptionText.setTextColor(viewHolder.itemView.getResources().getColor(R.color.secondary_text));
                viewHolder.descriptionText.setText(String.format("VENCE: %s", user.getCardID().getExpiration()));
            }
        }else if(Constants.USUARIO_TIPO_ALUMNO.equals(user.getTipo())){
            viewHolder.descriptionText.setText(String.format("CONDICIÓN: %s", user.getCardID().getCondition()));
        }else if(Constants.USUARIO_TIPO_EMPLEADO.equals(user.getTipo())){
            // Para empleado mostrar el cargo
        }else{
            viewHolder.groupText.setText(user.getEmail());
            viewHolder.descriptionText.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return this.users.size();
    }

}
