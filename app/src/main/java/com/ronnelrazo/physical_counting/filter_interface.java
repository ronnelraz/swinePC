package com.ronnelrazo.physical_counting;

import androidx.cardview.widget.CardView;

import com.ronnelrazo.physical_counting.adapter.Adapter_Farm;
import com.ronnelrazo.physical_counting.model.model_farm;

import java.util.ArrayList;
import java.util.List;

public interface filter_interface {

    void SearchItem(CardView cardview, int position, model_farm orgData, Adapter_Farm.VHItem holder, ArrayList<ListItem> items, List<model_farm> orgdata);
}
