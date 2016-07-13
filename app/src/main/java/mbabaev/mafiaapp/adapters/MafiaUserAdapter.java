package mbabaev.mafiaapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import mbabaev.mafiaapp.R;
import mbabaev.mafiaapp.model.User;

public class MafiaUserAdapter extends ArrayAdapter<User> {
    Context context;
    List<User> items;

    public MafiaUserAdapter(Context context, List<User> items){
        super(context, R.layout.cell ,items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.cell, parent, false);

        TextView userName = (TextView)
                rowView.findViewById(R.id.user_name_in_voting_list);

        User currItem = items.get(position);
        userName.setText(currItem.name);

        return rowView;
    }
}
