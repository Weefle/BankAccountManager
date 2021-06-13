package fr.weefle.myapplication.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.Collections;

import fr.weefle.myapplication.Adapter.TransactionAdapter;
import fr.weefle.myapplication.MainActivity;
import fr.weefle.myapplication.R;

import static fr.weefle.myapplication.MainActivity.datas;

public class PositionFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_transaction, container, false);
        Collections.reverse(datas);

        ListView shopListView = v.findViewById(R.id.transaction_list_view);
        shopListView.setAdapter(new TransactionAdapter(this.getContext(), datas));

        return v;
    }

}